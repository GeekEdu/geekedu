package com.zch.oss.adapter.tencent;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.jwt.JWT;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.qcloud.vod.common.FileUtil;
import com.qcloud.vod.common.StringUtil;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.vod.v20180717.VodClient;
import com.tencentcloudapi.vod.v20180717.models.*;
import com.zch.common.core.utils.RandomUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.common.mvc.exception.CommonException;
import com.zch.oss.adapter.MediaStorageAdapter;
import com.zch.oss.adapter.MediaUploadResult;
import com.zch.oss.config.properties.TencentProperties;
import com.zch.oss.domain.po.Media;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.zch.oss.enums.FileErrorInfo.Msg.*;

/**
 * @author Poison02
 * @date 2023/12/31
 */
@Slf4j
public class TencentMediaStorage implements MediaStorageAdapter {

    private final VodClient vodClient;

    private final TencentProperties tencentProperties;


    public TencentMediaStorage(VodClient vodClient, TencentProperties tencentProperties) {
        this.vodClient = vodClient;
        this.tencentProperties = tencentProperties;
    }

    private static final String CONTEXT_TEMPLATE =
            "secretId=%s&currentTimeStamp=%d&expireTime=%d&random=%d";
    private static final String CONTEXT_TEMPLATE_WITH_PROCEDURE =
            "secretId=%s&currentTimeStamp=%d&expireTime=%d&random=%d&procedure=%s";
    private static final String[] MEDIA_INFO_FILTERS = new String[]{"basicInfo", "metaData"};

    @Override
    public String getUploadSignature() {
        // 1. 获取加密工具
        HMac mac = new HMac(HmacAlgorithm.HmacSHA1, tencentProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        // 2. 准备加密数据
        long now = System.currentTimeMillis() / 1000;
        long endTime = now + tencentProperties.getVod().getVodValidSeconds();
        String procedure = tencentProperties.getVod().getProcedure();
        String context;
        if (StringUtils.isBlank(procedure)) {
            context = String.format(CONTEXT_TEMPLATE,
                    URLEncoder.encode(tencentProperties.getSecretId(), StandardCharsets.UTF_8),
                    now,
                    endTime,
                    RandomUtils.randomInt(0, Integer.MAX_VALUE)
            );
        } else {
            context = String.format(CONTEXT_TEMPLATE_WITH_PROCEDURE,
                    URLEncoder.encode(tencentProperties.getSecretId(), StandardCharsets.UTF_8),
                    now,
                    endTime,
                    RandomUtils.randomInt(0, Integer.MAX_VALUE),
                    procedure
            );
        }
        // 3. 加密返回
        byte[] bytes = ArrayUtil.addAll(mac.digest(context), context.getBytes(StandardCharsets.UTF_8));
        return Base64.encode(bytes);
    }

    @Override
    public String getPlaySignature(String mediaId, Long userId, Integer freeExpire) {
        long currentTime = System.currentTimeMillis() / 1000;

        HashMap<String, Object> urlAccessInfo = new HashMap<>(2);
        if (freeExpire != null) {
            // 试看时间 秒
            urlAccessInfo.put("exper", freeExpire * 60);
        }
        HashMap<String, Object> contentInfo = new HashMap<>(2);
        // contentInfo为自适应码流
        contentInfo.put("audioVideoType", "RawAdaptive");
        contentInfo.put("rawAdaptiveDefinition", 10);
        return JWT.create()
                .setHeader("alg", "HS256")
                .setHeader("typ", "JWT")
                .setKey(tencentProperties.getVod().getUrlKey().getBytes(StandardCharsets.UTF_8))
                .setPayload("appId", tencentProperties.getAppId())
                .setPayload("fileId", mediaId)
                .setPayload("contentInfo", contentInfo)
                .setPayload("currentTimeStamp", currentTime)
                // .setPayload("pcfg", tencentProperties.getVod().getPfcg())
                .setPayload("urlAccessInfo", urlAccessInfo)
                .sign();
    }

    @Override
    public MediaUploadResult uploadFile(String mediaName, InputStream inputStream, long contentLength) {
        CommitUploadResponse response = null;
        try {
            // 1.申请上传
            ApplyUploadResponse applyUploadResponse = applyUpload(mediaName);
            // 2.开始上传
            handleUpload(inputStream, contentLength, applyUploadResponse);
            // 3.确认上传
            response = commitUpload(applyUploadResponse);
        } catch (Exception e) {
            log.error("上传视频文件【{}】失败", mediaName, e);
            throw new CommonException(MEDIA_UPLOAD_ERROR, e);
        }
        // 3.解析结果
        MediaUploadResult result = new MediaUploadResult();
        result.setMediaId(response.getFileId());
        result.setMediaUrl(response.getMediaUrl());
        result.setCoverUrl(response.getCoverUrl());
        result.setRequestId(response.getRequestId());
        return result;
    }

    @Override
    public void deleteFile(String mediaId) {
        try {
            DeleteMediaRequest request = new DeleteMediaRequest();
            request.setFileId(mediaId);
            vodClient.DeleteMedia(request);
        } catch (TencentCloudSDKException e) {
            throw new CommonException(MEDIA_DELETE_ERROR, e);
        }
    }

    @Override
    public void deleteFiles(List<String> mediaIds) {
        for (String mediaId : mediaIds) {
            deleteFile(mediaId);
        }
    }

    @Override
    public List<Media> queryMediaInfos(String... mediaIds) {
        // 1. 请求参数
        DescribeMediaInfosRequest request = new DescribeMediaInfosRequest();
        request.setFileIds(mediaIds);
        request.setFilters(MEDIA_INFO_FILTERS);
        // 2. 发送请求
        DescribeMediaInfosResponse response = null;
        try {
            response = vodClient.DescribeMediaInfos(request);
        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("获取媒资信息异常!", e);
        }
        // 3. 解析结果
        MediaInfo[] mediaInfoSet = response.getMediaInfoSet();
        if (mediaInfoSet == null || mediaInfoSet.length == 0) {
            return Collections.emptyList();
        }
        // 4. 数据转换
        List<Media> list = new ArrayList<>(mediaInfoSet.length);
        for (MediaInfo info : mediaInfoSet) {
            Media media = new Media();
            media.setMediaId(info.getFileId());
            MediaMetaData mediaMetaData = info.getMetaData();
            MediaBasicInfo basicInfo = info.getBasicInfo();
            media.setMediaLink(basicInfo.getMediaUrl());
            media.setMediaName(basicInfo.getName());
            media.setSize(mediaMetaData.getSize());
            media.setDuration(Double.valueOf(mediaMetaData.getDuration()));
            list.add(media);
        }
        return list;
    }

    @Override
    public String getPlayUrl(String mediaId) {
        String playUrl;
        String[] filed = new String[] {mediaId};
        String[] filters = new String[] {"basicInfo"};
        DescribeMediaInfosRequest req = new DescribeMediaInfosRequest();
        req.setFilters(filters);
        req.setFileIds(filed);
        DescribeMediaInfosResponse resp;
        try {
            resp = vodClient.DescribeMediaInfos(req);
            playUrl = resp.getMediaInfoSet()[0].getBasicInfo().getMediaUrl();
            return playUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        // 腾讯云上的加密key
//        String pKey = tencentProperties.getVod().getUrlKey();
//        Long appId = tencentProperties.getAppId();
//        // 1. 获取加密工具
//        HMac mac = new HMac(HmacAlgorithm.HmacSHA256, pKey.getBytes(StandardCharsets.UTF_8));
//        Map<String, String> header = new HashMap<>(2);
//        header.put("alg", "HS256");
//        header.put("typ", "JWT");
//        Map<String, Object> payload = new HashMap<>(7);
//        payload.put("type", "DrmToken");
//        payload.put("appId", appId);
//        payload.put("fileId", mediaId);
//        payload.put("currentTimeStamp", System.currentTimeMillis());
//        payload.put("expireTimeStamp", null);
//        payload.put("random", RandomUtils.randomInt(10000000, 99999999));
//        payload.put("issuer", "client");
//        try {
//            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKey = new SecretKeySpec(pKey.getBytes(), "HmacSHA256");
//            sha256HMAC.init(secretKey);
//
//            String data = java.util.Base64.getUrlEncoder().encodeToString(header.toString().getBytes()) + "." + java.util.Base64.getUrlEncoder().encodeToString(payload.toString().getBytes());
//            // 计算signature
//            byte[] signatureBytes = sha256HMAC.doFinal(data.getBytes());
//            String signature = java.util.Base64.getUrlEncoder().encodeToString(signatureBytes);
//            // header
//            String headers = java.util.Base64.getUrlEncoder().encodeToString(header.toString().getBytes());
//            // payload
//            String payloads = java.util.Base64.getUrlEncoder().encodeToString(payload.toString().getBytes());
//            // 最终DrmToken
//            String drmToken = headers + "~" + payloads + "~" + signature;
//
//            return drmToken;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }
    }

    /**
     * 申请上传视频
     * @param mediaName 视频名
     * @return
     */
    private ApplyUploadResponse applyUpload(String mediaName) {
        ApplyUploadRequest req = new ApplyUploadRequest();
        req.setMediaName(mediaName);
        req.setMediaType(FileUtil.getFileType(mediaName));
        req.setProcedure(tencentProperties.getVod().getProcedure());
        TencentCloudSDKException err = null;
        int i = 0;
        while (i < 4) {
            try {
                // 返回的resp是一个ApplyUploadResponse的实例，与请求对象对应
                return vodClient.ApplyUpload(req);
            } catch (TencentCloudSDKException e) {
                if (StringUtil.isEmpty(e.getRequestId())) {
                    err = e;
                    ++i;
                    continue;
                }
                throw new CommonException(MEDIA_APPLY_UPLOAD_ERROR, e);
            }
        }
        throw new RuntimeException(MEDIA_APPLY_UPLOAD_ERROR, err);
    }

    /**
     * 上传视频
     * @param inputStream 文件流
     * @param contentLength
     * @param applyUploadResponse
     */
    private void handleUpload(InputStream inputStream, long contentLength, ApplyUploadResponse applyUploadResponse) {
        // 1.整理授权上传的凭证
        COSCredentials credentials = null;
        if (applyUploadResponse.getTempCertificate() != null) {
            TempCertificate certificate = applyUploadResponse.getTempCertificate();
            credentials = new BasicSessionCredentials(certificate.getSecretId(), certificate.getSecretKey(), certificate.getToken());
        } else {
            credentials = new BasicCOSCredentials(tencentProperties.getSecretId(), tencentProperties.getSecretKey());
        }
        // 2.准备上传客户端
        ClientConfig clientConfig = new ClientConfig(new Region(applyUploadResponse.getStorageRegion()));
        clientConfig.setHttpProtocol(HttpProtocol.https);
        COSClient cosClient = new COSClient(credentials, clientConfig);
        TransferManager transferManager = new TransferManager(cosClient);
        try {
            // 3.开始上传
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(contentLength);
            Upload upload = transferManager.upload(applyUploadResponse.getStorageBucket(), applyUploadResponse.getMediaStoragePath(), inputStream, metadata);
            // 4.等待上传完成
            upload.waitForCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 5.关闭客户端
            transferManager.shutdownNow();
        }
    }

    /**
     * 确认上传
     * @param applyUploadResponse
     * @return
     */
    private CommitUploadResponse commitUpload(ApplyUploadResponse applyUploadResponse) {
        CommitUploadRequest request = new CommitUploadRequest();
        request.setVodSessionKey(applyUploadResponse.getVodSessionKey());
        TencentCloudSDKException err = null;
        int i = 0;
        while (i < 4) {
            try {
                return vodClient.CommitUpload(request);
            } catch (TencentCloudSDKException e) {
                if (StringUtil.isEmpty(e.getRequestId())) {
                    err = e;
                    ++i;
                    continue;
                }

                throw new CommonException(MEDIA_COMMIT_UPLOAD_ERROR, e);
            }

        }
        throw new CommonException(MEDIA_COMMIT_UPLOAD_ERROR, err);
    }

    public static void main(String[] args) {
        String pKey = "wHrNZ3ihG2Eq1gfDN3vG";
        Long appId = 1315662121L;
        Map<String, String> header = new HashMap<>(2);
        header.put("alg", "HS256");
        header.put("typ", "JWT");
        Map<String, Object> payload = new HashMap<>(7);
        payload.put("type", "DrmToken");
        payload.put("appId", appId);
        payload.put("fileId", 1397757886313096284L);
        payload.put("currentTimeStamp", System.currentTimeMillis());
        payload.put("expireTimeStamp", null);
        payload.put("random", RandomUtils.randomInt(10000000, 99999999));
        payload.put("issuer", "client");
        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(pKey.getBytes(), "HmacSHA256");
            sha256HMAC.init(secretKey);

            String data = java.util.Base64.getUrlEncoder().encodeToString(header.toString().getBytes()) + "." + java.util.Base64.getUrlEncoder().encodeToString(payload.toString().getBytes());
            // 计算signature
            byte[] signatureBytes = sha256HMAC.doFinal(data.getBytes());
            String signature = java.util.Base64.getUrlEncoder().encodeToString(signatureBytes);
            // header
            String headers = java.util.Base64.getUrlEncoder().encodeToString(header.toString().getBytes());
            // payload
            String payloads = java.util.Base64.getUrlEncoder().encodeToString(payload.toString().getBytes());
            // 最终DrmToken
            String drmToken = headers + "~" + payloads + "~" + signature;
            System.out.println(headers + " " + payloads + " " + signature);
            System.out.println(drmToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

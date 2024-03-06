package com.zch.oss.adapter.tencent;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.MD5;
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
import com.zch.common.mvc.exception.CommonException;
import com.zch.oss.adapter.MediaStorageAdapter;
import com.zch.oss.adapter.MediaUploadResult;
import com.zch.oss.config.properties.TencentProperties;
import com.zch.oss.domain.po.Media;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
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
    private static final String[] MEDIA_INFO_FILTERS = new String[]{"basicInfo", "metaData", "adaptiveDynamicStreamingInfo"};

    private static final String HMAC_ALGORITHM = "HmacSHA1"; //签名算法
    private static final String CONTENT_CHARSET = "UTF-8";

    public static byte[] byteMerger(byte[] byte1, byte[] byte2) {
        byte[] byte3 = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, byte3, 0, byte1.length);
        System.arraycopy(byte2, 0, byte3, byte1.length, byte2.length);
        return byte3;
    }

    @Override
    public String getUploadSignature() {
        long now = System.currentTimeMillis() / 1000;
        String strSign = "";
        String contextStr = "";

        // 生成原始参数字符串
        long endTime = (now + tencentProperties.getVod().getVodValidSeconds());
        contextStr += "secretId=" + java.net.URLEncoder.encode(tencentProperties.getSecretId(), StandardCharsets.UTF_8);
        contextStr += "&currentTimeStamp=" + now;
        contextStr += "&expireTime=" + endTime;
        contextStr += "&random=" + RandomUtils.randomInt(0, Integer.MAX_VALUE);
        // 任务流可以用 可以不用
        // contextStr += "&procedure=" + tencentProperties.getVod().getProcedure();

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(tencentProperties.getSecretKey().getBytes(StandardCharsets.UTF_8), mac.getAlgorithm());
            mac.init(secretKey);


            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes(StandardCharsets.UTF_8));
            strSign = Base64.encode(sigBuf);
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strSign;

    }

    @Override
    public String getPlaySignature(String mediaId, Long userId, Integer freeExpire) {
        long currentTime = System.currentTimeMillis() / 1000;

        Map<String, Object> urlAccessInfo = new HashMap<>(2);
        if (freeExpire != null) {
            // 试看时间 秒
            urlAccessInfo.put("exper", freeExpire * 60);
            urlAccessInfo.put("rlimit", 3);
            urlAccessInfo.put("us", "72d4cd1101");
        }
        Map<String, Object> contentInfo = new HashMap<>(2);
        // contentInfo为自适应码流
        contentInfo.put("audioVideoType", "ProtectedAdaptive"); // 加密
        Map<String, Object> DRMAdaptiveInfo = new HashMap<>(1);
        DRMAdaptiveInfo.put("privateEncryptionDefinition", 12);
        contentInfo.put("rawAdaptiveDefinition", DRMAdaptiveInfo);
        return JWT.create()
                .setHeader("alg", "HS256")
                .setHeader("typ", "JWT")
                .setKey(tencentProperties.getVod().getUrlKey().getBytes(StandardCharsets.UTF_8))
                .setPayload("appId", tencentProperties.getAppId())
                .setPayload("fileId", mediaId)
                .setPayload("contentInfo", contentInfo)
                .setPayload("currentTimeStamp", currentTime)
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
    }

    public String getKeyPlayUrl(String mediaId) {
        String playUrl;
        String[] filed = new String[] {mediaId};
        DescribeMediaInfosRequest req = new DescribeMediaInfosRequest();
        req.setFilters(MEDIA_INFO_FILTERS);
        req.setFileIds(filed);
        DescribeMediaInfosResponse resp;
        // 随机 us
        String us = RandomUtils.randomString(8);
        // 生成key签名
        String key = keySignature(tencentProperties.getVod().getPKey(), 60, 3, us);
        // 当前时间戳
        long current = System.currentTimeMillis() / 1000;
        try {
            resp = vodClient.DescribeMediaInfos(req);
            // 内容URL
            playUrl = resp.getMediaInfoSet()[0].getAdaptiveDynamicStreamingInfo().getAdaptiveDynamicStreamingSet()[0].getUrl();
            // DRMToken生成
            String drmToken = generateDrmToken(tencentProperties.getVod().getUrlKey(), current, "DrmToken", tencentProperties.getAppId(), mediaId);
            String finalUrl = generateFinalUrl(playUrl, drmToken) + key;
            return finalUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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

    /**
     * key防盗链 签名生成
     * @param key
     * @param expr
     * @param rlimit
     * @param us
     * @return
     */
    public String keySignature(String key, long expr, Integer rlimit, String us) {
        long currentTime = System.currentTimeMillis() / 1000;
        String strSign = "";
        String contextStr = "";
        // 过期时间，固定为当前时间的下一天
        long expireTime = currentTime + 24 * 60 * 60;
        // 转为Hex字符串
        String expireHex = Long.toHexString(expireTime).toLowerCase();

        contextStr += "&t=" + expireHex;
        contextStr += "&exper=" + expr; // 试看时间
        contextStr += "&rlimit=" + rlimit; // 允许多少ip同时观看

        // 将参数拼接在一起使用md5加密
        strSign = Base64.encode(MD5.create().digest(key + expireHex + expr + rlimit + us));
        contextStr += "&sign=" + strSign;
        return contextStr;
    }

    /**
     * 生成 DRMToken
     * @param pkey
     * @param currentTimeStamp
     * @param type
     * @param appId
     * @param fileId
     * @return
     */
    public static String generateDrmToken(String pkey, long currentTimeStamp, String type, long appId, String fileId) {
        String header = "{\"alg\": \"HS256\", \"typ\": \"JWT\"}";

        Map<String, Object> payload = new HashMap<>();
        payload.put("type", type);
        payload.put("appId", appId);
        payload.put("fileId", fileId);
        payload.put("currentTimeStamp", currentTimeStamp);
        payload.put("issuer", "client");

        String encodedHeader = base64UrlEncode(header.getBytes(StandardCharsets.UTF_8));
        String encodedPayload = base64UrlEncode(payload.toString().getBytes(StandardCharsets.UTF_8));

        String signature = calculateSignature(encodedHeader + "." + encodedPayload, pkey);

        return encodedHeader + "~" + encodedPayload + "~" + signature;
    }

    private static String base64UrlEncode(byte[] input) {
        return java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(input);
    }

    private static String calculateSignature(String data, String key) {
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            hmacSHA256.init(secretKey);
            byte[] hmacData = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64UrlEncode(hmacData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成最终的url
     * @param contentUrl
     * @param drmToken
     * @return
     */
    public static String generateFinalUrl(String contentUrl, String drmToken) {
        int index = contentUrl.lastIndexOf("/");
        String baseUrl = contentUrl.substring(0, index + 1);
        String fileName = contentUrl.substring(index + 1);

        String finalUrl = baseUrl + drmToken + "/" + fileName;

        return finalUrl;
    }

    public static void main(String[] args) {
        String pkey = "dsfsdfdsgfhebberh";
        long currentTimeStamp = System.currentTimeMillis() / 1000L;
        String type = "DrmToken";
        long appId = 1500014561; // Example value, change according to your needs
        String fileId = "387702307091793695"; // Example value, change according to your needs

        String drmToken = generateDrmToken(pkey, currentTimeStamp, type, appId, fileId);
        System.out.println("DrmToken: " + drmToken);

        String url = "https://1500014561.vod2.myqcloud.com/4395be81vodtranscq1500014561/8f8e01fb387702307091793695/adp.12.m3u8?t=9ee2bdd0&sign=46f6120466d6e931ad792bad8e4f8dff";
        System.out.println(generateFinalUrl(url, drmToken));
    }
}

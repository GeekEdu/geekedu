package com.zch.oss.adapter.dogeCloud;

import cn.hutool.core.lang.UUID;
import com.zch.common.core.utils.CollUtils;
import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import com.zch.oss.domain.vo.DogeCloudPlayVO;
import com.zch.oss.domain.vo.DogeCloudUploadVO;
import com.zch.oss.utils.JacksonUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.zch.oss.utils.DogeApiUtils.dogeAPIGet;

/**
 * 多吉云 视频云上传
 * @author Poison02
 * @date 2024/3/5
 */
@Component
public class DogeCloudMediaStorage  {

    /**
     * 获取 多吉云 上传视频的临时签名
     * @param fileName 原文件名
     * @return
     */
    public DogeCloudUploadVO getUploadSignature(String fileName) throws JSONException {
        String newFileName = generateNewFileName(fileName);
        JSONObject body = new JSONObject();
        JSONObject vodConfig = new JSONObject();
        vodConfig.put("filename", fileName); // 从客户端请求的参数中获取文件名，主要用于判断文件后缀
        vodConfig.put("vn", newFileName); // 从客户端请求的参数中获取视频名称，也可以由你自己在服务端指定
        body.put("channel", "VOD_UPLOAD");
        body.put("vodConfig", vodConfig);

        JSONObject data = dogeAPIGet("/auth/tmp_token.json", body);

        JSONObject output = data.getJSONObject("VodUploadInfo");
        output.put("credentials", data.getJSONObject("Credentials"));

        // 使用Json工具转为对象
        DogeCloudUploadVO vo = JacksonUtils.string2ObjFieldLowerCamelCase(output.toString(), DogeCloudUploadVO.class);
        vo.setS3Bucket(output.getString("s3Bucket"));
        vo.setS3Endpoint(output.getString("s3Endpoint"));
        return vo;
    }

    /**
     * 获取视频播放地址
     * @param platform 平台 必传 pch5
     * @param vcode 视频vcode 必传
     * @param ip 播放ipv4地址 如 127.0.0.1
     * @param ua 防盗链相关User-Agent
     * @param playToken 播放token 控制试看等
     * @return
     * @throws JSONException
     */
    public DogeCloudPlayVO getTruePlayUrl(String platform, String vcode, String ip, String ua, String playToken) throws JSONException {
        DogeCloudPlayVO vo = new DogeCloudPlayVO();
        String url = "/video/streams.json?platform="+ platform + "&vcode="+ vcode + "&ip="+ ip;
        if (StringUtils.isNotBlank(ua)) {
            url += ("&ua=" + ua);
        }
        if (StringUtils.isNotBlank(playToken)) {
            playToken += ("&playToken=" + playToken);
        }
        JSONObject data = dogeAPIGet(url);
        JSONArray streams = data.getJSONArray("stream");
        List<String> playUrl = new ArrayList<>();
        List<String> size = new ArrayList<>();
        List<String> name = new ArrayList<>();
        List<String> backupUrl = new ArrayList<>();
        for (int i = 0 ; i < streams.length(); i++) {
            playUrl.add(streams.getJSONObject(i).get("url").toString());
            size.add(streams.getJSONObject(i).get("size").toString());
            name.add(streams.getJSONObject(i).get("name").toString());
            // 处理备用地址稍显麻烦
            int length = streams.getJSONObject(i).get("backup_urls").toString().length();
            backupUrl.add(streams.getJSONObject(i).get("backup_urls").toString().substring(1, length - 1));
        }
        vo.setPlayUrl(playUrl);
        vo.setPlayUrl(size);
        vo.setPlayUrl(name);
        vo.setPlayUrl(backupUrl);
        return vo;
    }

    /**
     * 批量删除视频
     * @param ids 远端的视频id
     * @return
     * @throws JSONException
     */
    public Integer deleteVideoBatch(List<Integer> ids) throws JSONException {
        if (ObjectUtils.isNull(ids) || CollUtils.isEmpty(ids)) {
            return 0;
        }
        String str = ids.stream().map(Object::toString)
                .collect(Collectors.joining(","))
                .toString();
        JSONObject data = dogeAPIGet("/console/video/delete.json?vids=" + str);
        String code = data.getString("code");
        return Integer.valueOf(code);
    }

    /**
     * 生成新文件名
     * @param originalFileName 原始文件名
     * @return
     */
    private static String generateNewFileName(String originalFileName) {
        // 1. 获取后缀
        String suffix = StringUtils.subAfter(originalFileName, ".", true);
        // 拼接生成新文件名
        return UUID.randomUUID().toString(true) + "." + suffix;
    }

    public static void main(String[] args) throws JSONException {
        JSONObject data = dogeAPIGet("/video/streams.json?platform=pch5&vcode=196ff7a3804b78e5&ip=127.0.0.1");
        JSONArray streams = data.getJSONArray("stream");
        for (int i = 0 ; i < streams.length(); i++) {
            int size = streams.getJSONObject(i).get("backup_urls").toString().length();
            System.out.println(streams.getJSONObject(i).get("backup_urls").toString().substring(1, size - 1));
        }

    }

}

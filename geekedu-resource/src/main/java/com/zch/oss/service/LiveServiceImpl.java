package com.zch.oss.service;

import com.zch.common.core.utils.ObjectUtils;
import com.zch.common.core.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Poison02
 * @date 2024/3/8
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LiveServiceImpl implements ILiveService{

    /**
     * 推流防盗链key
     */
    private static final String PUSH_KEY = "085b84ec337b9898731d97298b49cb56";
    /**
     * 播放防盗链key
     */
    private static final String PLAY_KEY = "kHyczxmG4XwWGFWbW2zS";
    /**
     * 固定 APPNAME
     */
    private static final String APP_NAME = "live";
    /**
     * 固定协议
     */
    private static final String PROTOCOL = "webrtc://";
    /**
     * 推流域名
     */
    private static final String PUSH_DOMAIN = "195984.push.tlivecloud.com";
    /**
     * 播放域名
     */
    private static final String PLAY_DOMAIN = "www.geekedu.fun";

    @Override
    public String generatePushUrl(String streamName, long txTime) {
        if (StringUtils.isBlank(streamName) || ObjectUtils.isNull(txTime)) {
            return "";
        }
        // 生成鉴权key
        String authenticationKey = getSafeUrl(PUSH_KEY, streamName, txTime);
        // 拼接推流url
        String playUrl = PROTOCOL + PUSH_DOMAIN + "/" + APP_NAME + "/" + streamName + "?" + authenticationKey;
        if (StringUtils.isBlank(playUrl)) {
            return "";
        }
        return playUrl;
    }

    @Override
    public String generatePlayUrl(String streamName, long txTime) {
        if (StringUtils.isBlank(streamName) || ObjectUtils.isNull(txTime)) {
            return "";
        }
        // 生成鉴权key
        String authenticationKey = getSafeUrl(PLAY_KEY, streamName, txTime);
        // 拼接播放url
        String playUrl = PROTOCOL + PLAY_DOMAIN + "/" + APP_NAME + "/" + streamName + "?" + authenticationKey;
        if (StringUtils.isBlank(playUrl)) {
            return "";
        }
        return playUrl;
    }


    public static void main(String[] args) {
        System.out.println(getSafeUrl("085b84ec337b9898731d97298b49cb56", "11212122", 1469762325L));
    }

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /*
     * KEY+ streamName + txTime
     */
    private static String getSafeUrl(String key, String streamName, long txTime) {
        String input = new StringBuilder().
                append(key).
                append(streamName).
                append(Long.toHexString(txTime).toUpperCase()).toString();

        String txSecret = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            txSecret  = byteArrayToHexString(
                    messageDigest.digest(input.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return txSecret == null ? "" :
                new StringBuilder().
                        append("txSecret=").
                        append(txSecret).
                        append("&").
                        append("txTime=").
                        append(Long.toHexString(txTime).toUpperCase()).
                        toString();
    }

    private static String byteArrayToHexString(byte[] data) {
        char[] out = new char[data.length << 1];

        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }
}

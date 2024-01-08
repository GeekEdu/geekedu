package com.zch.common.utils.commons;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.zch.common.exceptions.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;

/**
 * 通用 ip 地址工具类 使用 ip2resion
 * @author Poison02
 * @date 2024/1/8
 */
@Slf4j
public class CommonIpAddressUtils {

    private static final String LOCAL_REMOTE_HOST = "0:0:0:0:0:0:0:1";

    private static final Searcher searcher;

    static {
        String fileName = "/ip2region.xdb";
        File existFile = FileUtil.file(FileUtil.getTmpDir() + FileUtil.FILE_SEPARATOR + fileName);
        if (!FileUtil.exist(existFile)) {
            InputStream resourceAsStream = CommonIpAddressUtils.class.getResourceAsStream(fileName);
            FileUtil.writeFromStream(resourceAsStream, existFile);
        }

        String dbPath = existFile.getPath();

        // 1. 从dbpath中加载整个xdb到内存
        byte[] cBuff;
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
        } catch (Exception e) {
            log.error(">>> CommonIpAddressUtil初始化异常", e);
            throw new CommonException("CommonIpAddressUtil初始化异常");
        }

        // 2. 使用上面的cBuff创建一个完全基于内存的查询对象
        try {
            searcher = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            log.error(">>> CommonIpAddressUtil初始化异常", e);
            throw new CommonException("CommonIpAddressUtil初始化异常");
        }
    }

    /**
     * 获取客户端 ip
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        if (ObjectUtil.isEmpty(request)) {
            return Ipv4Util.LOCAL_IP;
        } else {
            try {
                String remoteHost = ServletUtil.getClientIP(request);
                return LOCAL_REMOTE_HOST.equals(remoteHost) ? Ipv4Util.LOCAL_IP : remoteHost;
            } catch (Exception e) {
                log.error(">>> 获取客户端IP异常！！！", e);
                return Ipv4Util.LOCAL_IP;
            }
        }
    }

    /**
     * 获取对应 ip 的城市信息
     * @param ip
     * @return
     */
    public static String getCityInfo(String ip) {
        try {
            ip = ip.trim();
            String region = searcher.searchByStr(ip);
            return region.replace("0|", "").replace("|0", "");
        } catch (Exception e) {
            return "未知地区";
        }
    }

}

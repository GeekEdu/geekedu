package com.zch.api.utils;

import com.zch.common.core.utils.commons.CommonIpAddressUtils;
import com.zch.common.mvc.utils.CommonUaUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Poison02
 * @date 2024/2/22
 */
public class AddressUtils {

    public static Map<String, String> getAddress(HttpServletRequest request) {
        Map<String, String> res = new HashMap<>(4);
        // ip
        String ip = CommonIpAddressUtils.getIp(request);
        // 地区
        String province = CommonIpAddressUtils.getCityInfo(ip);
        // 浏览器
        String browser = CommonUaUtil.getBrowser(request);
        // 操作系统
        String os = CommonUaUtil.getOs(request);
        res.put("ip", ip);
        res.put("province", province);
        res.put("browser", browser);
        res.put("os", os);
        return res;
    }

}

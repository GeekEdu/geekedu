package com.zch.common.core.listener;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.zch.common.core.constants.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Date;

/**
 * 启动时日志打印
 *
 * @author Poison02
 * @date 2024/1/8
 */
@Slf4j
public class AppStartupListener implements ApplicationRunner {

    @Resource
    private ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String appName = ctx.getEnvironment().getProperty("spring.application.name");
        String appJvmName = ManagementFactory.getRuntimeMXBean().getName();
        String appHost = InetAddress.getLocalHost().getHostAddress();
        String appPort = ctx.getEnvironment().getProperty("server.port");
        String appPath = ctx.getEnvironment().getProperty("server.servlet.context-path");
        String appStartupDate = DateUtil.format(new Date(ctx.getStartupDate()), DatePattern.NORM_DATETIME_MS_PATTERN);
        log.debug(AppConstants.APP_START_INFO, appName, appJvmName.split("@")[0], appStartupDate, appHost, appPort, (appPath == null ? "" : appPath));
    }

}

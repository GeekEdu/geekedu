package com.zch.common.constants;

/**
 * 启动时日志打印常量
 * @author Poison02
 * @date 2024/1/8
 */
public interface AppConstants {

    String TEAM_NAME = "GeekEdu";

    String TEAM_EMAIL = "2069820192@qq.com";

    String APP_START_INFO =
            "\n" +
                    "==============================================================\n" +
                    "\tApp:\t{}\n" +
                    "\tState:\tapp is running\n" +
                    "\tPID:\t{}\n" +
                    "\tDate:\tstarted at {}\n" +
                    "\tAuth:\t" + TEAM_NAME + "\n" +
                    "\tEmail:\t" + TEAM_EMAIL + "\n" +
                    "\tGitHub:\thttps://github.com/Poison02\n" +
                    "\tAppInfo: https://{}:{}/{}\n" +
                    "==============================================================";

}

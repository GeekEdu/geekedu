package com.zch.oss.service;

/**
 * @author Poison02
 * @date 2024/3/8
 */
public interface ILiveService {

    /**
     * 生成推流地址
     * @param streamName 唯一 使用userId
     * @param txTime 过期时间戳 一般使用当前时间的一天后
     * @return
     */
    String generatePushUrl(String streamName, long txTime);

    /**
     * 生成播放地址 参数和上面一样
     * @param streamName
     * @param txTime
     * @return
     */
    String generatePlayUrl(String streamName, long txTime);

}

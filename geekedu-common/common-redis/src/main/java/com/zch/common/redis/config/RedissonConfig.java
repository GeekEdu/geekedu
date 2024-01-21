package com.zch.common.redis.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.CompositeCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/11
 */
@Slf4j
@ConditionalOnClass({RedissonClient.class, Redisson.class})
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

    private static final String REDIS_PROTOCOL_PREFIX = "redis://";
    private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

    @Resource
    private ObjectMapper objectMapper;

    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(RedisProperties properties){
        log.debug("尝试初始化RedissonClient");
        // 读取Redis配置
        RedisProperties.Cluster cluster = properties.getCluster();
        RedisProperties.Sentinel sentinel = properties.getSentinel();
        String password = properties.getPassword();
        int timeout = 3000;
        Duration d = properties.getTimeout();
        if(d != null){
            timeout = Long.valueOf(d.toMillis()).intValue();
        }
        // 设置Redisson配置
        Config config = new Config();
        if(cluster != null && !CollectionUtil.isEmpty(cluster.getNodes())){
            // 集群模式
            config.useClusterServers()
                    .addNodeAddress(convert(cluster.getNodes()))
                    .setConnectTimeout(timeout)
                    .setPassword(password);
        }else if(sentinel != null && !StrUtil.isEmpty(sentinel.getMaster())){
            // 哨兵模式
            config.useSentinelServers()
                    .setMasterName(sentinel.getMaster())
                    .addSentinelAddress(convert(sentinel.getNodes()))
                    .setConnectTimeout(timeout)
                    .setDatabase(0)
                    .setPassword(password);
        }else{
            // 单机模式
            config.useSingleServer()
                    .setAddress(String.format("redis://%s:%d", properties.getHost(), properties.getPort()))
                    .setConnectTimeout(timeout)
                    .setDatabase(0)
                    .setPassword(password);
        }
        // 配置Redis序列化
        ObjectMapper om = objectMapper.copy();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，序列化时将对象全类名一起保存下来
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        TypedJsonJacksonCodec jsonJacksonCodec = new TypedJsonJacksonCodec(Object.class, om);
        // 组合序列化 key 使用 String 内容使用通用json格式
        CompositeCodec codec = new CompositeCodec(StringCodec.INSTANCE, jsonJacksonCodec, jsonJacksonCodec);
        // 缓存 lua 脚本
        config.setUseScriptCache(true).setCodec(codec);
        // 创建Redisson客户端
        return Redisson.create(config);
    }

    private String[] convert(List<String> nodesObject) {
        List<String> nodes = new ArrayList<>(nodesObject.size());
        for (String node : nodesObject) {
            if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
                nodes.add(REDIS_PROTOCOL_PREFIX + node);
            } else {
                nodes.add(node);
            }
        }
        return nodes.toArray(new String[0]);
    }

}

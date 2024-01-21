package com.zch.common.satoken.dao;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.util.SaFoxUtil;
import com.zch.common.redis.utils.RedisUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Poison02
 * @date 2024/1/21
 */
public class MySaTokenDao implements SaTokenDao {
    @Override
    public String get(String key) {
        return RedisUtils.getCacheObject(key);
    }

    @Override
    public void set(String key, String value, long duration) {
        if (duration == 0 || duration <= NOT_VALUE_EXPIRE) {
            return;
        }
        if (duration == NEVER_EXPIRE) {
            RedisUtils.setCacheObject(key, value);
        } else {
            RedisUtils.setCacheObject(key, value, Duration.ofSeconds(duration));
        }
    }

    @Override
    public void update(String key, String value) {
        if (RedisUtils.hasKey(key)) {
            RedisUtils.setCacheObject(key, value, true);
        }
    }

    @Override
    public void delete(String key) {
        RedisUtils.deleteObject(key);
    }

    /**
     * 查看剩余存活时间 单位 秒
     * @param key
     * @return
     */
    @Override
    public long getTimeout(String key) {
        long timeout = RedisUtils.getTimeToLive(key);
        return timeout < 0 ? timeout : timeout / 1000;
    }

    /**
     * 修改 value 过期时间
     * @param key
     * @param duration
     */
    @Override
    public void updateTimeout(String key, long duration) {
        RedisUtils.expire(key, Duration.ofSeconds(duration));
    }

    @Override
    public Object getObject(String key) {
        return RedisUtils.getCacheObject(key);
    }

    @Override
    public void setObject(String key, Object object, long duration) {
        if (duration == 0 || duration <= NOT_VALUE_EXPIRE) {
            return;
        }
        if (duration == NEVER_EXPIRE) {
            RedisUtils.setCacheObject(key, object);
        } else {
            RedisUtils.setCacheObject(key, object, Duration.ofSeconds(duration));
        }
    }

    @Override
    public void updateObject(String key, Object object) {
        if (RedisUtils.hasKey(key)) {
            RedisUtils.setCacheObject(key, object, true);
        }
    }

    @Override
    public void deleteObject(String key) {
        RedisUtils.deleteObject(key);
    }

    @Override
    public long getObjectTimeout(String key) {
        long timeout = RedisUtils.getTimeToLive(key);
        return timeout < 0 ? timeout : timeout / 1000;
    }

    @Override
    public void updateObjectTimeout(String key, long duration) {
        RedisUtils.expire(key, Duration.ofSeconds(duration));
    }

    /**
     * 搜索数据
     * @param prefix
     * @param keyword
     * @param start
     * @param size
     * @param sortType
     * @return
     */
    @Override
    public List<String> searchData(String prefix, String keyword, int start, int size, boolean sortType) {
        Collection<String> keys = RedisUtils.keys(prefix + "*" + keyword + "*");
        List<String> list = new ArrayList<>(keys);
        return SaFoxUtil.searchList(list, start, size, sortType);
    }
}

package com.zch.common.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.zch.common.mybatis.constant.Constants.DATA_FIELD_NAME_CREATE_TIME_CAMEL;
import static com.zch.common.mybatis.constant.Constants.DATA_FIELD_NAME_UPDATE_TIME_CAMEL;

/**
 * @author Poison02
 * @date 2024/1/10
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, DATA_FIELD_NAME_CREATE_TIME_CAMEL, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, DATA_FIELD_NAME_UPDATE_TIME_CAMEL, LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, DATA_FIELD_NAME_UPDATE_TIME_CAMEL, LocalDateTime::now, LocalDateTime.class);
    }
}

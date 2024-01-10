package com.zch.common.autoConfigure.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import static com.zch.common.constants.Constant.*;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Poison02
 * @date 2024/1/10
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, DATA_FIELD_NAME_CREATE_TIME, LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, DATA_FIELD_NAME_UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, DATA_FIELD_NAME_UPDATE_TIME, LocalDateTime::now, LocalDateTime.class);
    }
}

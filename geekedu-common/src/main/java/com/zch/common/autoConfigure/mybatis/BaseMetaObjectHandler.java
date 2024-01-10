package com.zch.common.autoConfigure.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.zch.common.context.UserContext;
import com.zch.common.utils.NumberUtils;
import org.apache.ibatis.reflection.MetaObject;

import static com.zch.common.constants.Constant.DATA_FIELD_NAME_CREATEBY;
import static com.zch.common.constants.Constant.DATA_FIELD_NAME_UPDATEBY;

/**
 * @author Poison02
 * @date 2023/12/28
 */
public class BaseMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 插入数据时，插入创建人和更新人
        setCreateBy(metaObject);
        setUpdateBy(metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新数据时，更新更新人
        setUpdateBy(metaObject);
    }

    private void setCreateBy(MetaObject metaObject) {
        // Long userId = UserContext.getUser();
        // 未找到用户id默认为0
        // this.strictInsertFill(metaObject, DATA_FIELD_NAME_CREATEBY, Long.class, NumberUtils.null2Zero(userId));
        this.strictInsertFill(metaObject, DATA_FIELD_NAME_CREATEBY, Long.class, 111L);
    }

    private void setUpdateBy(MetaObject metaObject) {
        // Long userId = UserContext.getUser();
        // 未找到用户id默认为0
        // this.strictInsertFill(metaObject, DATA_FIELD_NAME_UPDATEBY, Long.class, NumberUtils.null2Zero(userId));
        this.strictInsertFill(metaObject, DATA_FIELD_NAME_UPDATEBY, Long.class, 111L);
    }
}

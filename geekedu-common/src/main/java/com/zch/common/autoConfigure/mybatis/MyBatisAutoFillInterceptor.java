package com.zch.common.autoConfigure.mybatis;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.zch.common.context.UserContext;
import com.zch.common.utils.ReflectUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.sql.SQLException;

import static com.zch.common.constants.Constant.DATA_FIELD_NAME_CREATEBY;
import static com.zch.common.constants.Constant.DATA_FIELD_NAME_UPDATEBY;

/**
 * @author Poison02
 * @date 2023/12/28
 */
public class MyBatisAutoFillInterceptor implements InnerInterceptor {

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        // 更新操作
        updateExe(ms, parameter);

        // 插入操作
        insertExe(ms, parameter);
    }

    private void insertExe(MappedStatement ms, Object parameter) {
        // 1. 判断当前操作是否是插入操作
        if (ms.getSqlCommandType().compareTo(SqlCommandType.INSERT) == 0) {
            // 2. 判断是否有 updateBy 字段，若
            if (ReflectUtils.containField(DATA_FIELD_NAME_CREATEBY, parameter.getClass())) {
                Long userId = UserContext.getUser();
                // 3. 有userId也存在并设置updateBy
                if (userId != null) {
                    // 4. 当前操作人设置到创建人字段
                    ReflectUtils.setFieldValue(parameter, DATA_FIELD_NAME_CREATEBY, userId);
                }
            }
        }
    }

    private void updateExe(MappedStatement ms, Object parameter) {
        // 1. 判断是否有 updateBy 字段，若
        if (ReflectUtils.containField(DATA_FIELD_NAME_UPDATEBY, parameter.getClass())) {
            Long userId = UserContext.getUser();
            // 2. 有userId也存在并设置updateBy
            if (userId != null) {
                // 3. 当前操作人设置到创建人字段
                ReflectUtils.setFieldValue(parameter, DATA_FIELD_NAME_UPDATEBY, userId);
            }
        }
    }
}

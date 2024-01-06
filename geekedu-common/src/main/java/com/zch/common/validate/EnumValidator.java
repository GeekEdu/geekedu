package com.zch.common.validate;

import com.zch.common.enums.BaseEnum;
import com.zch.common.utils.ArrayUtils;
import com.zch.common.validate.annotation.EnumValid;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Slf4j
public class EnumValidator implements ConstraintValidator<EnumValid, BaseEnum> {

    private int[] enums = null;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.enums = constraintAnnotation.enumeration();
        log.info("payload:{}", ArrayUtils.toString(constraintAnnotation.payload()));
    }

    @Override
    public boolean isValid(BaseEnum baseEnum, ConstraintValidatorContext constraintValidatorContext) {
        // 不做空校验
        if (baseEnum == null) {
            return true;
        }
        // 没有配置枚举值不校验
        if (ArrayUtils.isEmpty(enums)) {
            return true;
        }
        for (int e : enums) {
            if (e == baseEnum.getValue()) {
                return true;
            }
        }
        return false;
    }
}

package com.zch.common.validate.annotation;

import com.zch.common.validate.EnumValidator;
import com.zch.common.validate.EnumValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author Poison02
 * @date 2024/1/6
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Constraint(validatedBy = {EnumValidator.class, EnumValueValidator.class})
public @interface EnumValid {

    String message() default "不满足业务条件！";

    int[] enumeration() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

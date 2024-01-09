package com.zch.common.annotation;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author Poison02
 * @date 2024/1/9
 */
public class XssValidator implements ConstraintValidator<Xss, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !ReUtil.contains(HtmlUtil.RE_HTML_MARK, s);
    }
}

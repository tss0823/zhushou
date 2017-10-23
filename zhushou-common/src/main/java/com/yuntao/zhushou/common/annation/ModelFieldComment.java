package com.yuntao.zhushou.common.annation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@java.lang.annotation.Documented
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
        ElementType.TYPE})
@java.lang.annotation.Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface ModelFieldComment {

    String value() default  "";

    boolean required() default false;

    int maxLength() default 0;

    int minLength() default Integer.MAX_VALUE;

    String regex() default "";

    String defaultValue() default "";





}

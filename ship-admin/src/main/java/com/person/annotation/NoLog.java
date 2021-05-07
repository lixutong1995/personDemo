package com.person.annotation;

import java.lang.annotation.*;

/**
 * @Description 日志注解，使用后不会默认记录日志
 * @Author Xutong Li
 * @Date 2021/5/7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NoLog {
    String dataTable() default "";
}

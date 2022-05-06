package com.gnerv.plus.elasticsearch.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * elasticsearch database 注解
 * </p>
 *
 * @author Gnerv LiGen
 * @since 2019/8/28
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ElasticsearchDB {

    /**
     * 数据源名称 默认 default
     *
     * @return 数据源名称
     */
    String value() default "default";
}

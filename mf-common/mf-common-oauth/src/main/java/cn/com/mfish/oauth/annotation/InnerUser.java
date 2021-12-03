package cn.com.mfish.oauth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：qiufeng
 * @description：内部服务相互调用
 * @date ：2021/12/3 11:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InnerUser {
    boolean validateUser() default false;
}

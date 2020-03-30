package com.zust.yan.rpc.spring.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.lang.annotation.*;

/**
 * @author yan
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScans({@ComponentScan(basePackages = "com.zust.yan.rpc.spring.config"),
        @ComponentScan(basePackages = "com.zust.yan.rpc.spring.listener")})
public @interface RpcComponentScan {
}

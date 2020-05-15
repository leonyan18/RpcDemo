package com.zust.yan.rpc.spring.annotation;

import java.lang.annotation.*;

/**
 * @author yan
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcServiceConsumer {
    boolean sync() default true;
}

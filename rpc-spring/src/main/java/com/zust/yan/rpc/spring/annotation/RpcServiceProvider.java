package com.zust.yan.rpc.spring.annotation;

import java.lang.annotation.*;

/**
 * @author yan
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcServiceProvider {
}

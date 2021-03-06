package com.zust.yan.rpc.spring.config;

import com.zust.yan.rpc.app.factory.BeanProxyFactory;
import com.zust.yan.rpc.spring.annotation.RpcServiceConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author yan
 */
@Component
@Slf4j
public class RpcServiceInjectProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetCls = bean.getClass();
        Field[] targetFld = targetCls.getDeclaredFields();
        for (Field field : targetFld) {
            //找到制定目标的注解类
            if (field.isAnnotationPresent(RpcServiceConsumer.class)) {
                field.setAccessible(true);
                RpcServiceConsumer rpcServiceConsumer = field.getAnnotation(RpcServiceConsumer.class);
                try {
                    field.set(bean, BeanProxyFactory.createProxy(field.getType(), rpcServiceConsumer.sync()));
                    log.info("set field" + field.getType().getName());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }


}

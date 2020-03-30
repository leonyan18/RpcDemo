package com.zust.yan.rpc.spring.config;

import com.zust.yan.rpc.app.factory.BeanProxyFactory;
import com.zust.yan.rpc.spring.annotation.RpcServiceConsumer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @author yan
 */
@Component
public class RpcServiceInjectProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetCls = bean.getClass();
        Field[] targetFld = targetCls.getDeclaredFields();
        for (Field field : targetFld) {
            //找到制定目标的注解类
            if (field.isAnnotationPresent(RpcServiceConsumer.class)) {
                field.setAccessible(true);
                try {
                    field.set(bean, BeanProxyFactory.createProxy(field.getType()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }


}

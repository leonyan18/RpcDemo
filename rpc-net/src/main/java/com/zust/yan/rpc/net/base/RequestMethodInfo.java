package com.zust.yan.rpc.net.base;

import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author yan
 */
@Data
public class RequestMethodInfo implements Serializable {
    private String methodName;
    private String returnType;
    private String className;
    private Object[] args;
    private Class<?>[] parameterTypes;

    public RequestMethodInfo() {
    }

    public RequestMethodInfo(Method method, Object[] args) {
        this.methodName = method.getName();
        this.returnType = method.getReturnType().getName();
        this.parameterTypes = method.getParameterTypes();
        this.className = method.getDeclaringClass().getName();
        this.args = args;
    }
}

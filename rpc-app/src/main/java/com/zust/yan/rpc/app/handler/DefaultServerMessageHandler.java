package com.zust.yan.rpc.app.handler;

import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.RequestMethodInfo;
import com.zust.yan.rpc.net.base.Response;
import com.zust.yan.rpc.net.handler.ServerMessageHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yan
 */
public class DefaultServerMessageHandler extends ServerMessageHandler {
    private static Map<String,Object> objectMap=new ConcurrentHashMap<>(20);

    @Override
    public Response handlerMessage(Object msg) {
        Request request= (Request) msg;
        Response response=new Response();
        response.setRequestId(request.getRequestId());
        if(request.getData() instanceof RequestMethodInfo){
            RequestMethodInfo requestMethodInfo= (RequestMethodInfo) request.getData();
            Object object=objectMap.get(requestMethodInfo.getClassName());
            try {
                Method method=object.getClass().getMethod(requestMethodInfo.getMethodName(),requestMethodInfo.getParameterTypes());
                method.setAccessible(true);
                Object result=method.invoke(object,requestMethodInfo.getArgs());
                response.setData(result);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
    public static void addHandler(Object object){
        // 放入所有接口
        for (Type t:object.getClass().getGenericInterfaces()) {
            System.out.println(t.getTypeName());
            objectMap.put(t.getTypeName(),object);
        }
    }
}

package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.net.config.RpcUtils;
import lombok.Data;

import java.util.concurrent.atomic.LongAdder;

@Data
public class Request {
    private static LongAdder adder=new LongAdder();
    private Long requestId;
    private Object data;
    public Request(){
        adder.increment();
        requestId=adder.longValue()+ RpcUtils.getMachineCode();
    }
    public Request(Object data){
        this.data=data;
        adder.increment();
        requestId=adder.longValue()+RpcUtils.getMachineCode();
    }
}

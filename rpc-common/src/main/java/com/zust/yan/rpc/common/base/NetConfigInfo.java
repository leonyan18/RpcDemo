package com.zust.yan.rpc.common.base;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author yan
 */
@Data
@Builder
public class NetConfigInfo {
    private Integer configId;
    private String host;
    private Integer port;
    private String password;
    private Integer timeOut;
    private Integer retryTimes;
    @Builder.Default
    private LongAdder failTimes=new LongAdder();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NetConfigInfo)) {
            return false;
        }
        NetConfigInfo that = (NetConfigInfo) o;
        return getPort().equals(that.getPort()) &&
                getHost().equals(that.getHost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHost(), getPort());
    }

    public String getNetAddress() {
        return host + ":" + port;
    }
}

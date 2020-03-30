package com.zust.yan.rpc.common.base;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class NetConfigInfo {
    private int configId;
    private String host;
    private int port;
    private String password;
    private int timeOut;
    private int retryTimes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NetConfigInfo)) return false;
        NetConfigInfo that = (NetConfigInfo) o;
        return getPort() == that.getPort() &&
                getHost().equals(that.getHost());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHost(), getPort());
    }
}

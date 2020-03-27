package com.zust.yan.rpc.net;

public class Bean {
    String a;
    String b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "com.zust.yan.rpc.net.base.Bean{" +
                "a='" + a + '\'' +
                ", b='" + b + '\'' +
                '}';
    }
}

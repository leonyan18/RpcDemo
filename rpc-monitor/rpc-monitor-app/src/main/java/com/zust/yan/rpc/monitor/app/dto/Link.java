package com.zust.yan.rpc.monitor.app.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Link implements Serializable {
    String source;
    String target;

    public Link() {
    }

    public Link(String source, String target) {
        this.source = source;
        this.target = target;
    }
}
package com.zust.yan.rpc.monitor.app.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yan
 */
@Data
public class RpcPath {
    List<Node> nodes;
    List<Link> links;

    public RpcPath() {
    }

    public RpcPath(List<Node> nodes, List<Link> links) {
        this.nodes = nodes;
        this.links = links;
    }
}

package com.zust.yan.rpc.monitor.app.dto;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author yan
 */
@Data
public class RpcPath {
    List<Node> nodes;
    List<Link> links;
    List<RequestDataDTO> requestDataList;

    public RpcPath() {
    }

    public RpcPath(List<Node> nodes, List<Link> links) {
        this.nodes = nodes;
        this.links = links;
    }

    public RpcPath(List<Node> nodes, List<Link> links, List<RequestDataDTO> requestDataList) {
        this.nodes = nodes;
        this.links = links;
        if(!CollectionUtils.isEmpty(requestDataList)){
            requestDataList.forEach(RequestDataDTO::makeTime);
        }
        this.requestDataList = requestDataList;
    }
}

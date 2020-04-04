package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.monitor.app.dto.Link;
import com.zust.yan.rpc.monitor.app.dto.Node;
import com.zust.yan.rpc.monitor.app.dto.RpcPath;
import com.zust.yan.rpc.monitor.app.entity.RequestData;
import com.zust.yan.rpc.monitor.app.mapper.RequestDataMapper;
import com.zust.yan.rpc.net.base.Request;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
@Slf4j
public class CallPathServiceImpl implements CallPathService {
    @Autowired
    RequestDataMapper requestDataMapper;

    @Override
    public RpcPath getCallPath(Long requestId) {
        // 构建层级关系
        List<List<Node>> permutations = new ArrayList<>(200);
        Set<String> set = new HashSet<>();
        List<Link> links = new ArrayList<>();
        List<Node> nodes = new ArrayList<>();
        Queue<LevelNode> queue = new ArrayDeque<>(200);
        RequestData requestData=requestDataMapper.getRequestData(requestId);
        if(requestData!=null){
            // 头结点初始化
            List<Node> nodeList=new ArrayList<>(2);
            Node node=new Node(requestData);
            nodeList.add(node);
            nodes.add(node);
            permutations.add(nodeList);
            queue.add(new LevelNode(1, requestId, node.getName()));
        }
        while (!queue.isEmpty()) {
            LevelNode levelNode = queue.poll();
            if (levelNode.requestId == null) {
                continue;
            }
            List<RequestData> requestDataList = requestDataMapper.getRequestDataFromId(levelNode.requestId);
            if (CollectionUtils.isEmpty(requestDataList)) {
                continue;
            }
            // 先进先出一般情况下只会比记录队列大一
            if (levelNode.level >= permutations.size()) {
                permutations.add(new ArrayList<>());
            }
            List<Node> temp = toNodeList(requestDataList);
            // 在该层添加
            permutations.get(levelNode.level).addAll(temp);
            nodes.addAll(temp);
            boolean outFlag = false;
            for (Node node : temp) {
                if (set.contains(node.getName())) {
                    outFlag = true;
                }
                set.add(node.getName());
                queue.add(new LevelNode(levelNode.level + 1, node.getRequestId(), node.getName()));
                if (levelNode.node != null) {
                    links.add(new Link(levelNode.node, node.getName()));
                }
            }
            if (outFlag) {
                break;
            }
        }
        generateNodePos(permutations);
        return new RpcPath(nodes, links);
    }

    private static List<Node> toNodeList(List<RequestData> requestDataList) {
        List<Node> nodes = new ArrayList<>();
        for (RequestData r : requestDataList) {
            nodes.add(new Node(r));
        }
        return nodes;
    }

    private static void generateNodePos(List<List<Node>> permutations) {
        int x = 0;
        int spacing = 50;
        for (List<Node> nodeList : permutations) {
            int y = (nodeList.size() / 2) * -1 * spacing;
            for (Node node : nodeList) {
                node.setX(x);
                node.setY(y);
                y += spacing;
            }
            x += spacing;
        }
    }

    static class LevelNode {
        Integer level;
        Long requestId;
        String node;

        public LevelNode(Integer level, Long requestId, String node) {
            this.level = level;
            this.requestId = requestId;
            this.node = node;
        }
    }
}

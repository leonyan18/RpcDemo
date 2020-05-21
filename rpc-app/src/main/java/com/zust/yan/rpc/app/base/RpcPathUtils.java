package com.zust.yan.rpc.app.base;

import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

@Slf4j
public class RpcPathUtils {
    private static ThreadLocal<Stack<Long>> requestStack = new ThreadLocal<>();
    private static ThreadLocal<Long> firstRequest = new ThreadLocal<>();

    public static Long beforeHandle(Long requestId) {
        Stack<Long> stack = requestStack.get();
        if (stack == null) {
            stack = new Stack<>();
            requestStack.set(stack);
        }
        Long last = null;
        if (!stack.empty()) {
            last = stack.peek();
        }
        stack.add(requestId);
//        log.info("beforeHandle stack =" + stack);
//        log.info("beforeHandle last =" + last);
//        log.info("beforeHandle stack.size =" + stack.size());
        return last;
    }

    public static void afterHandle() {
        Stack<Long> stack = requestStack.get();
        if (stack != null && !stack.empty()) {
            stack.pop();
//            log.info("beforeHandle stack =" + stack);
//            log.info("afterHandle stack.size =" + stack.size());
        }
    }
}

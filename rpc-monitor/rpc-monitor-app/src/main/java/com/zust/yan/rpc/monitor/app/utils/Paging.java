package com.zust.yan.rpc.monitor.app.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author yan
 */
public class Paging<T> implements Serializable {
    Long count;
    List<T> data;

    public Paging() {
    }

    public Paging(long count, List<T> data) {
        this.count = count;
        this.data = data;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static Paging emptyPaging() {
        return new Paging(0, Collections.emptyList());
    }

    @Override
    public String toString() {
        return "Paging{" +
                "count=" + count +
                ", data=" + data +
                '}';
    }
}

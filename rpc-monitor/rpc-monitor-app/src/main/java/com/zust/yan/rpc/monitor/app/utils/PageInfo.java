package com.zust.yan.rpc.monitor.app.utils;


import java.io.Serializable;
import java.util.Objects;

/**
 * @author yan
 */
public class PageInfo implements Serializable {
    private Integer pageSize;
    private Integer pageNo;

    public PageInfo(Integer pageSize, Integer pageNo) {
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        if (pageNo == null || pageNo <= 0) {
            pageNo = 1;
        }
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            pageSize = 10;
        }
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo <= 0) {
            pageNo = 1;
        }
        this.pageNo = pageNo;
    }

    public int getIndex() {
        return (pageNo - 1) * pageSize;
    }

    @Override
    public String toString() {
        return "Paging{" +
                "pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PageInfo)) {
            return false;
        }
        PageInfo paging = (PageInfo) o;
        return getPageSize() == paging.getPageSize() &&
                getPageNo() == paging.getPageNo();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPageSize(), getPageNo());
    }
}

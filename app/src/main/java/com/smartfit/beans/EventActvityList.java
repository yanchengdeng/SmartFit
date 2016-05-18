package com.smartfit.beans;

import java.util.List;

/**
 * 作者： 邓言诚 创建于： 2016/5/18 17;//10.
 */
public class EventActvityList {

    private String totalCount;//7,
    private String pageNo;//1,
    private String pageSize;//1000,
    private List<EventActivity> listData;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public List<EventActivity> getListData() {
        return listData;
    }

    public void setListData(List<EventActivity> listData) {
        this.listData = listData;
    }
}

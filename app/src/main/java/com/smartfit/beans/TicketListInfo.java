package com.smartfit.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public class TicketListInfo {

    private String totalCount;//0,
    private String pageNo;//1,
    private String pageSize;//10,
    private List<TicketInfo> listData;

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

    public List<TicketInfo> getListData() {
        return listData;
    }

    public void setListData(List<TicketInfo> listData) {
        this.listData = listData;
    }
}

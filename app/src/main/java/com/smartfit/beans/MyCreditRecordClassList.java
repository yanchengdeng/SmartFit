package com.smartfit.beans;

import java.util.List;

/**
 * Created by dengyancheng on 16/4/15.
 */
public class MyCreditRecordClassList {

    private String totalCount;//3,
    private String pageNo;//1,
    private String pageSize;//100,
    private List<MyCreditRecord> listData;

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

    public List<MyCreditRecord> getListData() {
        return listData;
    }

    public void setListData(List<MyCreditRecord> listData) {
        this.listData = listData;
    }
}

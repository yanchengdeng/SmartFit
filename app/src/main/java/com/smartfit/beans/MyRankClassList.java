package com.smartfit.beans;

import java.util.List;

/**
 * Created by dengyancheng on 16/4/15.
 */
public class MyRankClassList {

    private String totalCount;//3,
    private String pageNo;//1,
    private String pageSize;//100,
    private List<MyRankClass> listData;

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

    public List<MyRankClass> getListData() {
        return listData;
    }

    public void setListData(List<MyRankClass> listData) {
        this.listData = listData;
    }
}

package com.smartfit.beans;

import java.util.List;

/**
 * Created by dengyancheng on 16/4/15.
 */
public class MyAddClassList {

    private String totalCount;//3,
    private String pageNo;//1,
    private String pageSize;//100,
    private List<MyAddClass> listData;

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

    public List<MyAddClass> getListData() {
        return listData;
    }

    public void setListData(List<MyAddClass> listData) {
        this.listData = listData;
    }
}

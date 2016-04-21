package com.smartfit.beans;

import java.util.List;

/**
 * Created by dengyancheng on 16/4/21.
 */
public class ClassCommendList {

    private String totalCount;//1,
    private String pageNo;//1,
    private String pageSize;//20,
    private List<ClassCommend> listData;

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

    public List<ClassCommend> getListData() {
        return listData;
    }

    public void setListData(List<ClassCommend> listData) {
        this.listData = listData;
    }
}

package com.smartfit.beans;

import java.util.List;

/**
 * 作者：dengyancheng on 16/4/24 15;//04
 * 邮箱：yanchengdeng@gmail.com
 */
public class DynamicListData {

    private String totalCount;//2,
    private String pageNo;//1,
    private String pageSize;//20,
    private List<DynamicInfo> listData;

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

    public List<DynamicInfo> getListData() {
        return listData;
    }

    public void setListData(List<DynamicInfo> listData) {
        this.listData = listData;
    }
}

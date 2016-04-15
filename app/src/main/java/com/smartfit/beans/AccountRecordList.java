package com.smartfit.beans;

import java.util.List;

/**
 * Created by Administrator on 2016/4/15.
 */
public class AccountRecordList {
    private String totalCount;//2,
    private String pageNo;//1,
    private String pageSize;//100,
    private List<AccountRecord> listData;

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

    public List<AccountRecord> getListData() {
        return listData;
    }

    public void setListData(List<AccountRecord> listData) {
        this.listData = listData;
    }
}

package com.smartfit.beans;

import java.util.List;

/**
 *
 * 搜索课程信息
 * 作者：dengyancheng on 16/5/2 12:23
 * 邮箱：yanchengdeng@gmail.com
 */
public class SearchClassAllInfo {

  private String  totalCount;//:53,
    private String         pageNo;//:1,
    private String          pageSize;//:20,
    private List<ClassInfo> listData;//

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

    public List<ClassInfo> getListData() {
        return listData;
    }

    public void setListData(List<ClassInfo> listData) {
        this.listData = listData;
    }
}

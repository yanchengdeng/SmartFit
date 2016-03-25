package com.smartfit.beans;

/**
 * Created by yanchengdeng on 2016/3/25.
 * 赛选分类
 */
public class SelectedSort {
//排序顺序（0：按时间 1：按教练 2：按剩余名额


    public SelectedSort(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

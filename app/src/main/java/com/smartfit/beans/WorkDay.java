package com.smartfit.beans;

/**
 * Created by yanchengdeng on 2016/4/12.
 *
 * 工作日
 */
public class WorkDay {
    private String id;//1-->周天

    private String name;//周一 、周二

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    private boolean isChecked;

    public WorkDay(String id, String name) {
        this.id = id;
        this.name = name;
    }

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

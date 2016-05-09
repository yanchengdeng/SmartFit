package com.smartfit.beans;

/**
 * 作者：dengyancheng on 16/4/24 16;//47
 * 邮箱：yanchengdeng@gmail.com
 * 二级课程
 */
public class ClassFication {

    private String id;//10,
    private String classificationName;//MFT,
    private String classificationImg;//,
    private String status;//0,
    private String lastModifyTime;//1461313392,
    private String parentId;//1

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    private boolean isCheck;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getClassificationImg() {
        return classificationImg;
    }

    public void setClassificationImg(String classificationImg) {
        this.classificationImg = classificationImg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(String lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}

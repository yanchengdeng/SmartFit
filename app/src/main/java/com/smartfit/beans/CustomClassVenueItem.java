package com.smartfit.beans;

/**
 * @author yanchengdeng
 *         create at 2016/4/25 9;//47
 */
public class CustomClassVenueItem {
    private String classroomId;//31,
    private String classroomName;//私教,
    private String classroomPrice;//49,
    private String classroomPersonCount;//6

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    private boolean isCheck;

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getClassroomPrice() {
        return classroomPrice;
    }

    public void setClassroomPrice(String classroomPrice) {
        this.classroomPrice = classroomPrice;
    }

    public String getClassroomPersonCount() {
        return classroomPersonCount;
    }

    public void setClassroomPersonCount(String classroomPersonCount) {
        this.classroomPersonCount = classroomPersonCount;
    }
}

package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/19.
 */
public class IdleClassInfo implements Serializable {

    private String classroomId;//14,
    private String classroomName;//台江－团操,
    private String classroomPrice;//99,
    private String classroomPersonCount;//3

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

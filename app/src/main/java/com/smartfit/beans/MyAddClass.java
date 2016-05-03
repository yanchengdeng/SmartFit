package com.smartfit.beans;

import java.io.Serializable;

/**
 * Created by dengyancheng on 16/4/14.
 */
public class MyAddClass implements Serializable{

    private String id;//41,
    private String courseType;//0,
    private String courseName;//团操－台江－上午 －10,
    private String courseDetail;//123123123,
    private String coursePrice;//22.22,
    private String coachId;//9,
    private String coachPhone;//18950296012,
    private String startUserId;//2,
    private String startUserName;//昵称2,
    private String startUserPicUrl;///upload/picture/2016/3/5/1457192726538/1457192726.jpg,
    private String status;//1,
    private String signCount;//null,
    private String maxPersonCount;//2,
    private String startTime;//-1373806336,
    private String endTime;//-1373806336

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    private String venueId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getCoachPhone() {
        return coachPhone;
    }

    public void setCoachPhone(String coachPhone) {
        this.coachPhone = coachPhone;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public String getStartUserPicUrl() {
        return startUserPicUrl;
    }

    public void setStartUserPicUrl(String startUserPicUrl) {
        this.startUserPicUrl = startUserPicUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignCount() {
        return signCount;
    }

    public void setSignCount(String signCount) {
        this.signCount = signCount;
    }

    public String getMaxPersonCount() {
        return maxPersonCount;
    }

    public void setMaxPersonCount(String maxPersonCount) {
        this.maxPersonCount = maxPersonCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

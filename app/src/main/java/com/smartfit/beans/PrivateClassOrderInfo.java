package com.smartfit.beans;

/**
 * Created by Administrator on 2016/4/19.
 */
public class PrivateClassOrderInfo {

    private String courseId;
    private String venueId;//12,
    private String venueName;//福州台江健身馆,
    private String venueUrl;//http;////localhost;//8089/upload/picture/2016/4/2/1459595667710/1459595667.jpg,
    private String startTime;//1461054600,
    private String endTime;//1461060000,
    private String classroomPrice;//148.5,
    private String coachPrice;//300.75,
    private String totalPrice;//449.25,
    private String longit;//123.1234,
    private String lat;//23.2345

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
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

    public String getClassroomPrice() {
        return classroomPrice;
    }

    public void setClassroomPrice(String classroomPrice) {
        this.classroomPrice = classroomPrice;
    }

    public String getCoachPrice() {
        return coachPrice;
    }

    public void setCoachPrice(String coachPrice) {
        this.coachPrice = coachPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getLongit() {
        return longit;
    }

    public void setLongit(String longit) {
        this.longit = longit;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}

package com.smartfit.beans;/**
 * 作者：dengyancheng on 16/5/7 13;//38
 * 邮箱：yanchengdeng@gmail.com
 */

/**
 * 作者：dengyancheng on 16/5/7 13;//38
 * 邮箱：yanchengdeng@gmail.com
 * <p/>
 * 有氧课程  详情
 */
public class AreaDetailInfo {

    private String id;//6,
    private String classroomName;//有氧器械区,
    private String venueId;//9,
    private String classroomType;//3,
    private String classroomPersonCount;//8,
    private String classroomPrice;//0,
    private String startTime;//34200,
    private String endTime;//73800,
    private String courseType;//0,
    private String classroomImg1;//null,
    private String classroomImg2;//null,
    private String classroomImg3;//null,
    private String discount;//0,
    private String courseTypes;//null,
    private String state;//0,
    private String cityCode;//3,
    private VenueInfo venue;
    private String[] classroomPics;
    private String isParted;
    private String isFull;//是否满额
    private String bookTotal;//排队人数
    private String bookNumber;//我的排队编号

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    private String courseStatus;

    public String getIsFull() {
        return isFull;
    }

    public void setIsFull(String isFull) {
        this.isFull = isFull;
    }

    public String getBookTotal() {
        return bookTotal;
    }

    public void setBookTotal(String bookTotal) {
        this.bookTotal = bookTotal;
    }

    public String getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(String bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    private String qrcodeUrl;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    private String orderCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getClassroomType() {
        return classroomType;
    }

    public void setClassroomType(String classroomType) {
        this.classroomType = classroomType;
    }

    public String getClassroomPersonCount() {
        return classroomPersonCount;
    }

    public void setClassroomPersonCount(String classroomPersonCount) {
        this.classroomPersonCount = classroomPersonCount;
    }

    public String getClassroomPrice() {
        return classroomPrice;
    }

    public void setClassroomPrice(String classroomPrice) {
        this.classroomPrice = classroomPrice;
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

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getClassroomImg1() {
        return classroomImg1;
    }

    public void setClassroomImg1(String classroomImg1) {
        this.classroomImg1 = classroomImg1;
    }

    public String getClassroomImg2() {
        return classroomImg2;
    }

    public void setClassroomImg2(String classroomImg2) {
        this.classroomImg2 = classroomImg2;
    }

    public String getClassroomImg3() {
        return classroomImg3;
    }

    public void setClassroomImg3(String classroomImg3) {
        this.classroomImg3 = classroomImg3;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCourseTypes() {
        return courseTypes;
    }

    public void setCourseTypes(String courseTypes) {
        this.courseTypes = courseTypes;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public VenueInfo getVenue() {
        return venue;
    }

    public void setVenue(VenueInfo venue) {
        this.venue = venue;
    }

    public String[] getClassroomPics() {
        return classroomPics;
    }

    public void setClassroomPics(String[] classroomPics) {
        this.classroomPics = classroomPics;
    }

    public String getIsParted() {
        return isParted;
    }

    public void setIsParted(String isParted) {
        this.isParted = isParted;
    }
}

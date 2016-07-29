package com.smartfit.beans;

import java.util.List;

/**
 * Created by dengyancheng on 16/3/19.
 */
public class ClassInfoDetail {
    private String uid;
    private String longit;//null,
    private String lat;//null,
    private String isParted;//0,
    private List<ClassCommend> commentList;
    private String topicId;//20160409081801885545837,
    private String courseId;//37,
    private String courseName;//团操－台江－上午,
    private String coachRealName;//徐晃11,
    private String account;//admin,
    private String venueUrl;//http;////localhost;//8089/upload/picture/2016/4/2/1459595667710/1459595667.jpg,
    private String venueId;//12,
    private String venueName;//福州台江健身馆,
    private String linkPhone;//null,
    private String serviceDetails;//null,
    private String coachId;//9,
    private String stars;//0,
    private String nickName;//\U4ee5uu,
    private String price;//22.22,
    private String userPicUrl;//http;////123.57.164.115;//8098/uploadimgs/picture/2016/4/2/1459580390538/1459580390.png,
    private String startUserId;//1,
    private String endTime;//1460218620,
    private String classroomName;//台江－团操,
    private String courseDetail;//123123123,
    private String startDate;//1460217600,
    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    private String courseStatus;//0:未开始1:正在进行2:已结束

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    private String qrcodeUrl;

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
        this.classroomId = classroomId;
    }

    private String classroomId;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    private String startTime;
    private List<CommondPersion> persionList;//[
    private String userHeadImg;//,
    private String userSex;//0,
    private String userNickName;//,
    private String[] coursePics;

    public List<CoachInfo> getCoachList() {
        return coachList;
    }

    public void setCoachList(List<CoachInfo> coachList) {
        this.coachList = coachList;
    }

    private List<CoachInfo> coachList;

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    private String mobileNo;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    private String orderCode;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private String signature;

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    private String shared ;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * 订单状态（
     * 1我报名但未付款，
     * 2已经付款教练未接单，
     * 3已经付款教练接单（即正常），
     * 4课程已经结束
     * 5我退出该课程，
     * 6该课程被取消了，
     * 7课程已结束未评论8已评论）
     */
    private String orderStatus;

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

    public String getIsParted() {
        return isParted;
    }

    public void setIsParted(String isParted) {
        this.isParted = isParted;
    }

    public List<ClassCommend> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<ClassCommend> commentList) {
        this.commentList = commentList;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoachRealName() {
        return coachRealName;
    }

    public void setCoachRealName(String coachRealName) {
        this.coachRealName = coachRealName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
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

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(String serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClassroomName() {
        return classroomName;
    }

    public void setClassroomName(String classroomName) {
        this.classroomName = classroomName;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public List<CommondPersion> getPersionList() {
        return persionList;
    }

    public void setPersionList(List<CommondPersion> persionList) {
        this.persionList = persionList;
    }

    public String getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(String userHeadImg) {
        this.userHeadImg = userHeadImg;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String[] getCoursePics() {
        return coursePics;
    }

    public void setCoursePics(String[] coursePics) {
        this.coursePics = coursePics;
    }
}

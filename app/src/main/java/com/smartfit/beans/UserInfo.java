package com.smartfit.beans;

import java.util.ArrayList;

/**
 * Created by yanchengdneng on 2016/4/1.
 * <p/>
 * 用户信息    更具uid 获取
 */
public class UserInfo {

    private String coachRealName;
    private String uid;//7,
    private String nickName;//吉祥三宝想,
    private String signature;//null,
    private String userPicUrl;//null,
    private String sex;//0,
    private String isVip;//0,
    private String focusCount;//0,
    private String fansCount;//0,
    private String friendCount;//0,
    private String balance;//0,
    private ArrayList<CommentInfo> commonList;//null,
    private String coachInfo;//null,
    private String courseCount;//null,
    private String[] coachDynamicPics;//

    public String getCoachRealName() {
        return coachRealName;
    }

    public void setCoachRealName(String coachRealName) {
        this.coachRealName = coachRealName;
    }

    public String getCurClassCount() {
        return curClassCount;
    }

    public void setCurClassCount(String curClassCount) {
        this.curClassCount = curClassCount;
    }

    private String curClassCount;

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    private String stars;

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    private String   coachId;

   private String  certificates;//陌路,陌路,
    private String        coachClassDesc;//null,
    private String       authenCoachClassDesc;//null,

    public String getResumeContent() {
        return resumeContent;
    }

    public void setResumeContent(String resumeContent) {
        this.resumeContent = resumeContent;
    }

    private String resumeContent;

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getCoachClassDesc() {
        return coachClassDesc;
    }

    public void setCoachClassDesc(String coachClassDesc) {
        this.coachClassDesc = coachClassDesc;
    }

    public String getAuthenCoachClassDesc() {
        return authenCoachClassDesc;
    }

    public void setAuthenCoachClassDesc(String authenCoachClassDesc) {
        this.authenCoachClassDesc = authenCoachClassDesc;
    }

    private String   hight;//175,
           private String  weight;//75,

    public String getHight() {
        return hight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    private String isFoused;//;//0,//0 未   1  已关注
    private String beFoused;//;//0,
    private String isFriend;//0,

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String age;

    public String getIsFoused() {
        return isFoused;
    }

    public void setIsFoused(String isFoused) {
        this.isFoused = isFoused;
    }

    public String getBeFoused() {
        return beFoused;
    }

    public void setBeFoused(String beFoused) {
        this.beFoused = beFoused;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getFocusCount() {
        return focusCount;
    }

    public void setFocusCount(String focusCount) {
        this.focusCount = focusCount;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }

    public String getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public  ArrayList<CommentInfo> getCommonList() {
        return commonList;
    }

    public void setCommonList( ArrayList<CommentInfo> commonList) {
        this.commonList = commonList;
    }

    public String getCoachInfo() {
        return coachInfo;
    }

    public void setCoachInfo(String coachInfo) {
        this.coachInfo = coachInfo;
    }

    public String getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(String curClassCount) {
        this.courseCount = curClassCount;
    }

    public String[] getCoachDynamicPics() {
        return coachDynamicPics;
    }

    public void setCoachDynamicPics(String[] coachDynamicPics) {
        this.coachDynamicPics = coachDynamicPics;
    }
}

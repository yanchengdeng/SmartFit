package com.smartfit.beans;

/**
 * Created by yanchengdneng on 2016/4/1.
 * <p/>
 * 用户信息    更具uid 获取
 */
public class UserInfo {

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
    private String commonList;//null,
    private String coachInfo;//null,
    private String curClassCount;//null,
    private String[] coachDynamicPics;//

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

    public String getCommonList() {
        return commonList;
    }

    public void setCommonList(String commonList) {
        this.commonList = commonList;
    }

    public String getCoachInfo() {
        return coachInfo;
    }

    public void setCoachInfo(String coachInfo) {
        this.coachInfo = coachInfo;
    }

    public String getCurClassCount() {
        return curClassCount;
    }

    public void setCurClassCount(String curClassCount) {
        this.curClassCount = curClassCount;
    }

    public String[] getCoachDynamicPics() {
        return coachDynamicPics;
    }

    public void setCoachDynamicPics(String[] coachDynamicPics) {
        this.coachDynamicPics = coachDynamicPics;
    }
}
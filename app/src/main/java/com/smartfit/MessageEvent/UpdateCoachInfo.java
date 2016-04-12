package com.smartfit.MessageEvent;

/**
 * Created by yancheng on 2016/4/12.
 * 教练信息
 */
public class UpdateCoachInfo {

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private String signature;
    private String nickName;//yancheng11,
    private String userPicUrl;//http;////123.57.164.115;//8098/uploadimgs/picture/2016/4/12/1460444147277/1460444147.png,
    private String coachRealName;//null,
    private String sex;//0,

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getCoachRealName() {
        return coachRealName;
    }

    public void setCoachRealName(String coachRealName) {
        this.coachRealName = coachRealName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

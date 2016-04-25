package com.smartfit.beans;

/**
 * Created by yancheng on 2016/4/1.
 * 用户详情
 */
public class UserInfoDetail {

    private String uid;//7,
    private String account;//吉祥三宝想,
    private String nickName;//吉祥三宝想,
    private String mobile;//18650328280,
    private String sex;//0,
    private String userPicUrl;//null,
    private String sid;//null,
    private String signature;//null,
    private String isICF;//0,教练状态0：未申请1上线,2下线,3锁定.4审核中
    private String isCTC;//0,是否设置课程类型：0未设置；大于0为设置
    private String isCCC;//0,是否认证过简历：0未设置；1待审2审核通过3；审核不通过
    private String hight;//null,
    private String weight;//null,
    private String balance;//0

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    private String coachId;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getIsICF() {
        return isICF;
    }

    public void setIsICF(String isICF) {
        this.isICF = isICF;
    }

    public String getIsCTC() {
        return isCTC;
    }

    public void setIsCTC(String isCTC) {
        this.isCTC = isCTC;
    }

    public String getIsCCC() {
        return isCCC;
    }

    public void setIsCCC(String isCCC) {
        this.isCCC = isCCC;
    }

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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}

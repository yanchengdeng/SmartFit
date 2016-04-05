package com.smartfit.beans;

/**
 * Created by yanchnegdeng on 2016/4/5.
 * <p/>
 * 关注列表
 */
public class AttentionBean {

  private String  uid;//23,
    private String    sex;//1,
    private String   nickName;//Gordon,
    private String  signature;//null,
    private String  userPicUrl;//null

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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
}

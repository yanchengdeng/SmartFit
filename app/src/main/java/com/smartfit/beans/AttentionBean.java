package com.smartfit.beans;

/**
 * Created by yanchnegdeng on 2016/4/5.
 * <p/>
 * 关注列表
 */
public class AttentionBean {

    private String NickName;

    private String UserPicUrl;

    private String Signature;

    private String Uid;

    private String Sex;

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getUserPicUrl() {
        return UserPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        UserPicUrl = userPicUrl;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }
}

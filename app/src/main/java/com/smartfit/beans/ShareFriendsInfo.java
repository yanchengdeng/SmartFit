package com.smartfit.beans;/**
 * 作者：dengyancheng on 16/7/7 00;//01
 * 邮箱：yanchengdeng@gmail.com
 */

/**
 *
 * 作者：dengyancheng on 16/7/7 00;//01
 * 邮箱：yanchengdeng@gmail.com
 */
public class ShareFriendsInfo {
    private boolean isCheck;
   private String  uid;//1,
    private String      sex;//0,
    private String     nickName;//Gordon Huang,
    private String    signature;//既然青春留不住,
    private String    userPicUrl;//

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

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

package com.smartfit.beans;

/**
 * 参加课程成员信息
 * 作者： 邓言诚 创建于： 2016/5/3 17:46.
 */
public class MemeberInfo {

    private String id;
    private String nickName;
    ;
    private String mobileNo;
    private String userPicUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }
}

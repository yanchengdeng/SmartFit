package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：dengyancheng on 16/4/24 15;//06
 * 邮箱：yanchengdeng@gmail.com
 */
public class DynamicInfo implements Parcelable {


    private String id;
    private String topicId;//2016042414574927058890,
    private String good;//0,
    private String userPicUrl;//http;////123.57.164.115;//8098/uploadimgs/picture/2016/4/13/1460539717143/1460539717.jpg,
    private String uid;//33,
    private String nickName;//粉末,
    private String commentCount;//0,
    private String content;//哭咯弄,
    private String addTime;//1461481069,
    private String imgUrl;
    private String hadGood;//1 表示已点赞

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
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

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getHadGood() {
        return hadGood;
    }

    public void setHadGood(String hadGood) {
        this.hadGood = hadGood;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.topicId);
        dest.writeString(this.good);
        dest.writeString(this.userPicUrl);
        dest.writeString(this.uid);
        dest.writeString(this.nickName);
        dest.writeString(this.commentCount);
        dest.writeString(this.content);
        dest.writeString(this.addTime);
        dest.writeString(this.imgUrl);
        dest.writeString(this.hadGood);
    }

    public DynamicInfo() {
    }

    protected DynamicInfo(Parcel in) {
        this.id = in.readString();
        this.topicId = in.readString();
        this.good = in.readString();
        this.userPicUrl = in.readString();
        this.uid = in.readString();
        this.nickName = in.readString();
        this.commentCount = in.readString();
        this.content = in.readString();
        this.addTime = in.readString();
        this.imgUrl = in.readString();
        this.hadGood = in.readString();
    }

    public static final Creator<DynamicInfo> CREATOR = new Creator<DynamicInfo>() {
        public DynamicInfo createFromParcel(Parcel source) {
            return new DynamicInfo(source);
        }

        public DynamicInfo[] newArray(int size) {
            return new DynamicInfo[size];
        }
    };
}

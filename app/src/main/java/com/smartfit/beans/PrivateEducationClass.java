package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/7.
 * 私教课程单条信息
 */
public class PrivateEducationClass implements Parcelable {


    private String coachId;//12,
    private String uid;//19,
    private String nickName;//"从家出发",
    private String userPicUrl;//"http://123.57.164.115:8098/uploadimgs/picture/2016/4/2/1459599253692/1459599253.png",
    private String price;//200.5,
    private String stars;//0,
    private String coachDesc;//null,
    private String sex;//1

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
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

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getCoachDesc() {
        return coachDesc;
    }

    public void setCoachDesc(String coachDesc) {
        this.coachDesc = coachDesc;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    private boolean isCheck;


    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coachId);
        dest.writeString(this.uid);
        dest.writeString(this.nickName);
        dest.writeString(this.userPicUrl);
        dest.writeString(this.price);
        dest.writeString(this.stars);
        dest.writeString(this.coachDesc);
        dest.writeString(this.sex);
        dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
    }

    public PrivateEducationClass() {
    }

    protected PrivateEducationClass(Parcel in) {
        this.coachId = in.readString();
        this.uid = in.readString();
        this.nickName = in.readString();
        this.userPicUrl = in.readString();
        this.price = in.readString();
        this.stars = in.readString();
        this.coachDesc = in.readString();
        this.sex = in.readString();
        this.isCheck = in.readByte() != 0;
    }

    public static final Creator<PrivateEducationClass> CREATOR = new Creator<PrivateEducationClass>() {
        public PrivateEducationClass createFromParcel(Parcel source) {
            return new PrivateEducationClass(source);
        }

        public PrivateEducationClass[] newArray(int size) {
            return new PrivateEducationClass[size];
        }
    };
}

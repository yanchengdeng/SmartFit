package com.smartfit.beans;/**
 * 作者：dengyancheng on 16/7/8 22;//28
 * 邮箱：yanchengdeng@gmail.com
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 *
 * 作者：dengyancheng on 16/7/8 22;//28
 * 邮箱：yanchengdeng@gmail.com
 */
public class SelectCoachInfo implements Parcelable {

  private String  coachId;//63,
    private String    uid;//609,
    private String     nickName;//陈儒江,
    private String     userPicUrl;//http;////139.196.228.98;//7097/uploadimgs/picture/2016/5/30/1464573988170/1464573988.png,
    private String     price;//140,
    private String      stars;//5,
    private String      sex;//0,
    private String      startTime;//32400,
    private String       endTime;//79200,
    private String       signature;//null,
    private String       phone;//null,
    private ArrayList<PrivateClassDTO> timeDTOs;
    private String minPrice;
    private String maxPrice;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<PrivateClassDTO> getTimeDTOs() {
        return timeDTOs;
    }

    public void setTimeDTOs(ArrayList<PrivateClassDTO> timeDTOs) {
        this.timeDTOs = timeDTOs;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
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
        dest.writeString(this.sex);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.signature);
        dest.writeString(this.phone);
        dest.writeTypedList(timeDTOs);
        dest.writeString(this.minPrice);
        dest.writeString(this.maxPrice);
    }

    public SelectCoachInfo() {
    }

    protected SelectCoachInfo(Parcel in) {
        this.coachId = in.readString();
        this.uid = in.readString();
        this.nickName = in.readString();
        this.userPicUrl = in.readString();
        this.price = in.readString();
        this.stars = in.readString();
        this.sex = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.signature = in.readString();
        this.phone = in.readString();
        this.timeDTOs = in.createTypedArrayList(PrivateClassDTO.CREATOR);
        this.minPrice = in.readString();
        this.maxPrice = in.readString();
    }

    public static final Parcelable.Creator<SelectCoachInfo> CREATOR = new Parcelable.Creator<SelectCoachInfo>() {
        public SelectCoachInfo createFromParcel(Parcel source) {
            return new SelectCoachInfo(source);
        }

        public SelectCoachInfo[] newArray(int size) {
            return new SelectCoachInfo[size];
        }
    };
}

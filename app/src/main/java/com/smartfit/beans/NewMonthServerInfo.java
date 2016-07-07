package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 作者： 邓言诚 创建于： 2016/7/4 21;//04.
 */
public class NewMonthServerInfo implements Parcelable {

    private String uid;//596,
    private String nickName;//yancheng,
    private String mobileNo;//13067380836,
    private String userPicUrl;//http;////139.196.228.98;//7097/uploadimgs/picture/2016/5/25/1464163192037/1464163192.jpg,
    private String monthExpiredTime;//0,
    private String defaultMonthPrice;//139,
    private List<NewMouthServerEvent> newestMonthEvents;
    private List<UseableEventInfo> eventListDTOs;

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

    public String getMonthExpiredTime() {
        return monthExpiredTime;
    }

    public void setMonthExpiredTime(String monthExpiredTime) {
        this.monthExpiredTime = monthExpiredTime;
    }

    public String getDefaultMonthPrice() {
        return defaultMonthPrice;
    }

    public void setDefaultMonthPrice(String defaultMonthPrice) {
        this.defaultMonthPrice = defaultMonthPrice;
    }

    public List<NewMouthServerEvent> getNewestMonthEvents() {
        return newestMonthEvents;
    }

    public void setNewestMonthEvents(List<NewMouthServerEvent> newestMonthEvents) {
        this.newestMonthEvents = newestMonthEvents;
    }

    public List<UseableEventInfo> getEventListDTOs() {
        return eventListDTOs;
    }

    public void setEventListDTOs(List<UseableEventInfo> eventListDTOs) {
        this.eventListDTOs = eventListDTOs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.nickName);
        dest.writeString(this.mobileNo);
        dest.writeString(this.userPicUrl);
        dest.writeString(this.monthExpiredTime);
        dest.writeString(this.defaultMonthPrice);
        dest.writeTypedList(newestMonthEvents);
        dest.writeTypedList(eventListDTOs);
    }

    public NewMonthServerInfo() {
    }

    protected NewMonthServerInfo(Parcel in) {
        this.uid = in.readString();
        this.nickName = in.readString();
        this.mobileNo = in.readString();
        this.userPicUrl = in.readString();
        this.monthExpiredTime = in.readString();
        this.defaultMonthPrice = in.readString();
        this.newestMonthEvents = in.createTypedArrayList(NewMouthServerEvent.CREATOR);
        this.eventListDTOs = in.createTypedArrayList(UseableEventInfo.CREATOR);
    }

    public static final Parcelable.Creator<NewMonthServerInfo> CREATOR = new Parcelable.Creator<NewMonthServerInfo>() {
        public NewMonthServerInfo createFromParcel(Parcel source) {
            return new NewMonthServerInfo(source);
        }

        public NewMonthServerInfo[] newArray(int size) {
            return new NewMonthServerInfo[size];
        }
    };
}

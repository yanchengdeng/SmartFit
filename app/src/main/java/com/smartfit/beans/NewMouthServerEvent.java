package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： 邓言诚 创建于： 2016/7/4 21;//06.
 *
 *
 */
public class NewMouthServerEvent implements Parcelable {
    private String id;//13,
    private String eventTitle;//新增测试言诚,
    private String eventDetial;//开始投放市场,
    private String eventStartTime;//1464796800,
    private String eventEndTime;//1530460800,
    private String eventUseType;//0,
    private String useStartTime;//0,
    private String useEndTime;//0,
    private String useMonthRang;//10,
    private String eventStatus;//0,
    private String createTime;//1467432742,
    private String adminId;//0,
    private String adminName;//0,
    private String eventType;//2,
    private String eventPrice;//100,
    private String courseTypeCode;//null,
    private String expiredTime;//0,
    private String eventRelateds;//null,
    private String eventRelatedString;//null,
    private String newest2Events;//null,
    private String pics[];

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDetial() {
        return eventDetial;
    }

    public void setEventDetial(String eventDetial) {
        this.eventDetial = eventDetial;
    }

    public String getEventStartTime() {
        return eventStartTime;
    }

    public void setEventStartTime(String eventStartTime) {
        this.eventStartTime = eventStartTime;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventUseType() {
        return eventUseType;
    }

    public void setEventUseType(String eventUseType) {
        this.eventUseType = eventUseType;
    }

    public String getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(String useStartTime) {
        this.useStartTime = useStartTime;
    }

    public String getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(String useEndTime) {
        this.useEndTime = useEndTime;
    }

    public String getUseMonthRang() {
        return useMonthRang;
    }

    public void setUseMonthRang(String useMonthRang) {
        this.useMonthRang = useMonthRang;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(String eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getCourseTypeCode() {
        return courseTypeCode;
    }

    public void setCourseTypeCode(String courseTypeCode) {
        this.courseTypeCode = courseTypeCode;
    }

    public String getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(String expiredTime) {
        this.expiredTime = expiredTime;
    }

    public String getEventRelateds() {
        return eventRelateds;
    }

    public void setEventRelateds(String eventRelateds) {
        this.eventRelateds = eventRelateds;
    }

    public String getEventRelatedString() {
        return eventRelatedString;
    }

    public void setEventRelatedString(String eventRelatedString) {
        this.eventRelatedString = eventRelatedString;
    }

    public String getNewest2Events() {
        return newest2Events;
    }

    public void setNewest2Events(String newest2Events) {
        this.newest2Events = newest2Events;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.eventTitle);
        dest.writeString(this.eventDetial);
        dest.writeString(this.eventStartTime);
        dest.writeString(this.eventEndTime);
        dest.writeString(this.eventUseType);
        dest.writeString(this.useStartTime);
        dest.writeString(this.useEndTime);
        dest.writeString(this.useMonthRang);
        dest.writeString(this.eventStatus);
        dest.writeString(this.createTime);
        dest.writeString(this.adminId);
        dest.writeString(this.adminName);
        dest.writeString(this.eventType);
        dest.writeString(this.eventPrice);
        dest.writeString(this.courseTypeCode);
        dest.writeString(this.expiredTime);
        dest.writeString(this.eventRelateds);
        dest.writeString(this.eventRelatedString);
        dest.writeString(this.newest2Events);
        dest.writeStringArray(this.pics);
    }

    public NewMouthServerEvent() {
    }

    protected NewMouthServerEvent(Parcel in) {
        this.id = in.readString();
        this.eventTitle = in.readString();
        this.eventDetial = in.readString();
        this.eventStartTime = in.readString();
        this.eventEndTime = in.readString();
        this.eventUseType = in.readString();
        this.useStartTime = in.readString();
        this.useEndTime = in.readString();
        this.useMonthRang = in.readString();
        this.eventStatus = in.readString();
        this.createTime = in.readString();
        this.adminId = in.readString();
        this.adminName = in.readString();
        this.eventType = in.readString();
        this.eventPrice = in.readString();
        this.courseTypeCode = in.readString();
        this.expiredTime = in.readString();
        this.eventRelateds = in.readString();
        this.eventRelatedString = in.readString();
        this.newest2Events = in.readString();
        this.pics = in.createStringArray();
    }

    public static final Parcelable.Creator<NewMouthServerEvent> CREATOR = new Parcelable.Creator<NewMouthServerEvent>() {
        public NewMouthServerEvent createFromParcel(Parcel source) {
            return new NewMouthServerEvent(source);
        }

        public NewMouthServerEvent[] newArray(int size) {
            return new NewMouthServerEvent[size];
        }
    };
}

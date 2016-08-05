package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： 邓言诚 创建于： 2016/7/5 10;//55.
 * 可使用卷信息
 */
public class UseableEventInfo implements Parcelable {

    private String id;//5350,
    private String eventId;//null,
    private String eventTitle;//全场通用现金券,
    private String eventDetial;//全场通用现金券,
    private String eventEndTime;//1470644220,
    private String eventType;//21,
    private String count;//null,
    private String usedTime;//1470384975,
    private String coachLevel;//null,
    private String ticketPrice;//22.1,
    private String status;//0,
    private String cashEventType;//0
    private boolean isCheck;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(String usedTime) {
        this.usedTime = usedTime;
    }

    public String getCoachLevel() {
        return coachLevel;
    }

    public void setCoachLevel(String coachLevel) {
        this.coachLevel = coachLevel;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCashEventType() {
        return cashEventType;
    }

    public void setCashEventType(String cashEventType) {
        this.cashEventType = cashEventType;
    }

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
        dest.writeString(this.id);
        dest.writeString(this.eventId);
        dest.writeString(this.eventTitle);
        dest.writeString(this.eventDetial);
        dest.writeString(this.eventEndTime);
        dest.writeString(this.eventType);
        dest.writeString(this.count);
        dest.writeString(this.usedTime);
        dest.writeString(this.coachLevel);
        dest.writeString(this.ticketPrice);
        dest.writeString(this.status);
        dest.writeString(this.cashEventType);
        dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
    }

    public UseableEventInfo() {
    }

    protected UseableEventInfo(Parcel in) {
        this.id = in.readString();
        this.eventId = in.readString();
        this.eventTitle = in.readString();
        this.eventDetial = in.readString();
        this.eventEndTime = in.readString();
        this.eventType = in.readString();
        this.count = in.readString();
        this.usedTime = in.readString();
        this.coachLevel = in.readString();
        this.ticketPrice = in.readString();
        this.status = in.readString();
        this.cashEventType = in.readString();
        this.isCheck = in.readByte() != 0;
    }

    public static final Parcelable.Creator<UseableEventInfo> CREATOR = new Parcelable.Creator<UseableEventInfo>() {
        public UseableEventInfo createFromParcel(Parcel source) {
            return new UseableEventInfo(source);
        }

        public UseableEventInfo[] newArray(int size) {
            return new UseableEventInfo[size];
        }
    };
}

package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/4/13.
 */
public class TicketInfo implements Parcelable {

    private boolean isCheck;
    private String id;//12,
    private String eventTitle;//包月卡,
    private String eventDetial;//这是一个测试月卡,
    private String eventEndTime;//1469894400,
    private String eventType;//3

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
        dest.writeString(this.eventTitle);
        dest.writeString(this.eventDetial);
        dest.writeString(this.eventEndTime);
        dest.writeString(this.eventType);
    }

    public TicketInfo() {
    }

    protected TicketInfo(Parcel in) {
        this.isCheck = in.readByte() != 0;
        this.id = in.readString();
        this.eventTitle = in.readString();
        this.eventDetial = in.readString();
        this.eventEndTime = in.readString();
        this.eventType = in.readString();
    }

    public static final Parcelable.Creator<TicketInfo> CREATOR = new Parcelable.Creator<TicketInfo>() {
        public TicketInfo createFromParcel(Parcel source) {
            return new TicketInfo(source);
        }

        public TicketInfo[] newArray(int size) {
            return new TicketInfo[size];
        }
    };
}

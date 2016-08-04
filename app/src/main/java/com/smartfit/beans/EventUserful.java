package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： 邓言诚 创建于： 2016/5/20 15;//53.
 */
public class EventUserful implements Parcelable {

    private String id;//5267,
    private String eventId;//null,
    private String eventTitle;//10元活动现金券,
    private String eventDetial;//123123123,
    private String eventEndTime;//1472631600,
    private String eventType;//21,
    private String count;//null,
    private String usedTime;//1470191509,
    private String coachLevel;//null,
    private String ticketPrice;//10,
    private String status;//0,
    private String cashEventType;//0现金券1满减券2折扣券
    private boolean isCheked;

    public boolean isCheked() {
        return isCheked;
    }

    public void setIsCheked(boolean isCheked) {
        this.isCheked = isCheked;
    }

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
        dest.writeByte(isCheked ? (byte) 1 : (byte) 0);
    }

    public EventUserful() {
    }

    protected EventUserful(Parcel in) {
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
        this.isCheked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<EventUserful> CREATOR = new Parcelable.Creator<EventUserful>() {
        public EventUserful createFromParcel(Parcel source) {
            return new EventUserful(source);
        }

        public EventUserful[] newArray(int size) {
            return new EventUserful[size];
        }
    };
}

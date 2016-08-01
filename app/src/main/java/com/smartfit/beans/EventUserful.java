package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： 邓言诚 创建于： 2016/5/20 15;//53.
 */
public class EventUserful implements Parcelable {

    private String id;// 1339,
    private String userId;// 33,
    private String eventId;// 12,
    private String startTime;// 1463582722,
    private String endTime;// 1466261122,
    private String eventType;// 3,
    private String courseTypeCode;// null,
    private String coachLevel;// null,
    private String count;// 0,
    private String eventTitle;// 包月卡,
    private String eventDetial;// 这是一个测试月卡,
    private String courseClassId;// null

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    private String ticketPrice;

    public boolean isCheked() {
        return isCheked;
    }

    public void setIsCheked(boolean isCheked) {
        this.isCheked = isCheked;
    }

    private boolean isCheked;

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    private boolean isCheck;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCourseTypeCode() {
        return courseTypeCode;
    }

    public void setCourseTypeCode(String courseTypeCode) {
        this.courseTypeCode = courseTypeCode;
    }

    public String getCoachLevel() {
        return coachLevel;
    }

    public void setCoachLevel(String coachLevel) {
        this.coachLevel = coachLevel;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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

    public String getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(String courseClassId) {
        this.courseClassId = courseClassId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.eventId);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.eventType);
        dest.writeString(this.courseTypeCode);
        dest.writeString(this.coachLevel);
        dest.writeString(this.count);
        dest.writeString(this.eventTitle);
        dest.writeString(this.eventDetial);
        dest.writeString(this.courseClassId);
        dest.writeString(this.ticketPrice);
        dest.writeByte(isCheked ? (byte) 1 : (byte) 0);
        dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
    }

    public EventUserful() {
    }

    protected EventUserful(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.eventId = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.eventType = in.readString();
        this.courseTypeCode = in.readString();
        this.coachLevel = in.readString();
        this.count = in.readString();
        this.eventTitle = in.readString();
        this.eventDetial = in.readString();
        this.courseClassId = in.readString();
        this.ticketPrice = in.readString();
        this.isCheked = in.readByte() != 0;
        this.isCheck = in.readByte() != 0;
    }

    public static final Creator<EventUserful> CREATOR = new Creator<EventUserful>() {
        public EventUserful createFromParcel(Parcel source) {
            return new EventUserful(source);
        }

        public EventUserful[] newArray(int size) {
            return new EventUserful[size];
        }
    };
}

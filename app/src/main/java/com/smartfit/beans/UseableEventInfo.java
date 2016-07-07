package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： 邓言诚 创建于： 2016/7/5 10:55.
 * 可使用卷信息
 */
public class UseableEventInfo implements Parcelable {

    private String id;
    private String eventTitle;
    private String eventDetail;
    private String eventEndTime;
    private boolean isCheck;

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

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getEventEndTime() {
        return eventEndTime;
    }

    public void setEventEndTime(String eventEndTime) {
        this.eventEndTime = eventEndTime;
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
        dest.writeString(this.eventTitle);
        dest.writeString(this.eventDetail);
        dest.writeString(this.eventEndTime);
        dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
    }

    public UseableEventInfo() {
    }

    protected UseableEventInfo(Parcel in) {
        this.id = in.readString();
        this.eventTitle = in.readString();
        this.eventDetail = in.readString();
        this.eventEndTime = in.readString();
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

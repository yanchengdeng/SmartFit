package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/3/23.
 */
public class WorkPoint implements Parcelable{

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    private String  tittle;
    private String id;//56,
    private String uid;//33,
    private String venueId;//12,
    private String venueName;//福州台江健身馆,
    private String addTime;//1460519667,
    private String startTime;//1460520000,
    private String endTime;//1460527200,
    private String workspaceCode;//1460519667799,
    private String dayOfWeek;//1,
    private String[] daysOfWeek;//
    private String daysOfWeekString;//null

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
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

    public String getWorkspaceCode() {
        return workspaceCode;
    }

    public void setWorkspaceCode(String workspaceCode) {
        this.workspaceCode = workspaceCode;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getDaysOfWeekString() {
        return daysOfWeekString;
    }

    public void setDaysOfWeekString(String daysOfWeekString) {
        this.daysOfWeekString = daysOfWeekString;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tittle);
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.venueId);
        dest.writeString(this.venueName);
        dest.writeString(this.addTime);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.workspaceCode);
        dest.writeString(this.dayOfWeek);
        dest.writeStringArray(this.daysOfWeek);
        dest.writeString(this.daysOfWeekString);
    }

    public WorkPoint() {
    }

    protected WorkPoint(Parcel in) {
        this.tittle = in.readString();
        this.id = in.readString();
        this.uid = in.readString();
        this.venueId = in.readString();
        this.venueName = in.readString();
        this.addTime = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.workspaceCode = in.readString();
        this.dayOfWeek = in.readString();
        this.daysOfWeek = in.createStringArray();
        this.daysOfWeekString = in.readString();
    }

    public static final Creator<WorkPoint> CREATOR = new Creator<WorkPoint>() {
        public WorkPoint createFromParcel(Parcel source) {
            return new WorkPoint(source);
        }

        public WorkPoint[] newArray(int size) {
            return new WorkPoint[size];
        }
    };
}

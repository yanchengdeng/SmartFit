package com.smartfit.beans;/**
 * 作者：dengyancheng on 16/6/6 22;//39
 * 邮箱：yanchengdeng@gmail.com
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：dengyancheng on 16/6/6 22;//39
 * 邮箱：yanchengdeng@gmail.com
 */
public class LinyuRecord implements Parcelable {

    private String recordId;//166,
    private String venueId;//9,
    private String venueName;//东二环泰禾店,
    private String courseId;//324,
    private String courseName;//私教课,
    private String startTime;//1465178972,
    private String endTime;//1465179227,
    private String useTime;//255,
    private String price;//3

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.recordId);
        dest.writeString(this.venueId);
        dest.writeString(this.venueName);
        dest.writeString(this.courseId);
        dest.writeString(this.courseName);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.useTime);
        dest.writeString(this.price);
    }

    public LinyuRecord() {
    }

    protected LinyuRecord(Parcel in) {
        this.recordId = in.readString();
        this.venueId = in.readString();
        this.venueName = in.readString();
        this.courseId = in.readString();
        this.courseName = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.useTime = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<LinyuRecord> CREATOR = new Parcelable.Creator<LinyuRecord>() {
        public LinyuRecord createFromParcel(Parcel source) {
            return new LinyuRecord(source);
        }

        public LinyuRecord[] newArray(int size) {
            return new LinyuRecord[size];
        }
    };
}

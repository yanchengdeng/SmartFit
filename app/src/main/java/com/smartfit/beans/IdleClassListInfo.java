package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/19.
 */
public class IdleClassListInfo implements Serializable{

    private String venueId;//12,
    private String venueName;//福州台江健身馆,
    private String count;//4,
    private String longit;//123.1234,
    private String venueUrl;//http;////localhost;//8089/upload/picture/2016/4/2/1459595667710/1459595667.jpg,
    private String lat;//23.2345,
    private ArrayList<IdleClassInfo> classroomList;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getLongit() {
        return longit;
    }

    public void setLongit(String longit) {
        this.longit = longit;
    }

    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public ArrayList<IdleClassInfo> getClassroomList() {
        return classroomList;
    }

    public void setClassroomList(ArrayList<IdleClassInfo> classroomList) {
        this.classroomList = classroomList;
    }


}

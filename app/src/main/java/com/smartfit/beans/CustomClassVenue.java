package com.smartfit.beans;

import java.util.List;

/**
 * @author yanchengdeng
 *         create at 2016/4/25 9;//43
 */

public class CustomClassVenue {
    private String venueId;//11,
    private String venueName;//省体店,
    private String count;//1,
    private String longit;//119.27,
    private String venueUrl;//http;////123.57.164.115;//8098/uploadimgs/picture/2016/4/24/1461456971472/1461456971.JPG,
    private String lat;//26.11,
    private List<CustomClassVenueItem> classroomList;

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

    public List<CustomClassVenueItem> getClassroomList() {
        return classroomList;
    }

    public void setClassroomList(List<CustomClassVenueItem> classroomList) {
        this.classroomList = classroomList;
    }
}

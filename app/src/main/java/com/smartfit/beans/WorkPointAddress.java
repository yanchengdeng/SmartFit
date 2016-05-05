package com.smartfit.beans;

/**
 * Created by yanchengdeng on 2016/3/23.
 * 选择工作地点
 */
public class WorkPointAddress {
    private String venueId; //3,
    private String venueName;//fz2,
    private String venueUrl;// null
    private String longit;
    private String lat;

    public String getLongit() {
        return longit;
    }

    public void setLongit(String longit) {
        this.longit = longit;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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

    public String getVenueUrl() {
        return venueUrl;
    }

    public void setVenueUrl(String venueUrl) {
        this.venueUrl = venueUrl;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setIsCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    private boolean isCheck;


}

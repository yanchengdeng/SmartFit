package com.smartfit.beans;

/**
 * Created by Administrator on 2016/4/15.
 */
public class AccountRecord {

    private String id;//16,
    private String recordName;//用户充值,
    private String price;//100,
    private String recordTime;//2016-04-14 23;//10;//17

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }
}

package com.smartfit.MessageEvent;

/**
 * Created by Administrator on 2016/4/13.
 */
public class UpdateWorkPointInfo extends  UpdateWeekDay {
    private String startTime;

    private String endTime;

    private String workName;

    private String workPoint;

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

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getWorkPoint() {
        return workPoint;
    }

    public void setWorkPoint(String workPoint) {
        this.workPoint = workPoint;
    }
}

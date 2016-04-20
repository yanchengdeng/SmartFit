package com.smartfit.beans;

/**
 * Created by dengyancheng on 16/4/20.
 */
public class MessageAllInfo {

    private String unReadSysCount;
    private MesageInfo sysMessage;
    private String unReadFriendCount;
    private MesageInfo friendMessage;
    private String unReadCourseCount;
    private MesageInfo courseMessage;


    public String getUnReadSysCount() {
        return unReadSysCount;
    }

    public void setUnReadSysCount(String unReadSysCount) {
        this.unReadSysCount = unReadSysCount;
    }

    public MesageInfo getSysMessage() {
        return sysMessage;
    }

    public void setSysMessage(MesageInfo sysMessage) {
        this.sysMessage = sysMessage;
    }

    public String getUnReadFriendCount() {
        return unReadFriendCount;
    }

    public void setUnReadFriendCount(String unReadFriendCount) {
        this.unReadFriendCount = unReadFriendCount;
    }

    public MesageInfo getFriendMessage() {
        return friendMessage;
    }

    public void setFriendMessage(MesageInfo friendMessage) {
        this.friendMessage = friendMessage;
    }

    public String getUnReadCourseCount() {
        return unReadCourseCount;
    }

    public void setUnReadCourseCount(String unReadCourseCount) {
        this.unReadCourseCount = unReadCourseCount;
    }

    public MesageInfo getCourseMessage() {
        return courseMessage;
    }

    public void setCourseMessage(MesageInfo courseMessage) {
        this.courseMessage = courseMessage;
    }
}

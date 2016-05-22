package com.smartfit.beans;

/**
 * Created by dengyancheng on 16/4/20.
 */
public class ListMessageAllInfoItem {

    private String unReadSysCount;
    private MesageInfo sysMessage;


    public ListMessageAllInfoItem() {
    }
    public ListMessageAllInfoItem(String unReadSysCount, MesageInfo sysMessage) {
        this.unReadSysCount = unReadSysCount;
        this.sysMessage = sysMessage;
    }

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
}

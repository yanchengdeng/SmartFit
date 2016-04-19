package com.smartfit.MessageEvent;

/**
 * Created by Administrator on 2016/4/19.
 * 更换教室
 */
public class UpdateRoom {

    public UpdateRoom(int position) {
        this.positon = position;
    }

    public int getPositon() {
        return positon;
    }

    public void setPositon(int positon) {
        this.positon = positon;
    }

    private int positon;
}

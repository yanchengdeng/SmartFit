package com.smartfit.utils;


import  com.google.gson.JsonObject;

/**
 * Created by Administrator on 2015/6/4.
 */
public class ResponseData {

    private JsonObject data;//返回数据
    private String error;//错误信息
    private String state;//接口状态
    private String stateMsg;//状态信息
    private String total;

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateMsg() {
        return stateMsg;
    }

    public void setStateMsg(String stateMsg) {
        this.stateMsg = stateMsg;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

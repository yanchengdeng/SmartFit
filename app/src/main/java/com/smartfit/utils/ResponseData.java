package com.smartfit.utils;

import com.google.gson.JsonObject;

/**
 * Created by Administrator on 2015/6/4.
 */
public class ResponseData {

    private String code ;

    private String msg;

    private JsonObject data;

    private String requestInterface;

    public String getRequestInterface() {
        return requestInterface;
    }

    public void setRequestInterface(String requestInterface) {
        this.requestInterface = requestInterface;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}

package com.smartfit.beans;

/**
 * 作者： 邓言诚 创建于： 2016/5/26 13;//39.
 */
public class WXPayAppId {
    private String appid;// wx081f8efdfee69ab2,
    private String sign;// E7B9DFAD1FB238BC548B8D1F54B6C5B0,
    private String partnerid;// 1342800901,
    private String prepayid;// wx20160526115123754f2323c20737275816,
    private String noncestr;// 1464234448196,
    private String timestamp;// 1464234448

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

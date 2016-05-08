package com.smartfit.beans;/**
 * 作者：dengyancheng on 16/5/8 23;//20
 * 邮箱：yanchengdeng@gmail.com
 */

/**
 *
 * 作者：dengyancheng on 16/5/8 23;//20
 * 邮箱：yanchengdeng@gmail.com
 * 
 * 课程详情中的  教练信息
 */
public class CoachInfo {

   private String coachId;//12,
    private String        uid;//19,
    private String       nickName;//从家出发,
    private String      userPicUrl;//http;////123.57.164.115;//8098/uploadimgs/picture/2016/4/2/1459599253692/1459599253.png,
    private String      price;//200.5,
    private String      stars;//3.5,
    private String      sex;//1,
    private String      startTime;//null,
    private String       endTime;//null,
    private String       signature;//曾经经常觉得肯德基德克士的快点看快点快点快点到快点看看,
    private String      phone;//18650328285

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserPicUrl() {
        return userPicUrl;
    }

    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

package com.smartfit.beans;

/**
 * Created by dengyancheng on 16/4/3.
 */
public class CoachDetailInfo {

    private String nickName;//null,
    private String userPicUrl;//null,
    private String coachRealName;//null,
    private String sex;//0,
    private String coachDesc;//null,
    private String stars;//0,
    private String courseCount;//null,
    private String hight;//null,
    private String weight;//null,
    private String certificates;//null,
    private String coachClassDesc;//null,
    private String authenCoachClassDesc;//null

    public String getResumeContent() {
        return resumeContent;
    }

    public void setResumeContent(String resumeContent) {
        this.resumeContent = resumeContent;
    }

    private String resumeContent;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String age;

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

    public String getCoachRealName() {
        return coachRealName;
    }

    public void setCoachRealName(String coachRealName) {
        this.coachRealName = coachRealName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCoachDesc() {
        return coachDesc;
    }

    public void setCoachDesc(String coachDesc) {
        this.coachDesc = coachDesc;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(String courseCount) {
        this.courseCount = courseCount;
    }

    public String getHight() {
        return hight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getCoachClassDesc() {
        return coachClassDesc;
    }

    public void setCoachClassDesc(String coachClassDesc) {
        this.coachClassDesc = coachClassDesc;
    }

    public String getAuthenCoachClassDesc() {
        return authenCoachClassDesc;
    }

    public void setAuthenCoachClassDesc(String authenCoachClassDesc) {
        this.authenCoachClassDesc = authenCoachClassDesc;
    }
}

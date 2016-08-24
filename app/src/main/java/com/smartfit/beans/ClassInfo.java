package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dengyancheng on 16/3/19.
 * 课程列表信息
 */
public class ClassInfo implements Parcelable {

    private String courseType;//null,
    private String otherCount;//null,
    private String partCount;//null,
    private String courseId;//17,
    private String courseName;//教练发起团操课,
    private String coachRealName;//徐晃,
    private String linkPhone;//null,
    private String coachId;//12,
    private String price;//100,
    private String nickName;//从家出发,
    private String stars;//0,
    private String endTime;//1459908000,
    private String personCount;//null,
    private String classroomPersonCount;//30,
    private String courseDetail;//null,
    private String classUrl;//http;////localhost;//8089/upload/picture/2016/3/27/1459078205056/1459078205.jpg,
    private String beginTime;//1459904400

    public String getOpenAppointmentTime() {
        return openAppointmentTime;
    }

    public void setOpenAppointmentTime(String openAppointmentTime) {
        this.openAppointmentTime = openAppointmentTime;
    }

    private String openAppointmentTime;

    public String getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    private String courseStatus;//0:未开始1:正在进行2:已结束

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(String otherCount) {
        this.otherCount = otherCount;
    }

    public String getPartCount() {
        return partCount;
    }

    public void setPartCount(String partCount) {
        this.partCount = partCount;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCoachRealName() {
        return coachRealName;
    }

    public void setCoachRealName(String coachRealName) {
        this.coachRealName = coachRealName;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPersonCount() {
        return personCount;
    }

    public void setPersonCount(String personCount) {
        this.personCount = personCount;
    }

    public String getClassroomPersonCount() {
        return classroomPersonCount;
    }

    public void setClassroomPersonCount(String classroomPersonCount) {
        this.classroomPersonCount = classroomPersonCount;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    public String getClassUrl() {
        return classUrl;
    }

    public void setClassUrl(String classUrl) {
        this.classUrl = classUrl;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.courseType);
        dest.writeString(this.otherCount);
        dest.writeString(this.partCount);
        dest.writeString(this.courseId);
        dest.writeString(this.courseName);
        dest.writeString(this.coachRealName);
        dest.writeString(this.linkPhone);
        dest.writeString(this.coachId);
        dest.writeString(this.price);
        dest.writeString(this.nickName);
        dest.writeString(this.stars);
        dest.writeString(this.endTime);
        dest.writeString(this.personCount);
        dest.writeString(this.classroomPersonCount);
        dest.writeString(this.courseDetail);
        dest.writeString(this.classUrl);
        dest.writeString(this.beginTime);
        dest.writeString(this.openAppointmentTime);
        dest.writeString(this.courseStatus);
    }

    public ClassInfo() {
    }

    protected ClassInfo(Parcel in) {
        this.courseType = in.readString();
        this.otherCount = in.readString();
        this.partCount = in.readString();
        this.courseId = in.readString();
        this.courseName = in.readString();
        this.coachRealName = in.readString();
        this.linkPhone = in.readString();
        this.coachId = in.readString();
        this.price = in.readString();
        this.nickName = in.readString();
        this.stars = in.readString();
        this.endTime = in.readString();
        this.personCount = in.readString();
        this.classroomPersonCount = in.readString();
        this.courseDetail = in.readString();
        this.classUrl = in.readString();
        this.beginTime = in.readString();
        this.openAppointmentTime = in.readString();
        this.courseStatus = in.readString();
    }

    public static final Parcelable.Creator<ClassInfo> CREATOR = new Parcelable.Creator<ClassInfo>() {
        @Override
        public ClassInfo createFromParcel(Parcel source) {
            return new ClassInfo(source);
        }

        @Override
        public ClassInfo[] newArray(int size) {
            return new ClassInfo[size];
        }
    };
}

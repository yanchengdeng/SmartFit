package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： 邓言诚 创建于： 2016/8/26 15;//03.
 */
public class MyAbsentClass implements Parcelable {

    private String id;// 2926,
    private String courseType;// 3,
    private String courseName;// 器械区,
    private String courseDetail;// 器械区,
    private String courseClassId;// null,
    private String coursePrice;// 19,
    private String orderPrice;// null,
    private String coachId;// null,
    private String coachPhone;// null,
    private String startUserId;// 1920,
    private String startUserName;// 123456,
    private String startUserPicUrl;// null,
    private String status;// 0,
    private String signCount;// 1,
    private String maxPersonCount;// 1,
    private String startTime;// 1472191200,
    private String endTime;// 1472194800,
    private String topicId;// 2016082613495655129512926,
    private String venueId;// 23,
    private String coachUid;// null,
    private String coachNickName;// null,
    private String coachUserPicUrl;// null,
    private String isLate;// null,
    private String score;// -3,
    private String punishTime;// 0

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    public String getCourseClassId() {
        return courseClassId;
    }

    public void setCourseClassId(String courseClassId) {
        this.courseClassId = courseClassId;
    }

    public String getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(String coursePrice) {
        this.coursePrice = coursePrice;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getCoachId() {
        return coachId;
    }

    public void setCoachId(String coachId) {
        this.coachId = coachId;
    }

    public String getCoachPhone() {
        return coachPhone;
    }

    public void setCoachPhone(String coachPhone) {
        this.coachPhone = coachPhone;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    public String getStartUserName() {
        return startUserName;
    }

    public void setStartUserName(String startUserName) {
        this.startUserName = startUserName;
    }

    public String getStartUserPicUrl() {
        return startUserPicUrl;
    }

    public void setStartUserPicUrl(String startUserPicUrl) {
        this.startUserPicUrl = startUserPicUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignCount() {
        return signCount;
    }

    public void setSignCount(String signCount) {
        this.signCount = signCount;
    }

    public String getMaxPersonCount() {
        return maxPersonCount;
    }

    public void setMaxPersonCount(String maxPersonCount) {
        this.maxPersonCount = maxPersonCount;
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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getCoachUid() {
        return coachUid;
    }

    public void setCoachUid(String coachUid) {
        this.coachUid = coachUid;
    }

    public String getCoachNickName() {
        return coachNickName;
    }

    public void setCoachNickName(String coachNickName) {
        this.coachNickName = coachNickName;
    }

    public String getCoachUserPicUrl() {
        return coachUserPicUrl;
    }

    public void setCoachUserPicUrl(String coachUserPicUrl) {
        this.coachUserPicUrl = coachUserPicUrl;
    }

    public String getIsLate() {
        return isLate;
    }

    public void setIsLate(String isLate) {
        this.isLate = isLate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPunishTime() {
        return punishTime;
    }

    public void setPunishTime(String punishTime) {
        this.punishTime = punishTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.courseType);
        dest.writeString(this.courseName);
        dest.writeString(this.courseDetail);
        dest.writeString(this.courseClassId);
        dest.writeString(this.coursePrice);
        dest.writeString(this.orderPrice);
        dest.writeString(this.coachId);
        dest.writeString(this.coachPhone);
        dest.writeString(this.startUserId);
        dest.writeString(this.startUserName);
        dest.writeString(this.startUserPicUrl);
        dest.writeString(this.status);
        dest.writeString(this.signCount);
        dest.writeString(this.maxPersonCount);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeString(this.topicId);
        dest.writeString(this.venueId);
        dest.writeString(this.coachUid);
        dest.writeString(this.coachNickName);
        dest.writeString(this.coachUserPicUrl);
        dest.writeString(this.isLate);
        dest.writeString(this.score);
        dest.writeString(this.punishTime);
    }

    public MyAbsentClass() {
    }

    protected MyAbsentClass(Parcel in) {
        this.id = in.readString();
        this.courseType = in.readString();
        this.courseName = in.readString();
        this.courseDetail = in.readString();
        this.courseClassId = in.readString();
        this.coursePrice = in.readString();
        this.orderPrice = in.readString();
        this.coachId = in.readString();
        this.coachPhone = in.readString();
        this.startUserId = in.readString();
        this.startUserName = in.readString();
        this.startUserPicUrl = in.readString();
        this.status = in.readString();
        this.signCount = in.readString();
        this.maxPersonCount = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.topicId = in.readString();
        this.venueId = in.readString();
        this.coachUid = in.readString();
        this.coachNickName = in.readString();
        this.coachUserPicUrl = in.readString();
        this.isLate = in.readString();
        this.score = in.readString();
        this.punishTime = in.readString();
    }

    public static final Parcelable.Creator<MyAbsentClass> CREATOR = new Parcelable.Creator<MyAbsentClass>() {
        @Override
        public MyAbsentClass createFromParcel(Parcel source) {
            return new MyAbsentClass(source);
        }

        @Override
        public MyAbsentClass[] newArray(int size) {
            return new MyAbsentClass[size];
        }
    };
}

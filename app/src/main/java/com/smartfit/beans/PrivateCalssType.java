package com.smartfit.beans;/**
 * 作者：dengyancheng on 16/7/8 21;//26
 * 邮箱：yanchengdeng@gmail.com
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：dengyancheng on 16/7/8 21;//26
 * 邮箱：yanchengdeng@gmail.com
 * 私教课  类别
 */
public class PrivateCalssType implements Parcelable {

    private String classificationId;//69,
    private String classificationName;//Smart瘦身,
    private String classificationImg;//http;////139.196.228.98;//7097/uploadimgs/picture/2016/5/19/1463649848099/1463649848.jpg,
    private String classificationDesc;//null,
    private String coachNum;//20

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public String getClassificationImg() {
        return classificationImg;
    }

    public void setClassificationImg(String classificationImg) {
        this.classificationImg = classificationImg;
    }

    public String getClassificationDesc() {
        return classificationDesc;
    }

    public void setClassificationDesc(String classificationDesc) {
        this.classificationDesc = classificationDesc;
    }

    public String getCoachNum() {
        return coachNum;
    }

    public void setCoachNum(String coachNum) {
        this.coachNum = coachNum;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.classificationId);
        dest.writeString(this.classificationName);
        dest.writeString(this.classificationImg);
        dest.writeString(this.classificationDesc);
        dest.writeString(this.coachNum);
    }

    public PrivateCalssType() {
    }

    protected PrivateCalssType(Parcel in) {
        this.classificationId = in.readString();
        this.classificationName = in.readString();
        this.classificationImg = in.readString();
        this.classificationDesc = in.readString();
        this.coachNum = in.readString();
    }

    public static final Parcelable.Creator<PrivateCalssType> CREATOR = new Parcelable.Creator<PrivateCalssType>() {
        public PrivateCalssType createFromParcel(Parcel source) {
            return new PrivateCalssType(source);
        }

        public PrivateCalssType[] newArray(int size) {
            return new PrivateCalssType[size];
        }
    };
}

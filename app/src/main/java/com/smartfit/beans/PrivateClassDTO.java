package com.smartfit.beans;/**
 * 作者：dengyancheng on 16/7/8 22;//29
 * 邮箱：yanchengdeng@gmail.com
 */

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * 作者：dengyancheng on 16/7/8 22;//29
 * 邮箱：yanchengdeng@gmail.com
 */
public class PrivateClassDTO implements Parcelable {

   private String  startTime;//32400,
    private String      endTime;//79200

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
    }

    public PrivateClassDTO() {
    }

    protected PrivateClassDTO(Parcel in) {
        this.startTime = in.readString();
        this.endTime = in.readString();
    }

    public static final Parcelable.Creator<PrivateClassDTO> CREATOR = new Parcelable.Creator<PrivateClassDTO>() {
        public PrivateClassDTO createFromParcel(Parcel source) {
            return new PrivateClassDTO(source);
        }

        public PrivateClassDTO[] newArray(int size) {
            return new PrivateClassDTO[size];
        }
    };
}

package com.smartfit.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者： 邓言诚 创建于： 2016/8/18 16:22.
 */
public class EntityCardInfo implements Parcelable {

    private String code;
    /**
     * 14;//卡－月卡
     15;//卡－季卡
     16;//卡－年卡
     */
    private String type;

    public EntityCardInfo(String code, String type) {
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.type);
    }

    public EntityCardInfo() {
    }

    protected EntityCardInfo(Parcel in) {
        this.code = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<EntityCardInfo> CREATOR = new Parcelable.Creator<EntityCardInfo>() {
        public EntityCardInfo createFromParcel(Parcel source) {
            return new EntityCardInfo(source);
        }

        public EntityCardInfo[] newArray(int size) {
            return new EntityCardInfo[size];
        }
    };
}

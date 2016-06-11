package com.smartfit.beans;/**
 * 作者：dengyancheng on 16/6/6 22;//24
 * 邮箱：yanchengdeng@gmail.com
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 * 作者：dengyancheng on 16/6/6 22;//24
 * 邮箱：yanchengdeng@gmail.com
 */
public class LingyunListInfo implements Parcelable {

  private String   totalCount;//2,
    private String          pageNo;//1,
    private String         pageSize;//2,
    private List<LinyuRecord> listData;


    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public List<LinyuRecord> getListData() {
        return listData;
    }

    public void setListData(List<LinyuRecord> listData) {
        this.listData = listData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.totalCount);
        dest.writeString(this.pageNo);
        dest.writeString(this.pageSize);
        dest.writeTypedList(listData);
    }

    public LingyunListInfo() {
    }

    protected LingyunListInfo(Parcel in) {
        this.totalCount = in.readString();
        this.pageNo = in.readString();
        this.pageSize = in.readString();
        this.listData = in.createTypedArrayList(LinyuRecord.CREATOR);
    }

    public static final Parcelable.Creator<LingyunListInfo> CREATOR = new Parcelable.Creator<LingyunListInfo>() {
        public LingyunListInfo createFromParcel(Parcel source) {
            return new LingyunListInfo(source);
        }

        public LingyunListInfo[] newArray(int size) {
            return new LingyunListInfo[size];
        }
    };
}

package com.smartfit.MessageEvent;

/**
 * 作者： 邓言诚 创建于： 2016/8/26 16:54.
 * <p>
 * 申述课程完成
 */
public class AllegeClassOver {
    public AllegeClassOver(String id) {
        this.classId = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    private String classId;

}

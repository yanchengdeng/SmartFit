package com.smartfit.beans;

/**
 * 作者： 邓言诚 创建于： 2016/7/5 14:06.
 */
public class SelectMouth {

    public SelectMouth(int num, boolean isSelect) {
        this.num = num;
        this.isSelect = isSelect;
    }

    private int num;

    private boolean isSelect;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}

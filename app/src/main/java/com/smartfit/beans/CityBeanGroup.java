package com.smartfit.beans;

import java.util.List;

/**
 * Created by dengyancheng on 16/3/25.
 */
public class CityBeanGroup {

    private String index;

    private List<CityBean> tags;


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<CityBean> getTags() {
        return tags;
    }

    public void setTags(List<CityBean> tags) {
        this.tags = tags;
    }
}

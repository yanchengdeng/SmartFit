package com.smartfit.beans;

/**
 * Created by dengyancheng on 16/3/27.
 * 认证更多证书
 *
 *
 */
public class Certificate {

    private String text_tips;//

    public String getText_tips() {
        return text_tips;
    }

    public void setText_tips(String text_tips) {
        this.text_tips = text_tips;
    }

    public String getImage_tips() {
        return image_tips;
    }

    public void setImage_tips(String image_tips) {
        this.image_tips = image_tips;
    }

    private String image_tips;

    public String name;

    private String photo;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

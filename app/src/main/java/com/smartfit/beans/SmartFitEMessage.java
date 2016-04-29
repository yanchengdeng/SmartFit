package com.smartfit.beans;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.adapter.message.EMAMessage;

/**
 * 作者： 邓言诚 创建于： 2016/4/29 12:41.
 *
 * 重新 环信消息
 */
public class SmartFitEMessage extends EMMessage {
    public SmartFitEMessage(EMAMessage emaMessage) {
        super(emaMessage);
    }

    private String userHeader ;

    private String friendsHeader;

    public String getUserHeader() {
        return userHeader;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    public String getFriendsHeader() {
        return friendsHeader;
    }

    public void setFriendsHeader(String friendsHeader) {
        this.friendsHeader = friendsHeader;
    }
}

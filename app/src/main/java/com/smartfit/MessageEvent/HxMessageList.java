package com.smartfit.MessageEvent;/**
 * 作者：dengyancheng on 16/5/22 11:44
 * 邮箱：yanchengdeng@gmail.com
 */

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 *
 * 作者：dengyancheng on 16/5/22 11:44
 * 邮箱：yanchengdeng@gmail.com
 */
public class HxMessageList {

    public HxMessageList(List<EMMessage> messages) {
        this.messages = messages;
    }

    public List<EMMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<EMMessage> messages) {
        this.messages = messages;
    }

    List<EMMessage> messages;


}

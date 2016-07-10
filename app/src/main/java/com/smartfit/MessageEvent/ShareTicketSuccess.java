package com.smartfit.MessageEvent;/**
 * 作者：dengyancheng on 16/7/10 10:48
 * 邮箱：yanchengdeng@gmail.com
 */

/**
 *
 * 作者：dengyancheng on 16/7/10 10:48
 * 邮箱：yanchengdeng@gmail.com
 * 分享券 成功
 */
public class ShareTicketSuccess {
    public boolean isFinishNow() {
        return finishNow;
    }

    public void setFinishNow(boolean finishNow) {
        this.finishNow = finishNow;
    }

    private boolean finishNow;
}

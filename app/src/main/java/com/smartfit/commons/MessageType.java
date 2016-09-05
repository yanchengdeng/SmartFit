package com.smartfit.commons;

/**
 * 消息类型
 * 作者：dengyancheng on 16/5/2 17:01
 * 邮箱：yanchengdeng@gmail.com
 */
public class MessageType {

    /**
     * 无 0
     */
    public static String MESSAGE_TYPE_NONE = "0";
    /**
     * 系统消息 1
     */
    public static String MESSAGE_TYPE_SYTEM = "1";
    /**
     * 课程邀请消息 2
     */
    public static String MESSAGE_TYPE_COURSE_INVITE = "2";
    /**
     * 预约成功消息 3
     */
    public static String MESSAGE_TYPE_APPOStringMENT_SUCCESS = "3";
    /**
     * 课程请求消息  4
     */
    public static String MESSAGE_TYPE_COURSE_REQUEST = "4";
    /**
     * 好友邀请消息 5
     */
    public static String MESSAGE_TYPE_FRIEND_INVITE = "5";
    /**
     * 订单成功消息 6
     */
    public static String MESSAGE_TYPE_ORDER_SUCCESS = "6";
    /**
     * 拒绝课程 7
     */
    public static String MESSAGE_TYPE_COURSE_REFUSE = "7";
    /**
     * 课程代课申请---发给教练 8
     */
    public static String MESSAGE_TYPE_COURSE_SUBSTITUTE_TO_COACH = "8";
    /**
     * 课程代课申请--发给用户 9
     */
    public static String MESSAGE_TYPE_COURSE_SUBSTITUTE_TO_USER = "9";
    /**
     * 课程代课申请--接受后发给发起教练  10
     */
    public static String MESSAGE_TYPE_COURSE_SUBSITUTE_ACCEPT = "10";
    /**
     * 用户参加小班课消息通知教练  11
     */
    public static String MESSAGE_TYPE_SMALL_CLASS_NOTICE_COACH = "11";
    /**
     * 用户取消了小班课和私教课通知教练  12
     */
    public static String MESSAGE_TYPE_COURSE_CANCLE_CLASS_NOTCI_COACH = "12";
    /**
     * 教练取消课程--通知参与用户  13
     */
    public static String MESSAGE_TYPE_COURSE_COACH_CANCLE_CLASS_NOTICE_USER = "13";



    /***
     * 有氧器械课程结束
     */

    public static String AEREABICON_CLASS_OVER = "24";

    /**
     * 14.团操课和器械区在课前1小时进行短信和系统消息的提示［v1.1］
     */
    public static String ONE_HOUR_TIPS_BEFORE_OPREN_CLASS = "14";
    /**
     * 15:旷课消息【V1.1】
     31：申诉通过【v1.1】
     32：申诉不通过【v1.1】
     */


    public static String ABSENT_CLASS_MSG = "15";
    /**16：排队消息（排到）【v1.1】
     *
     */

    public static String RANK_LINE_SUCCESS = "16";

    /**
     * 排队消息（未排到）
     */
    public static String RANK_LINE_ING = "17";
    /**
     * 礼券赠送消息
     */

    public static String TICKE_GIFT_GIVE = "21";


    /**
     *领取
     */

    public static String TICKE_GIFT_ACCEPTE = "22";

    /***
     * 券退回
     */
    public static String TICKET_BACK_MESSAGE = "23";
    /**
     * 24;器械课完结后通知领券
     */

    public static String AREO_OVER_CLASS_GET_TICKT = "24";
    public static String STEAGE_PASS = "31";
    public static String SETAGE_PASS_NOT = "32";





}

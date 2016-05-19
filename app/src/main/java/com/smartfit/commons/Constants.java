package com.smartfit.commons;

/**
 * Created by dengyancheng on 16/2/23.
 * 常量类  主要为一些网络地址     键值对等
 */
public class Constants {


    public final static String FRIST_OPEN_APP = "first_open_app";

    /*****
     * 首页传入 位置
     */
    public final static String FRAGMENT_POSITION = "positon";


    public final static String PASS_STRING = "pass_string";

    public final static String PASS_OBJECT = "pass_object";

    public final static String PASS_IDLE_CLASS_INFO = "idle_class_info";
    /***
     * //定义  1 ：团体课  2.小班课  3.私教课 .4 自顶课程  5 再次开课  6 自订课程  7  活动绑定
     */
    public static final String PAGE_INDEX = "page_index";


    public static final String CITY_CODE = "city_code";
    public static final String CITY_LONGIT = "city_longit";
    public static final String CITY_LAT = "city_lat";


    public static final String CITY_LIST_INOF = "city_list";
    public static final String CITY_NAME = "city_name";

    public static final String VENUE_LIST_INFO = "venue_list_info";//场馆列表信息

    public static final String VENU_ID = "venu_id";

    public static final String COURSE_TYPE = "course_type";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_ORDER_CODE = "course_order_code";
    public static final String COURSE_MONEY = "course_pay";

    public static final String ORDER_TIME = "order_time";


    public static final String UID = "uid";//用户id
    public static final String ACCOUNT = "account";//账号
    public static final String PASSWORD = "password";
    public static final String SID = "sid";//会话id
    public static final String IS_ICF = "is_icf";//是否教练认证
    public static final String OPEN_COACH_AUTH = "open_coach_auth";
    public static final String USER_INFO = "user_info";
    public static final String COACH_ID = "coach_id";

    public static final String CLINET_ID="client_id";


    /**
     * 接口域名方法
     */

    //账户相关接口
    public static final String REGISTER_METHOD = "/User/RegUser";
    public static final String LOGIN_IN_METHOD = "/User/Login";
    public static final String SMS_SMSSEND = "/SMS/SMSSend";
    public static final String RESET_PASSOWRD = "/User/ResetPassword";
    public static final String USER_CONTACTCOURSELIST = "/User/myContactCourseList";
    public static final String RECORD_GETRECORDLIST = "/Record/getRecordList";

    //课程相关接口
    public static final String SEARCH_CLASS = "/ClassIf/searchInfo";
    public static final String SEARCH_CLASS_DETAIL = "/ClassIf/getClassifInfo";
    public static final String CLASS_COMMEND = "/comment/getCommentsByTopicId";
    public static final String GET_VENUElIST = "/ClassIf/getVenueList";
    public static final String GET_CLASS_LIST = "/ClassIf/getClassifList";
    public static final String COACH_LISTIDLECOACHESBYVENUEID = "/coach/listIdleCoachesByVenueId";
    public static final String USER_CANCELCOURSELIST = "/User/cancelCourseList";
    public static final String COMMENT_SAVE = "/comment/save";
    public static final String COURSE_COACHJOINCOURSE = "/course/coachJoinCourse";
    public static final String COACH_BESPOKECOACH = "/coach/bespokeCoach";
    public static final String CLASSIF_GETCLASSIFICATION = "/ClassIf/getClassification";
    public static final String CLASSIF_GETIDLECLASSROOMSVENUELIST = "/ClassIf/getIdleClassroomsVenueList";
    public static final String COURSE_MEMBERRELEASECOURSE = "/course/memberReleaseCourse";
    public static final String CLASSIF_GETSELFDESIGNCOURSELIST = "/ClassIf/getSelfDesignCourseList";
    public static final String COURSE_COACHRELEASECOURSE = "/course/coachReleaseCourse";
    public static final String COURSE_MEMBERLIST = "/course/memberList";
    public static final String COACH_SUBSTITUTECOACH = "/coach/substituteCoach";
    public static final String COURSE_COACHREOPENCOURSE = "/course/coachReopenCourse";
    public static final String CLASSROOM_GETIDLEAEROBICCLASSROOMS = "/classroom/getIdleAerobicClassrooms";
    public static final String CLASSROOM_GETCLASSROOM = "/classroom/getClassroom";
    public static final String ORDER_ORDERAEROBIC = "/order/orderAerobic";


    //用户信息
    public static final String MAIN_PAGE_INFO = "/User/mainPage";
    public static final String USER_USERINFO = "/User/userInfo";
    public static final String USER_CONCERNLIST = "/User/concernList";
    public static final String USER_FANSLIST = "/User/fansList";
    public static final String USER_SEARCHFRIENDLIST = "/User/searchFriendList";
    public static final String USER_ADDFOCUS = "/User/addfocus";
    public static final String USER_ADDFRIEND = "/User/addFriend";
    public static final String USER_SAVEUSERINFO = "/User/saveUserInfo";
    public static final String USER_FRIENDLIST = "/User/friendList";
    public static final String USER_APPLYCASH = "/User/applyCash";
    public static final String USER_SAVECOACHCOURSETYPES = "/User/saveCoachCourseTypes";
    public static final String USER_GETCOACHCOURSETYPES = "/User/getCoachCourseTypes";

    //教练
    public static final String COACH_ADD_CERTIFICATE = "/coach/addCertificate";
    public static final String COACH_LISTCERTIFICATE = "/coach/listCertificate";
    public static final String COACH_GETCOACHINFO = "/coach/getCoachInfo";
    public static final String COACH_GETRESUME = "/coach/getResume";
    public static final String COACH_UPDATERESUME = "/coach/updateResume";
    public static final String GET_COACHPAGEINFO = "/coach/getCoachPageInfo";
    public static final String WORKSPACE_LIST = "/workspace/List";
    public static final String WORKSPACE_DELETE = "/workspace/Delete";
    public static final String WORKSPACE_ADD = "/workspace/Add";
    public static final String COACH_CHGCOACHONLINESTATE = "/coach/chgCoachOnlineState";
    public static final String CLASSIF_LISTTHEVENUEIDLECLASSROOMS = "/ClassIf/listTheVenueIdleClassrooms";
    public static final String COACH_LISTIDLECOACHESBYVENUEIDANDCOURSETYPECODE = "/coach/listIdleCoachesByVenueIdAndCourseTypeCode";
    public static final String COACH_DELCERTIFICATE = "/coach/delCertificate";


    //系统数据
    public static final String GET_CITY_LIST = "/sys/dict/listCities";
    public static final String UPLOAD_PHOTOS = "/sys/upload/uploadPic";
    public static final String MESSAGE_GETMESSAGEMAIN = "/message/getMessageMain";
    public static final String SYS_SAVEFEEDBACK = "/sys/saveFeedback";
    public static final  String USER_CHGOPENPUSH = "/User/chgOpenPush";
    public static final String USER_CHGOPENSOUND="/User/chgOpenSound";
    public static final String USER_SYNCLIENTIID="/User/synClientiId";


    //订单
    public static final String ORDER_ORDERCOURSE = "/order/orderCourse";
    public static final String PAY_BALANCEPAY = "/pay/balancePay";
    public static final String PAY_PAYMOCK = "/pay/payMock";
    public static final String ORDER_ORDERCHARGE = "/order/orderCharge";
    public static final String EVENT_LISTEVENTS = "/event/listEvents";
    public static final String EVENT_GETEVENT = "/event/getEvent";
    public static final String ORDER_ORDEREVENT = "/order/orderEvent";


    //活动
    public static final String EVENT_LISTUSEREVENT = "/event/listUserEvents";
    public static final String DYNAMIC_GETDYNAMICLIST = "/dynamic/getDynamicList";
    public static final String DYNAMIC_ADDDYNAMIC = "/dynamic/addDynamic";
    public static final String DYNAMIC_GOOD = "/dynamic/good";


    //消息
    public static final String MESSAGE_DEL = "/message/del";
    public static final String MESSAGE_LIST = "/message/list";
    public static final String COACH_RECEIVESUBSTITUTECOACH = "/coach/receiveSubstituteCoach";
    public static final String COACH_REJECTSUBSTITUTECOACH = "/coach/rejectSubstituteCoach";
    public static final String CLASSIF_INVITEFRIENDS = "/ClassIf/inviteFriends";

    public static final int SIZE = 20;
    public static final String SEX_MAN = "0";
    public static final String SEX_WOMEN = "1";


    /*****
     * 支付宝支付参数
     */
    public static class AliPay {
        // 商户PID
        public static final String PARTNER = "2088221475684324";
        // 商户收款账号
        public static final String SELLER = "smartfit2016@163.com";
        // 商户私钥，pkcs8格式
        public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANAshb9FgtZ3KM/o98QwohXofTLGza+Ec/XAekYB71Nc2jRVKM7ZR/mG5QLwyOVjyk+RldbVEZ72AXv/qT93JTD5bg8YgkKeKqiQ4TUZtAcRpdeZJ6v+3b77E+Lr89mIxJq5o9MueTi/GmDFlGdqgKhtj5KZRWVFMatNqidRos7VAgMBAAECgYEAz1HH0Xl0mAoMByNIvYaBpbfoF82NwqLyqWiLXXVH14m9dijpfzc9SvBg2tDaAjmjniKrS0zfQ6RUTh2LfL03twaXHVKo8yYzcIuErMT2zHSC58rpWPm4HNL1/4gepyKGXSAVLft4VzT0A5CBYyHajoRqc7oQyZ2w0vlEOSVhfwECQQDp45IR6fu6CqI9Za5wZj1JDRNbyTPw/G8Fzel6k0U1arEtwWgTk8ZPj8tgWx9OQJ2pMuCMKOJ9rB/qY6qgj6AxAkEA49qc/klOaNhgkwDN5j4D2hO0ozfz2Gg+rBqNfj/e1CtFET+6uZlEAaRPz9v2x94DxhGDM4w8DwHw84JI4Trz5QJABanoGJz05o1pXlk+/yPKqiEx7dASfOxv99EDrV8xltiNj9EWJxs1+yk9QIkUWf3Ak59SxrIawfZQE4n0UM0JkQJBANPGLIRrveTUt23qIXUNzqayCgJpzsokmZt8UmY1FvY8AUW25eX6/apI/aVD3GqHfxpozHjsriWEuF6biQFNucECQQCZjKTzWMtSGa3CQFFtVggNvNLqJ/WOZmokc+BR9qW++fXyFXsxYon3Rrat269EP3JHZ+O4VEURPpWk8gxH3ykn";
             // 支付宝公钥
        public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
        public static final int SDK_PAY_FLAG = 1;
    }


    public static class WXPay {
        //appid
        //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wx275d8ba3c5fccaf5"/>为新设置的appid
        public static final String APP_ID = "wxcc98b5cd5a985e3f";
        //商户号
        public static final String MCH_ID = "1306645401";
        //  API密钥，在商户平台设置
        public static final String API_KEY = "70FB3464792B4C33B89B1AB11548C4E2";


    }

    public static class Net {
        //发布时的接口
        public static final String URL = "http://139.196.228.98:8098";

        //测试
//        public static final String URL = "http://123.57.164.115:8098";


        //支付回调
        public static final String ALIPAY_CALLBACK = Net.URL + "/pay/aliCallback";
        public static final String WX_PAY_CALLBACK = Net.URL + "/pay/weixinCallback";

    }
}

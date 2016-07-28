package com.smartfit.commons;

/**
 * Created by dengyancheng on 16/2/23.
 * 常量类  主要为一些网络地址     键值对等
 */
public class Constants {


    public final static String FRIST_OPEN_APP = "first_open_app";

    public final static String MIAN_ADS = "main_ads";//主页广告

    public final static String SPLASH_ADS = "splash_ads";//封面广告

    /*****
     * 首页传入 位置
     */
    public final static String FRAGMENT_POSITION = "positon";


    public final static String PASS_STRING = "pass_string";

    public final static String PASS_OBJECT = "pass_object";

    public final static String PASS_IDLE_CLASS_INFO = "idle_class_info";
    /***
     * //定义  1 ：团体课  2.小班课  3.私教课 .4 有氧课  5 再次开课  6 自订课程  7  活动绑定   8 淋浴支付  9 包月  10  包季度/半年
     */
    public static final String PAGE_INDEX = "page_index";

    public static final String SHARE_ID = "SHARE_ID";


    public static final String CITY_CODE = "city_code";
    public static final String CITY_LONGIT = "city_longit";
    public static final String CITY_LAT = "city_lat";
    public static final String LOCAL_FRIENDS_LIST = "local_friends_list";//好友列表


    public static final String CITY_LIST_INOF = "city_list";
    public static final String CITY_NAME = "city_name";

    public static final String VENUE_LIST_INFO = "venue_list_info";//场馆列表信息

    public static final String VENU_ID = "venu_id";

    public static final String COURSE_TYPE = "course_type";//课程类型（0团操；1小班；2私教；3器械）
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_ORDER_CODE = "course_order_code";
    public static final String COURSE_MONEY = "course_pay";

    public static final String ORDER_TIME = "order_time";

    public static final String SELECT_ADDRESS_VENER = "SELECT_ADDRESS_VENER";


    public static final String UID = "uid";//用户id
    public static final String ACCOUNT = "account";//账号
    public static final String PASSWORD = "password";
    public static final String SID = "sid";//会话id
    public static final String IS_ICF = "is_icf";//是否教练认证
    public static final String OPEN_COACH_AUTH = "open_coach_auth";
    public static final String USER_INFO = "user_info";
    public static final String COACH_ID = "coach_id";

    public static final String CLINET_ID = "client_id";

    public static final String IS_VIP = "is_vip";

    public static boolean IS_PASS_FROM_ORDER = true;//购买    、充值 false
    public static int PAGE_INDEX_FROM = 1;// 1 ：团体课  2.小班课  3.私教课 .4 自顶课程  5 再次开课  6 自订课程  7  活动绑定  8 .淋浴充值

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
    public static final String USER_CHGOPENPUSH = "/User/chgOpenPush";
    public static final String USER_CHGOPENSOUND = "/User/chgOpenSound";
    public static final String USER_SYNCLIENTIID = "/User/synClientiId";


    //订单
    public static final String ORDER_ORDERCOURSE = "/order/orderCourse";
    public static final String PAY_BALANCEPAY = "/pay/balancePay";
    public static final String PAY_PAYMOCK = "/pay/payMock";
    public static final String ORDER_ORDERCHARGE = "/order/orderCharge";
    public static final String EVENT_LISTEVENTS = "/event/listEvents";
    public static final String EVENT_GETEVENT = "/event/getEvent";
    public static final String ORDER_ORDEREVENT = "/order/orderEvent";
    public static final String PAY_PAYBYEVENT = "/pay/payByEvent";
    public static final String PAY_PAYAEROBICBYEVENT = "/pay/payAerobicByEvent";
    public static final String PAY_WEIXINPREPAY = "/pay/weixinPrePay";


    //活动
    public static final String EVENT_GETUSEFULLEVENTS = "/event/getUsefullEvents";
    public static final String EVENT_LISTUSEREVENT = "/event/listUserEvents";
    public static final String DYNAMIC_GETDYNAMICLIST = "/dynamic/getDynamicList";
    public static final String DYNAMIC_ADDDYNAMIC = "/dynamic/addDynamic";
    public static final String DYNAMIC_GOOD = "/dynamic/good";

    //淋浴
    public static final String TBEVENTRECORD_GETSHOWERRECORD = "/tbEventRecord/getShowerRecord";
    public static final String ORDER_ORDERSHOWER = "/order/orderShower";

    //实体卡
    public static final String EVENT_CARDBINDEVENT = "/event/cardBindEvent";
    public static final String EVENT_CARDCHECK = "/event/cardCheck";
    public static final String PAY_CARDPAY = "/pay/cardPay";


    //消息
    public static final String MESSAGE_DEL = "/message/del";
    public static final String MESSAGE_LIST = "/message/list";
    public static final String COACH_RECEIVESUBSTITUTECOACH = "/coach/receiveSubstituteCoach";
    public static final String COACH_REJECTSUBSTITUTECOACH = "/coach/rejectSubstituteCoach";
    public static final String CLASSIF_INVITEFRIENDS = "/ClassIf/inviteFriends";


    //v.1.0.3
    public static final String AD_ADFIRST = "/ad/adFirst";
    public static final String AD_ADSNAP = "/ad/adSnap";
    public static final String EVENT_GETMONTHSELLBOARD = "/event/getMonthSellBoard";

    public static final String USER_SHAREFRIENDLIST = "/User/shareFriendList";
    public static final String SMS_SMSSend = "/SMS/SMSSend";
    public static final String EVENT_SHAREEVENTUSER = "/event/shareEventUser";
    public static final String ORDER_ORDERMONTHSELL = "/order/orderMonthSell";
    public static final String CLASSIF_LISTPRIVATEVALUEBLECLASSIFICATION = "/ClassIf/listPrivateValuebleClassification";
    public static final String CLASSIF_LISTCOACHESBYVENUEIDANDCOURSETYPECODE = "/ClassIf/listCoachesByVenueIdAndCourseTypeCode";
    public static final String EVENT_ACCEPTSHAREDEVENTUSER = "/event/acceptSharedEventUser";
    public static final String EVENT_SHARESUCCESSCALLBACK = "/event/shareSuccessCallBack";

    //v1.0.5
    public static final String EVENT_GETAVAILABLECASHEVENT = "/event/getAvailableCashEvent";
    public static final String EVENT_GETSHAREDMAINPAGE = "/event/getSharedMainPage";


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
        public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAL3a+jgh7Il/WdbVmEwdhk2IrH5SyV/BpcY1nXWZfgjJGRprLu5QR4yydRfBNNimgKUhqXNMeS0IBTDurQz22iIeb2/LPsMmWuQ3UJf/J7irH/l7XNTv8/iI6qcDXiSStsVGAX3t/w3iB/F+cpcq6c46Q1lEb0Z5tL2+n6rGZT/VAgMBAAECgYAP/+tEXb+EWuIkU73Q9FAEKrd2sd1cn4Ir8QrDBVt0vBXMfynmVYeiGMv7Y4HjnCyNPgf8un4fyGQ/mm2lAQyi3DUkHDXv4VtcOiRfIeHlEOvBfNQB0Hq/vhYHSlZBaIy7f9mas+A8WPRuwsoAe/yFLv5/KWSSu2rvRXvC7kz3hQJBAOwAODddiPZBzDQXz/jpQSvLSyaZkSQqmc92xAusQUYJkh0CU07InGbgwgoF4GFOsBh+7qhxrqGAvtVxXFHuic8CQQDN8a6jf4OMgxKoq1mOhDp1+dv126mPyA/Xc+pd69bXB5VOThPjKsCYLetme9u69WccoxUYgNrqb0s7i9MoIJkbAkA4w1jCIFI4AN1vziZslRXixbPepeMeEs0J9GP09sqAYP7u0UdeVKwhpn+lT4KHhRif26/lfepl2ASObASrhDdLAkEAyVS9Wv881nAJHLWu7qGcGVuzyq39HSADRKkAdOMre7nhH9U6oflYoS07FUx+qk5giGFyLStDWK6GyObP/kmoAwJARSl7Yf6Fw3NK9W2HI0QQfv0VIgGK8AybDL+lddF/vJmKMmfvIJ2xTbyyempahlPT0mmZwLpWMfv+nwMrV3R3uA==";        // 支付宝公钥
        public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
        public static final int SDK_PAY_FLAG = 1;
    }


    public static class WXPay {
        //appid
        //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wx275d8ba3c5fccaf5"/>为新设置的appid
        public static final String APP_ID = "wx081f8efdfee69ab2";
    }

    public static class Net {
        //正式上线地址
//        public static final String URL = "http://139.196.228.98:7098";

        //发布测试的接口
        public static final String URL = "http://139.196.228.98:8098";

        //内部测试接口
//        public static final String URL = "http://123.57.164.115:8098";

        //支付回调
        public static final String ALIPAY_CALLBACK = Net.URL + "/pay/aliCallback";
        public static final String WX_PAY_CALLBACK = Net.URL + "/pay/weixinCallback";

    }
}

package com.smartfit.commons;

/**
 * Created by dengyancheng on 16/2/23.
 * 常量类  主要为一些网络地址     键值对等
 */
public class Constants {


    public final  static  String FRIST_OPEN_APP = "first_open_app";

    /*****首页传入 位置*/
    public final static String FRAGMENT_POSITION = "positon";


    public final static String PASS_STRING = "pass_string";

    public final static String PASS_OBJECT = "pass_object";
    /***
     * //定义  1 ：团体课  2.小班课  3.私教课
     */
    public static final String PAGE_INDEX = "page_index";


    public static final String CITY_CODE = "city_code";
    public static final String CITY_LONGIT = "city_longit";
    public static final String CITY_LAT = "city_lat";


    public static final String CITY_LIST_INOF = "city_list";
    public static final String CITY_NAME =  "city_name";

    public static final String VENUE_LIST_INFO = "venue_list_info";//场馆列表信息

    public static final String VENU_ID = "venu_id";

    public static final String COURSE_TYPE = "course_type";
    public static final String COURSE_ID = "course_id";
    public static final String COURSE_MONEY = "course_pay";

    public static final String ORDER_TIME = "order_time";






    public static final String UID = "uid";//用户id
    public static final String ACCOUNT = "account";//账号
    public static final String PASSWORD  = "password";
    public static final String SID = "sid";//会话id
    public static final String IS_ICF = "is_icf";//是否教练认证
    public static final String OPEN_COACH_AUTH = "open_coach_auth";


    /**
     * 接口域名方法
     */

    //账户相关接口
    public static final String REGISTER_METHOD = "/User/RegUser";
    public static final String LOGIN_IN_METHOD = "/User/Login";
    public static final String SMS_SMSSEND = "/SMS/SMSSend";
    public static final String RESET_PASSOWRD = "/User/ResetPassword";
    public static final String USER_CONTACTCOURSELIST= "/User/myContactCourseList";

    //课程相关接口
    public static final String SEARCH_CLASS = "/ClassIf/searchInfo";
    public static final String SEARCH_CLASS_DETAIL = "/ClassIf/getClassifInfo";
    public static final String CLASS_COMMEND = "/Comment/getCommentList";
    public static final String GET_VENUElIST = "/ClassIf/getVenueList";
    public static final String GET_CLASS_LIST = "/ClassIf/getClassifList";


    //用户信息
    public static final String MAIN_PAGE_INFO = "/User/mainPage";
    public static final String USER_USERINFO = "/User/userInfo";
    public static final String USER_CONCERNLIST ="/User/concernList";
    public static final String USER_FANSLIST  = "/User/fansList";
    public static final String USER_SEARCHFRIENDLIST = "/User/searchFriendList";
    public static final String USER_ADDFOCUS ="/User/addFocus";
    public static final String USER_ADDFRIEND = "/User/addFriend";

    //教练
    public static final String COACH_ADD_CERTIFICATE = "/coach/addCertificate";
    public static final String COACH_LISTCERTIFICATE = "/coach/listCertificate";
    public static final String COACH_GETCOACHINFO = "/coach/getCoachInfo";
    public static final String  COACH_GETRESUME = "/coach/getResume";
    public static final String COACH_UPDATERESUME = "/coach/updateResume";
    public static final String GET_COACHPAGEINFO  = "/coach/getCoachPageInfo";
    public static final String WORKSPACE_LIST ="/workspace/List";
    public static final String WORKSPACE_DELETE = "/workspace/Delete";
    public static final String  WORKSPACE_ADD = " /workspace/Add";


    //系统数据
    public static final String GET_CITY_LIST = "/sys/dict/listCities";
    public static final String UPLOAD_PHOTOS = "/sys/upload/uploadPic";


    //订单
    public static final String ORDER_ORDERCOURSE = "/order/orderCourse";
    public static final String PAY_BALANCEPAY = "/pay/balancePay";
    public static final String PAY_PAYMOCK = "/pay/payMock";




    /*****
     * 支付宝支付参数
     */
    public static class AliPay {
        // 商户PID
        public static final String PARTNER = "2088021690798954";
        // 商户收款账号
        public static final String SELLER = "zhiniao@goodrobin.com";
        // 商户私钥，pkcs8格式
        public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKeSRx67y34xhLmzalrwERR3k/1/v+XIAuNP/Nmv5rHh95VPvL9WDV9FFoeLMULe1w6P8f9uJz3+LRgFhKgESG1vZyKEsJ4S7utAzZdskKoPyAkw8EjjP3p9IOhM9f7UBHECVmeGrn11MW8Pe2rH8hHbTX92Y/D3dP4wDEgL8VuLAgMBAAECgYBFbB2YxvZ8NmmlQYZLXG/HCe8+s4E94goIQGghi3VItrQKoLkyj6UElivKRclHyehQuSPpzanmTfpCnG6j6PpT6IVcRMM5axrnKAosM5iuHCTbGwE/+XUS7EpclyTuYU4BSoyFO4zkf4ZY3cGx3XefckkFaE5hwUXCTflbw/4N6QJBANVgdljKqdTVWSd10SYQMIjllFErm6Ya6fFMOOHcYhkq4WCbixsWj1raGFHh8Fis7+80zVXQhbp5eFYXkD2XVT8CQQDJC3IKxTmk/iiq0OJw9ewIAvu+h1CJwqmOqCBGlkYzNEdkk6rybGWnz8TBGgr3aDyPMMoGquouRNEHgmgB2mq1AkEAwKdLs3eoR0YqLPqvUBKl/7RpabBZ5GvAdeFAKo109QxGWZeWzWpdHMZ11tiMzUO3jt651pVjaK5r/C9iSHnuuQJBAMfbhu1KDvoEZ20X5mOhNZjpTlWjst6gE2rrm9pP/U1dxTMo0SHZvFzMYK4OM6SrjLhUKiHiupZwdtPeQYLGHJkCQCaPAg7UruzSVXOl17VSY0FNWAMCxMe62MVzuAZN4YTYp7JgvEGXTp4OAs08ZhXwf+j9kPK494xe7tptFGOAu7M=";
        // 支付宝公钥
        public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
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
        //普通接口
//        public static final String URL = "http://222.77.181.80:8080/smartFit";//接口域名
        public static final String URL =  "http://123.57.164.115:8098";



        //支付回调
        public static final String ALIPAY_CALLBACK = Net.URL+"/Pay/aliCallback";
        public static final String WX_PAY_CALLBACK = Net.URL+"/Pay/weixinCallback";

    }
}

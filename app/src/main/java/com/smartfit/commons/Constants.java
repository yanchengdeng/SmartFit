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
    /***
     * //定义  1 ：团体课  2.小班课  3.私教课
     */
    public static final String PAGE_INDEX = "page_index";

    public static final String UID = "uid";//用户id
    public static final String ACCOUNT = "account";//账号
    public static final String SID = "sid";//会话id


    /**
     * 接口域名方法
     */

    //账户相关接口
    public static final String REGISTER_METHOD = "/User/RegUser";
    public static final String LOGIN_IN_METHOD = "/User/Login";
    public static final String SMS_SMSSEND = "/SMS/SMSSend";

    //课程相关接口
    public static final String SEARCH_CLASS = "/ClassIf/searchInfo";
    public static final String SEARCH_CLASS_DETAIL = "/ClassIf/getClassifInfo";
    public static final String CLASS_COMMEND = "/Comment/getCommentList";


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
        public static final String URL = "http://222.77.181.80:8080/smartFit";//接口域名

        //支付回调
        public static final String ALIPAY_CALLBACK = "http://120.76.65.142:8015/api.php";
        public static final String WX_PAY_CALLBACK = "http://120.76.65.142:8015/wxapi.php";

    }
}

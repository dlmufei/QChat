package com.tencent.qchat.constant;

/**
 * Created by hiwang on 16/8/21.
 * <p/>
 * 对整个APP的全局设置
 */

public class Config {

    public static final boolean DEBUG=true; //debug模式

    public static final String QQ_APP_ID = "1105552737";    //QQ登录id
    public static final String WECHAT_APP_ID="wxcc3aab7e40034c31";  //微信登录
    public static final String WECHAT_SECRET="d329544fda5ceebe7d3cc57e24e7a9ca";    //微信登录

    public static final String DEVICE_TYPE = "android";

    public static final String BASE_URL = "https://interesting.geeyan.com/interesting/"; //  服务器地址前缀
    public static final String Q_LIST = "question/list"; //  问题列表
    public static final String LOGIN = "user/login";  //  用户登录

    public static final String MY_A_LIST = "user/my-answered-question-list";  // 获取我回答的问题列表
    public static final String STAFF_LIST="user/staff-list"; //获取回答者列表
    public static final String MY_Q_LIST="user/my-question-list"; //获取我的提问列表
    public static final String Q_ADD="question/add";//增加一个提问
    public static final String A_ADD="question/answer";//对提问增加一条回答


    public static final String MSG_LIST = "notification/list"; // 获取嘉宾用户的通知列表 TODO
    public static final String MSG_USER="notification/list";//获取普通用户的通知列表
    public static final String MSG_COUNT="notification/count";//获取通知数量

    //消息通知的类型
    public static final String NOTI_NEW_Q="new_question";//有新的问题邀请你回答
    public static final String NOTI_NEW_A="new_answer";//你的问题有了新的答案

    //通知轮询时间设定
    public static final int MSG_PULL_INTEVAL=5;


}

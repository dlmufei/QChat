package com.tencent.qchat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hiwang on 16/8/21.
 */

public class TimeUtil {

    private static final int SEC_PER_MIN = 60;
    private static final int SEC_PER_HOUR = SEC_PER_MIN * 60;
    private static final int SEC_PER_DAY = SEC_PER_HOUR * 24;
    private static final int SEC_PER_MON = SEC_PER_DAY * 30;
    private static final int SEC_PER_YEAR = SEC_PER_MON * 12;

    /**
     * 根据毫秒时间换成对应的字符串
     */
    public static String msecToString(long msec) {
        long sec = msec / 1000;
        String result = "太久远";
        if (sec < SEC_PER_MIN) {
            result = "一分钟前";
        } else if (sec < SEC_PER_HOUR) {
            result = sec / SEC_PER_MIN + "分钟前";
        } else if (sec < SEC_PER_DAY) {
            result = sec / SEC_PER_HOUR + "小时前";
        } else if (sec < SEC_PER_MON) {
            result = sec / SEC_PER_DAY + "天前";
        } else if (sec < SEC_PER_YEAR) {
            result = sec / SEC_PER_MON + "月前";
        }
        return result;
    }


    /**
     * 下拉刷新顶部时间字符获取
     */
    public static String currTimeStr(){
        SimpleDateFormat format=new SimpleDateFormat("MM月dd日 hh时mm分");
        Date date=new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * 根据时间判断是否为 "新"
     */
    public static boolean isNewPost(long msec) {
        return msec <= SEC_PER_MIN * 30;
    }
}

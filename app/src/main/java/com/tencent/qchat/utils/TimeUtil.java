package com.tencent.qchat.utils;

/**
 * Created by hiwang on 16/8/21.
 */

public class TimeUtil {

    private static final int SEC_PER_MIN = 60;
    private static final int SEC_PER_HOUR = SEC_PER_MIN * 60;
    private static final int SEC_PER_DAY = SEC_PER_HOUR * 60;
    private static final int SEC_PER_MON = SEC_PER_DAY * 30;
    private static final int SEC_PER_YEAR = SEC_PER_MON * 12;

    /**
     * 根据毫秒时间换成对应的字符串
     */
    public static String msecToString(long msec) {
        long sec = msec / 1000;
        String result = "太久远";
        if (sec < SEC_PER_MIN) {
            result = "刚刚";
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
     * 根据时间判断是否为 "新"
     */
    public static boolean isNewPost(long msec) {
        return msec <= SEC_PER_MIN * 30;
    }
}

package com.tencent.qchat.utils;

import android.content.Context;

/**
 * Created by hiwang on 16/8/27.
 */
public class UserUtil {

    protected static final String TAG = "com.tencent.qchat.utils.UserUtil";
    protected static final String isLogin = "isLogin";
    protected static final String openId = "openId";
    protected static final String nickName = "nickName";
    protected static final String avatar = "avatar";
    protected static final String openType = "openType";
    protected static final String isStaff = "isStaff";
    protected static final String token = "token";

    public static boolean isLogin(Context context) {
        return context.getSharedPreferences(TAG, 0).getBoolean(isLogin, false);
    }

    public static void setIsLogin(Context context, boolean login) {
        context.getSharedPreferences(TAG, 0).edit().putBoolean(isLogin, login).commit();
    }

    public static void setOpenId(Context context, String id) {
        context.getSharedPreferences(TAG, 0).edit().putString(openId, id).commit();
    }

    public static String getOpenId(Context context) {
        return context.getSharedPreferences(TAG, 0).getString(openId, "");
    }

    public static void setNickName(Context context, String nick) {
        context.getSharedPreferences(TAG, 0).edit().putString(nickName, nick).commit();
    }

    public static String getNickName(Context context) {
        return context.getSharedPreferences(TAG, 0).getString(nickName, "");
    }

    public static void setAvator(Context context, String a) {
        context.getSharedPreferences(TAG, 0).edit().putString(avatar, a).commit();
    }

    public static String getAvator(Context context) {
        return context.getSharedPreferences(TAG, 0).getString(avatar, "");
    }

    public static void setOpenType(Context context, String type) {
        context.getSharedPreferences(TAG, 0).edit().putString(openType, type).commit();
    }

    public static boolean getIsStaff(Context context) {
        return context.getSharedPreferences(TAG, 0).getBoolean(isStaff, false);
    }
    public static void setIsStaff(Context context, boolean staff) {
        context.getSharedPreferences(TAG, 0).edit().putBoolean(isStaff, staff).commit();
    }

    public static String getOpenType(Context context) {
        return context.getSharedPreferences(TAG, 0).getString(openType, "");
    }
    public static String getToken(Context context) {
        return context.getSharedPreferences(TAG, 0).getString(token, "");
    }
    public static void setToken(Context context, String to) {
        context.getSharedPreferences(TAG, 0).edit().putString(token, to).commit();
    }

    public static void clear(Context context){
        context.getSharedPreferences(TAG, 0).edit().clear().commit();
    }

}

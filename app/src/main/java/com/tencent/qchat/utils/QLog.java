package com.tencent.qchat.utils;

import android.util.Log;

import com.tencent.qchat.constant.Config;

/**
 * Created by hiwang on 16/9/2.
 * 自定义log
 */
public class QLog {

    protected static final String TAG= QLog.class.toString();

    public static void d(Class clz,String log){
        if(clz==null){
            clz=QLog.class;
        }
        if(Config.DEBUG){
            Log.d(clz.toString(),log);
        }
    }
}

package com.tencent.qchat.utils;

import android.util.Log;

/**
 * Created by hiwang on 16/9/2.
 * 自定义log
 */
public class QLog {

    protected final String TAG= this.getClass().toString();
    protected final boolean DEBUG = true;

    public void d(Class clz,String log){
        if(clz==null){
            clz=this.getClass();
        }
        if(DEBUG){
            Log.d(clz.toString(),log);
        }
    }
}

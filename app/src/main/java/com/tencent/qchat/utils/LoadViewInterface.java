package com.tencent.qchat.utils;

/**
 * Created by hiwang on 2016/7/22.
 * 该接口封装了视图加载过程的是三个时间点
 */
public interface LoadViewInterface {


    /**
     * 给出所用的布局
     */
    int initLayoutRes();

    /**
     * 加载布局文件之前
     */
    void onWillLoadView();

    /**
     * 视图加载成功
     */
    void onDidLoadView();


}

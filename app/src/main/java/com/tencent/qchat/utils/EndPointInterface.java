package com.tencent.qchat.utils;

import com.tencent.qchat.module.QList;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.tencent.qchat.constant.Config.Q_LIST;


/**
 * Created by hiwang on 16/8/21.
 */

public interface EndPointInterface {

    @GET(Q_LIST)
    Call<QList> getQList();
}

package com.tencent.qchat.http;

import com.google.gson.JsonObject;
import com.tencent.qchat.model.Data;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

import static com.tencent.qchat.constant.Config.LOGIN;
import static com.tencent.qchat.constant.Config.Q_LIST;
import static com.tencent.qchat.constant.Config.MSG_LIST;


/**
 * Created by hiwang on 16/8/21.
 */

public interface EndPointInterface {

    @GET(Q_LIST)
    Observable<HttpResult<Data>> getQList();

    @GET(MSG_LIST)
    Observable<HttpResult<Data>> getMSGList();

    @FormUrlEncoded
    @POST(LOGIN)
    Observable<HttpResult<JsonObject>> login(@Field("nickname") String nick, @Field("avatar") String avatar,
                           @Field("open_type") String open_type, @Field("open_id") String open_id,
                           @Field("device_type") String device_type);


}

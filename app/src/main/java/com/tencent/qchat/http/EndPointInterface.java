package com.tencent.qchat.http;

import com.google.gson.JsonObject;
import com.tencent.qchat.model.Data;
import com.tencent.qchat.model.MsgData;
import com.tencent.qchat.model.QList;
import com.tencent.qchat.model.StaffData;
import com.tencent.qchat.model.StaffMsgData;

import java.util.ArrayList;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.tencent.qchat.constant.Config.LOGIN;
import static com.tencent.qchat.constant.Config.MSG_USER;
import static com.tencent.qchat.constant.Config.MY_A_LIST;
import static com.tencent.qchat.constant.Config.MY_Q_LIST;
import static com.tencent.qchat.constant.Config.Q_ADD;
import static com.tencent.qchat.constant.Config.Q_LIST;
import static com.tencent.qchat.constant.Config.MSG_LIST;
import static com.tencent.qchat.constant.Config.STAFF_LIST;
import static com.tencent.qchat.constant.Config.A_ADD;


/**
 * Created by hiwang on 16/8/21.
 */

public interface EndPointInterface {

    //问题列表信息
    @GET(Q_LIST)
    Observable<HttpResult<Data>> getQList();

    //回答者通知列表信息
    @GET(MSG_LIST)
    Observable<HttpResult<StaffMsgData>> getStaffMsgList(@Query("token") String token);

    //获取邀请回答者
    @GET(STAFF_LIST)
    Observable<HttpResult<StaffData>> getStaffList();

    //普通用户通知列表信息
    @GET(MSG_USER)
    Observable<HttpResult<Data>> getUserMsgList(@Query("token") String token);

    @GET(MY_Q_LIST)
    Observable<HttpResult<Data>> getMyQuesList(@Query("token") String token);

    @GET(MY_A_LIST)
    Observable<HttpResult<Data>> getMyAnswerList(@Query("token") String token);


    //登录
    @FormUrlEncoded
    @POST(LOGIN)
    Observable<HttpResult<JsonObject>> login(@Field("nickname") String nick, @Field("avatar") String avatar,
                                             @Field("open_type") String open_type, @Field("open_id") String open_id,
                                             @Field("device_type") String device_type);

    //增加一个提问
    @FormUrlEncoded
    @POST(Q_ADD)
    Observable<HttpResult<JsonObject>> addQuestion(@Field("token") String token, @Field("content") String content,
                                                   @Field("staff_id[]") ArrayList<Integer> staffIds);

    //对提问增加一条回答
    @FormUrlEncoded
    @POST(A_ADD)
    Observable<HttpResult<JsonObject>> addAnswer(@Field("token") String token,  @Field("question_id") int question_id,
                                                 @Field("answer_type") String answer_type , @Field("content") String content);


}

package com.tencent.qchat.http;

import com.google.gson.JsonObject;
import com.tencent.qchat.constant.Config;
import com.tencent.qchat.model.Data;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hiwang on 16/8/21.
 */

public class RetrofitHelper {

    static RetrofitHelper mInterface = new RetrofitHelper();

    private Retrofit mRetrofit;
    private EndPointInterface mEndPointInterface;

    protected RetrofitHelper() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        mEndPointInterface = mRetrofit.create(EndPointInterface.class);
    }

    public static RetrofitHelper getInstance() {
        return mInterface;
    }

    /**
     * 用户登录
     */
    public void login(String nick, String avatar, String type, String openid, Subscriber<JsonObject> subscriber) {
        mEndPointInterface.login(nick, avatar, type, openid, Config.DEVICE_TYPE)
                .map(new HttpResultFilter<JsonObject>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取问题列表
     */
    public void getQList(Subscriber<Data> subscriber) {
        mEndPointInterface.getQList()
                .map(new HttpResultFilter<Data>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取通知列表
     */
    public void getMSGList(Subscriber<Data> subscriber){
        mEndPointInterface.getQList()
                .map(new HttpResultFilter<Data>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    protected class HttpResultFilter<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.code != 0) {
                throw new HttpException(httpResult.code, httpResult.message);
            }
            return httpResult.data;
        }
    }

    protected class HttpException extends RuntimeException {
        public HttpException(int code, String msg) {
            super(code + ":" + msg);
        }
    }
}

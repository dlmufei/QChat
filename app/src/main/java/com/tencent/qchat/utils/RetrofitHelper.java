package com.tencent.qchat.utils;

import com.tencent.qchat.constant.Config;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hiwang on 16/8/21.
 */

public class RetrofitHelper {

    private static Retrofit mRetrofit;
    private static EndPointInterface mEndPointInterface;

    public static EndPointInterface getEndPointInterface() {
        if (mEndPointInterface == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            mEndPointInterface = mRetrofit.create(EndPointInterface.class);
        }
        return mEndPointInterface;
    }
}

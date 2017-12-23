package com.iu33.crudbukulanjutan.network;

import com.iu33.crudbukulanjutan.helper.MyConstant;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hp on 12/10/2017.
 */

public class MyRetrofitClient {
    private static Retrofit getRetrofit() {
        //insialisasi retrofit 2
        Retrofit r = new Retrofit.Builder()
                .baseUrl(MyConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return r;
    }

    public static ResApi getInstaceRetrofit() {
        return getRetrofit().create(ResApi.class);
    }

}
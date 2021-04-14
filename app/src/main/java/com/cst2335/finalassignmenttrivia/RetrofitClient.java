package com.cst2335.finalassignmenttrivia;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retro = null;

    public static Retrofit getClient(String baseUrl){
        if (retro==null) {
            retro = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retro;
    }
}

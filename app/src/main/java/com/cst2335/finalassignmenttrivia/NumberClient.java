package com.cst2335.finalassignmenttrivia;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class NumberClient {

    private static Retrofit retro = null;

    public static Retrofit getClient(String baseUrl){
        if (retro==null) {
            retro = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retro;
    }
}

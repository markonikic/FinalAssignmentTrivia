package com.cst2335.finalassignmenttrivia;


public class ApiUrl {

    public static final String BASE_URL = "https://opentdb.com/";

    public static ApiHelper getAPIService(){
        return RetrofitClient.getClient(BASE_URL).create(ApiHelper.class);
    }

}

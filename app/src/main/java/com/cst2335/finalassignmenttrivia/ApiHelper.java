package com.cst2335.finalassignmenttrivia;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiHelper {
    @GET("api.php?amount=3/")
    Call<TriviaList> getQuestion(@Query("amount") String amount,
                                 @Query("category") String calegory,
                                 @Query("difficulty") String difficulty,
                                 @Query("type") String type);
}

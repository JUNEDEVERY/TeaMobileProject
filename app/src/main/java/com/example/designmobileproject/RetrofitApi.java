package com.example.designmobileproject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
public interface RetrofitApi {

    @POST("user/login")
    Call<mUser> User(@Body mSend mSendUser);
}

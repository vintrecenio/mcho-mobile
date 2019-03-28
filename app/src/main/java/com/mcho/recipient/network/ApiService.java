package com.mcho.recipient.network;

import com.mcho.recipient.model.DeductionResponse;
import com.mcho.recipient.model.LoginResponse;
import com.mcho.recipient.model.Stocks;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @POST("rxlogin")
    @FormUrlEncoded
    Single<LoginResponse> loginCredential(@Field("username") String username, @Field("password") String password);

    @POST("rxlogout")
    @FormUrlEncoded
    Single<String> logoutCredential(@Field("user") String user, @Field("token") String token);

    @POST("rxinventory")
    @FormUrlEncoded
    Single<ArrayList<Stocks>> getStocks(@Field("recipient") String recipient);

    @POST("rxdeductrecipientstock")
    @FormUrlEncoded
    Single<DeductionResponse> deductStocks(@Field("user") String user, @Field("item_id") String item_id, @Field("recipient") String recipient, @Field("remarks") String remarks, @Field("qty") int qty);

/*
    @POST("share")
    @FormUrlEncoded
    Single<String> getShareLink(@Field("device") String deviceId, @Field("id") String id, @Field("type") String type, @Field("md5") String hash);
*/

}

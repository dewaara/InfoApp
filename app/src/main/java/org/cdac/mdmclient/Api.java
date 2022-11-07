package org.cdac.mdmclient;

import org.cdac.mdmclient.ModelResponse.DeviceLock;
import org.cdac.mdmclient.ModelResponse.LoginResponse;
import org.cdac.mdmclient.ModelResponse.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    @FormUrlEncoded
    @POST("/auth/login")
    Call<RegisterResponse> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("deviceId") String deviceId
    );


    @FormUrlEncoded
    @POST("/auth/login")
    Call<LoginResponse> Login(
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("/")
    Call<DeviceLock> RemoteLock(
           // @Field("deviceId") String deviceId,
            @Field("isDeviceLock") boolean isDeviceLock
    );





}

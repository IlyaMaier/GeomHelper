package com.example.geomhelper;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserService {

    @FormUrlEncoded
    @POST("/post")
    Call<String> createUser(@Field("email") String email,
                            @Field("password") String password,
                            @Field("name") String name);

    @FormUrlEncoded
    @POST("/get")
    Call<String> login(@Field("email") String email,
                            @Field("password") String password);

    @FormUrlEncoded
    @PUT("/put")
    Call<String> updateUser(@Field("id") String id,
                            @Field("param") String param,
                            @Field("value") String value);

    @GET("/leaders")
    Call<String> getLeaders();
}

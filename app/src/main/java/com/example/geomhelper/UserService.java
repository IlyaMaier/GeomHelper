package com.example.geomhelper;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface UserService {

    @FormUrlEncoded
    @POST("/signUp")
    Call<String> createUser(@Field("email") String email,
                            @Field("password") String password,
                            @Field("name") String name);

    @FormUrlEncoded
    @POST("/login")
    Call<String> login(@Field("email") String email,
                       @Field("password") String password);

    @FormUrlEncoded
    @PUT("/updateUser")
    Call<String> updateUser(@Field("id") String id,
                            @Field("param") String param,
                            @Field("value") String value);

    @GET("/getLeaders")
    Call<String> getLeaders();

    @GET("/getImage")
    Call<String> downloadimage(@Query("id") String id);

    @Multipart
    @POST("/setUserImage")
    Call<String> upload(
            @Part("id") RequestBody id,
            @Part("file") RequestBody file);

}

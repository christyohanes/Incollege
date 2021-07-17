package com.example.incollege.main.network.api

import com.example.incollege.main.model.login.LoginResponse
import com.example.incollege.main.model.pengurus.PengurusResponse
import com.example.incollege.main.model.userdetail.GetUserDetailResponse
import com.example.incollege.main.model.userphoto.EditPhotoResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @FormUrlEncoded
    @POST("Users")
    fun loginClient(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>

    @GET("UsersDetail")
    fun getUserDetail(
        @Query("id") id: String?
    ): Call<GetUserDetailResponse>

    @Multipart
    @POST("UsersDetail")
    fun updateUserPhoto(
        @Part("user_id") userId: String?,
        @Part photo: MultipartBody.Part?,
        @Part("deskripsi") description: String?,
        @Part("visi") Vission: String?,
        @Part("misi") Mission: String?
    ): Call<EditPhotoResponse>

    @FormUrlEncoded
    @PUT("UserDVM")
    fun setUserDVM(
        @Field("id") id: String?,
        @Field("deskripsi") desc: String?,
        @Field("visi") visi: String?,
        @Field("misi") misi: String?
    ): Call<EditPhotoResponse>

    @GET("Pengurus")
    fun getPengurusData(
        @Query("id") id: String?,
        @Query("tj") tj: String?
    ): Call<PengurusResponse>

    @FormUrlEncoded
    @POST("Pengurus")
    fun deletePengurusData(
        @Field("id") id: String?,
        @Field("flag") flag: String?
    ): Call<EditPhotoResponse>

}
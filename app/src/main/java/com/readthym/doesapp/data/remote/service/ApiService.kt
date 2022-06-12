package com.readthym.doesapp.data.remote.service

import com.readthym.doesapp.data.remote.reqres.*
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @POST("auth/login-unsafe")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<ReadthymLoginResponse>

    @POST("auth/register-unsafe")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<RegisterResponse>

    @POST("task-android/store")
    @FormUrlEncoded
    suspend fun saveTask(
        @Field("id_user") id_user: String,
        @Field("title") title: String,
        @Field("desc") desc: String,
        @Field("deadline") deadline: String,
    ): Response<SaveTaskResponse>

    @POST("task-android/{id}/update")
    @FormUrlEncoded
    suspend fun updateTask(
        @Path("id") id: String,
        @Field("title") title: String,
        @Field("desc") desc: String,
        @Field("deadline") deadline: String,
    ): Response<SaveTaskResponse>

    @GET("task-android/user/{id}")
    suspend fun getTaskUserById(
        @Path("id") id: String
    ): Response<TasksResponse>

    @GET("task-android/{id}/delete")
    suspend fun deleteTask(
        @Path("id") id: String
    ): Response<DeleteResponse>

    @GET("task-android/{id}")
    suspend fun detailTaskTask(
        @Path("id") id: String
    ): Response<DetailTaskResponse>


}
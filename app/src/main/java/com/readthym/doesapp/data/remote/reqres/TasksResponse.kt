package com.readthym.doesapp.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class TasksResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val listTask: MutableList<Task>,
    @SerializedName("status_code")
    val statusCode: Int
) {
    data class Task(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("deadline")
        val deadline: String,
        @SerializedName("desc")
        val desc: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("id_user")
        val idUser: Int,
        @SerializedName("title")
        val title: String,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}
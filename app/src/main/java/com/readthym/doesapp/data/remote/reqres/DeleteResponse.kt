package com.readthym.doesapp.data.remote.reqres


import com.google.gson.annotations.SerializedName

data class DeleteResponse(
    @SerializedName("api_code")
    val apiCode: Int,
    @SerializedName("http_response")
    val httpResponse: Int,
    @SerializedName("message_en")
    val messageEn: String,
    @SerializedName("message_id")
    val messageId: String,
    @SerializedName("res_data")
    val resData: ResData,
    @SerializedName("status_code")
    val statusCode: Int
) {
    data class ResData(
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
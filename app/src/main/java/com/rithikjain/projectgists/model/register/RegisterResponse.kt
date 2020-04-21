package com.rithikjain.projectgists.model.register


import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("message")
    val Message: String,
    @SerializedName("status")
    val Status: Int,
    @SerializedName("token")
    val Token: String,
    @SerializedName("user")
    val User: User
)
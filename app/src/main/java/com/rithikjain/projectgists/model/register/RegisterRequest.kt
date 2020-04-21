package com.rithikjain.projectgists.model.register

import com.google.gson.annotations.SerializedName

data class RegisterRequest (
    @SerializedName("email")
    val Email: String,
    @SerializedName("name")
    val Name: String,
    @SerializedName("oauth_token")
    val OAuthToken: String
)
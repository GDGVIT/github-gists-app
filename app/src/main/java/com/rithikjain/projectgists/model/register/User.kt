package com.rithikjain.projectgists.model.register


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("CreatedAt")
    val CreatedAt: String,
    @SerializedName("DeletedAt")
    val DeletedAt: String,
    @SerializedName("email")
    val Email: String,
    @SerializedName("ID")
    val ID: Int,
    @SerializedName("name")
    val Name: String,
    @SerializedName("oauth_token")
    val OAuthToken: String,
    @SerializedName("UpdatedAt")
    val UpdatedAt: String
)
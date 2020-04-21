package com.rithikjain.projectgists.model.gists

import com.google.gson.annotations.SerializedName

data class CreateGistRequest(
    @SerializedName("description")
    val Description: String,
    @SerializedName("public")
    val IsPublic: Boolean,
    @SerializedName("filename")
    val Filename: String,
    @SerializedName("content")
    val Content: String
)
package com.rithikjain.projectgists.model.gists

import com.google.gson.annotations.SerializedName

data class UpdateGistRequest(
    @SerializedName("gist_id")
    val GistID: String,
    @SerializedName("description")
    val Description: String,
    @SerializedName("filename")
    val Filename: String,
    @SerializedName("content")
    val Content: String
)
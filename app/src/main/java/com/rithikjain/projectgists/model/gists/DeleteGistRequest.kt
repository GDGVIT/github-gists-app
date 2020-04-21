package com.rithikjain.projectgists.model.gists

import com.google.gson.annotations.SerializedName

data class DeleteGistRequest(
    @SerializedName("gist_id")
    val GistID: String,
    @SerializedName("filename")
    val Filename: String
)
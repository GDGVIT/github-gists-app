package com.rithikjain.projectgists.model.gists


import com.google.gson.annotations.SerializedName

data class ViewGistsResponse(
    @SerializedName("files")
    val Files: List<File>,
    @SerializedName("message")
    val Message: String,
    @SerializedName("status")
    val Status: Int
)
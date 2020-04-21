package com.rithikjain.projectgists.model.gists


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "gists", primaryKeys = ["GistID", "Filename"])
data class File(
    @SerializedName("content")
    val Content: String,
    @SerializedName("description")
    val Description: String,
    @SerializedName("filename")
    val Filename: String,
    @SerializedName("gist_id")
    val GistID: String,
    @SerializedName("gist_url")
    val GistUrl: String,
    @SerializedName("language")
    val Language: String,
    @SerializedName("public")
    val IsPublic: Boolean,
    @SerializedName("raw_url")
    val RawUrl: String,
    @SerializedName("updated_at")
    val UpdatedAt: String
)
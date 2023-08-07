package com.akb.ownews.data.model

import com.google.gson.annotations.SerializedName

data class SourceModel(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("category")
    val category: String? = null,
    @SerializedName("language")
    val language: String? = null,
    @SerializedName("country")
    val country: String? = null
)
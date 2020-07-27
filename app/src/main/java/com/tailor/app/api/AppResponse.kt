package com.tailor.app.api

import com.google.gson.annotations.SerializedName


/**
 * Created by Soham Robinkumar Sheth on 11/6/18.
 */
data class AppResponse<T>(
    @field:SerializedName("code")
    val code: Int = 0,
    @field:SerializedName("message")
    val message: String? = null,
    @field:SerializedName("data")
    val data: T? = null
)
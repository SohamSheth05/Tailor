package com.tailor.app.api

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface ApiServices {
    @Streaming
    @GET(URLFactory.LOGIN)
    fun downloadFileWithDynamicUrlSync(): Single<ResponseBody>
}
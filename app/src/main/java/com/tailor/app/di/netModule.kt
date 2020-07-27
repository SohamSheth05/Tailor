package com.tailor.app.di

import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tailor.app.api.ApiHeaders
import com.tailor.app.api.URLFactory
import com.tailor.app.custom_exception.AuthenticationException
import com.tailor.app.custom_exception.ServerError


import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.Interceptor.Companion.invoke
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val netModule = module {
    fun provideCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(application.cacheDir, cacheSize.toLong())
    }

    fun provideHttpClient(cache: Cache): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .cache(cache)

        return okHttpClientBuilder.build()
    }

    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }


    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(
            client.newBuilder()
                .addInterceptor(headerInterceptor())
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addNetworkInterceptor(networkInterceptor())
                .build()
        )
            .baseUrl(URLFactory.provideHttpUrl())
            .addConverterFactory(GsonConverterFactory.create(factory))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    single { provideCache(androidApplication()) }
    single { provideHttpClient(get()) }
    single { provideGson() }
    single { provideRetrofit(get(), get()) }


}

private fun networkInterceptor(): Interceptor {
    return invoke { chain ->
        val response = chain.proceed(chain.request())
        val code = response.code
        if (code >= 500)
            throw ServerError("Unknown server error", response.body!!.string())
        else if (code == 401 || code == 403)
            throw AuthenticationException()
        response
    }
}

private fun headerInterceptor(): Interceptor {
    return Interceptor { chain ->
        chain.proceed(chain.request().newBuilder().apply {
            header(ApiHeaders.TOKEN, "appSession.getUserToken()")
        }.build())
    }
}

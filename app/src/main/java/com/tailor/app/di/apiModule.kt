package com.tailor.app.di

import com.tailor.app.api.ApiServices
import org.koin.dsl.module
import retrofit2.Retrofit


val apiModule = module {
    single(createdAtStart = false) { get<Retrofit>().create(ApiServices::class.java) }
}
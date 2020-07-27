package com.tailor.app.di


import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
   /* factory { AppSession(get(), named("api-key").value, get()) }
    factory { AppPreferences(androidContext(), named("preferences").value) }
    factory { LocationManager() }
    factory { Validator() }
    factory { FontManager(get()) }*/
}
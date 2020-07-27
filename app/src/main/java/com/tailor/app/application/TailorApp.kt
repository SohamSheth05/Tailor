package com.tailor.app.application

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application

import com.google.firebase.FirebaseApp
import com.tailor.app.di.viewModelModule
import com.tailor.app.di.apiModule
import com.tailor.app.di.appModule
import com.tailor.app.di.netModule

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.util.*


/**
 * Created by Soham Robinkumar Sheth on 2/7/18.
 */
class TailorApp : Application() {

    lateinit var tailorApp: TailorApp


    companion object {
        @SuppressLint("StaticFieldLeak")
        var currentActivity: Activity? = null
        private var activity_List = ArrayList<Activity>()
        fun getInstance(): TailorApp? {
            return TailorApp()
        }
        fun setActivity(mCurrentActivity: Activity) {
            try {
                activity_List.add(mCurrentActivity)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


    }

    override fun onCreate() {
        super.onCreate()

        //Fabric.with(this, Crashlytics())
        // Initialize Places.

        startKoin {
            androidLogger()
            androidContext(this@TailorApp)
            modules(
                listOf(
                    viewModelModule,
                    netModule,
                    appModule,
                    apiModule
                )
            )
        }
// Create a new Places client instance.
        FirebaseApp.initializeApp(applicationContext)
    }

}
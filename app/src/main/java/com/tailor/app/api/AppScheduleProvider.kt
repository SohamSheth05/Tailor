package com.tailor.app.api

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class AppScheduleProvider {
    open fun io(): Scheduler {
        return Schedulers.io()
    }

    open fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}
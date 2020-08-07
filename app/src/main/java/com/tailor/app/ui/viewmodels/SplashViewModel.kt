package com.tailor.app.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tailor.app.api.ApiServices
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.Schedulers.io
import okhttp3.ResponseBody
import retrofit2.Response
import java.net.UnknownHostException

class SplashViewModel constructor(private val apiServices: ApiServices) : ViewModel() {
    var loading = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var errorThrowable = MutableLiveData<Throwable>()
    var loginObserver = MutableLiveData<ResponseBody>()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    fun getLogin() {
        loading.value = true
        compositeDisposable.add(
            apiServices.downloadFileWithDynamicUrlSync()
                .subscribeOn(io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { user ->
                        loading.value = false
                        loginObserver.value = user
                    },
                    { throwable ->
                        loading.value = false
                        errorThrowable.value = throwable
                        if (throwable is UnknownHostException) {
                            errorMessage.value = ""
                        } else {
                            errorMessage.value = throwable.localizedMessage
                        }
                    })
        )
    }
}

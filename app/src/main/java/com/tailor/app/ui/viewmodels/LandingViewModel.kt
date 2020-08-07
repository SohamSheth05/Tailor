package com.tailor.app.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.net.UnknownHostException

class LandingViewModel : ViewModel() {
    /*
    var loading = MutableLiveData<Boolean>()
     var errorMessage = MutableLiveData<String>()
     var errorThrowable = MutableLiveData<Throwable>()
    var loginObserver = MutableLiveData<AppResponse<User>>()

     fun getLogin(parameters: HashMap<String, String>) {
         loading.value = true
         compositeDisposable.add(
             apiServices.callLoginApi1(parameters)
                 .subscribeOn(appScheduleProvider.io())
                 .observeOn(appScheduleProvider.mainThread())
                 .subscribe(
                     { user ->
                         loading.value = false
                         loginObserver.value = user
                     },
                     { throwable ->
                         loading.value = false
                         errorThrowable.value=throwable
                         if (throwable is UnknownHostException) {
                             errorMessage.value = INTERNET_CONNECTION_MESSAGE
                         } else {
                             errorMessage.value = throwable.localizedMessage
                         }
                     })
         )
     }*/
}

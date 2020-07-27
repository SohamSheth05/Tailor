package com.tailor.app.di


import com.tailor.app.ui.viewmodels.LandingViewModel
import com.tailor.app.ui.viewmodels.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel() }
    viewModel { LandingViewModel() }

}
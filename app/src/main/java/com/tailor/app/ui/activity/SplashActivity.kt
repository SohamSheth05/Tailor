package com.tailor.app.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import androidx.core.content.ContextCompat
import com.tailor.app.BR
import com.tailor.app.R
import com.tailor.app.base.BaseActivity
import com.tailor.app.databinding.ActivitySplashBinding
import com.tailor.app.ui.fragments.LandingFragment
import com.tailor.app.ui.viewmodels.SplashViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    lateinit var binding: ActivitySplashBinding
    private val splashViewModel by viewModel<SplashViewModel>()
    override fun getBindingVariable(): Int {
        return BR.viewModel
    }

    override fun getViewModel(): SplashViewModel {
        return splashViewModel
    }

    override fun onReady(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            fragmentNavigationFactory.make(LandingFragment()).add(true)
        },1000)

    }

    override fun findFragmentPlaceHolder(): Int {
        return R.id.container
    }

    override fun findContentView(): Int {
        return R.layout.activity_splash
    }
}
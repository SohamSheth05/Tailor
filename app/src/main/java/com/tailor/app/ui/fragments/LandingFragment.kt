package com.tailor.app.ui.fragments

import com.tailor.app.R
import com.tailor.app.base.BaseFragment
import com.tailor.app.databinding.FragmentLandingPageLayoutBinding
import com.tailor.app.manager.Passable
import com.tailor.app.ui.viewmodels.LandingViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LandingFragment : BaseFragment<FragmentLandingPageLayoutBinding, LandingViewModel>() {
    private val landingViewModel by viewModel<LandingViewModel>()
    override fun onReady() {
        fragmentNavigationFactory.make(LandingFragment()).hasData(object : Passable<LandingFragment>{
            override fun passData(t: LandingFragment) {
                t.setData()
            }

        })
    }

    private fun setData() {


    }

    override fun getViewModel(): LandingViewModel {
        return landingViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_landing_page_layout
    }
}
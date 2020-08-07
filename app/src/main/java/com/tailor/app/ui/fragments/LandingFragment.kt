package com.tailor.app.ui.fragments

import androidx.lifecycle.Observer
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
    /*private fun setMainViewModelObserver() {
        vm.loading.observe(viewLifecycleOwner, Observer {
            if (it != null && it) {
                showLoader()
            }
        })
        vm.errorMessage.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                hideKeyBoard()
                hideLoader()
                showSnackBar(it)
            }
        })
        vm.loginObserver.observe(viewLifecycleOwner, Observer {
            hideLoader()
            if (it != null) {
                when (it.code) {
                    Common.SUCCESS -> {
                        it.data?.sessionToken?.let { it1 -> session.setUserToken(it1) }
                        session.sessionUpdate(it.data)
                        session.getUser()?.id?.let { it -> vm.getStoreList(it) }
                        //intercomUserDetails(it.data)

                    }
                    Common.UNAUTHENTICATED -> {

                        navigationController.logout(Common.UNAUTHENTICATED)
                    }
                    else -> {
                        it.message?.let { it1 -> showSnackBar(it1) }
                    }
                }
            }
        })

        vm.storeListObserver.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            hideLoader()
            if (it != null) {
                when (it.code) {
                    Common.SUCCESS -> {
                        it.data?.get(0)?.merchantId?.let { it1 ->
                            session.setMerchantId(it1)
                        }

                        try {
                            if (it.data != null) {
                                if (it.data[0].getMerchantStores()?.size!! > 1) {
                                    fragmentNavigationFactory.make(UsersStoresListFragment())
                                        .hasData(object :
                                            Passable<UsersStoresListFragment> {
                                            override fun passData(t: UsersStoresListFragment) {
                                                t.setName(session.getUser()?.name, false)
                                            }
                                        }).add(true)
                                } else {
                                    it.data[0].getMerchantStores()?.get(0)?.let { it1 ->
                                        session.setStoresData(
                                            it1
                                        )
                                    }
                                    session.sessionLogin()
                                    fragmentNavigationFactory.make(HomeActivity::class.java)
                                        .byFinishingCurrent().start()
                                }
                            } else {
                                showSnackBar(getString(R.string.invalid_user_login))
                            }
                        } catch (e: Exception) {
                            showSnackBar(e.localizedMessage!!)
                        }

                    }
                    Common.UNAUTHENTICATED -> {
                        navigationController.logout(Common.UNAUTHENTICATED)
                    }
                }
            }
        })
    }*/
    override fun getViewModel(): LandingViewModel {
        return landingViewModel
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_landing_page_layout
    }

    
}
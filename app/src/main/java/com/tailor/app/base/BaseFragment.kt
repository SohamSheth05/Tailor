package com.tailor.app.base

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.tailor.app.custom_exception.AuthenticationException
import com.tailor.app.custom_exception.ServerException
import com.tailor.app.manager.FragmentNavigationFactory
import com.tailor.app.manager.NavigationController
import java.net.ConnectException
import java.net.UnknownHostException


abstract class BaseFragment<T : ViewDataBinding, V : ViewModel> : Fragment(), RootView {

    lateinit var navigationController: NavigationController
    lateinit var fragmentNavigationFactory: FragmentNavigationFactory

    private lateinit var mViewDataBinding: T
    private lateinit var mViewModel: V
    private var mRootView: View? = null
    private lateinit var mActivity: BaseActivity<*, *>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            this.mActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = NavigationController(mActivity)
        fragmentNavigationFactory = FragmentNavigationFactory(mActivity)
        mViewModel = getViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mRootView = mViewDataBinding.root

        return mRootView
    }

    open fun onShow() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onReady()
    }

    private fun performDependencyInjection() {
        /*AndroidSupportInjection.inject(this)*/
    }

    abstract fun onReady()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       /* if (requestCode == NavigationController.REQUEST_CODE_WITH_MESSAGE) {
            if (data != null && data.hasExtra(NavigationController.KEY_MESSAGE))
                showSnackBar(data.getStringExtra(NavigationController.KEY_MESSAGE)!!)
        }*/
    }

    override fun showSnackBar(message: String) {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).showSnackBar(message)
        }
    }

    override fun showSnackBar(message: String, showOk: Boolean) {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).showSnackBar(message, showOk)
        }
    }

    override fun showSnackBar(view: View, message: String, showOk: Boolean) {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).showSnackBar(view, message, showOk)
        }
    }

    override fun showLoader() {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).showLoader()
        }
    }

    override fun hideLoader() {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).hideLoader()
        }
    }

    override fun showKeyBoard() {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).showKeyBoard()
        }
    }

    override fun hideKeyBoard() {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).hideKeyBoard()
        }
    }

    override fun showDialogWithOneAction(
        title: String?,
        message: String?,
        positiveButton: String?,
        positiveFunction: (DialogInterface, Int) -> Unit
    ) {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).showDialogWithOneAction(
                title,
                message,
                positiveButton,
                positiveFunction
            )
        }
    }

    override fun showDialogWithTwoActions(
        title: String?,
        message: String?,
        positiveName: String?,
        negativeName: String?,
        positiveFunction: (DialogInterface, Int) -> Unit,
        negativeFunction: (DialogInterface, Int) -> Unit
    ) {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).showDialogWithTwoActions(
                title,
                message,
                positiveName,
                negativeName,
                positiveFunction,
                negativeFunction
            )
        }
    }

    override fun hideDialog() {
        if (activity is BaseActivity<*, *>) {
            (activity as BaseActivity<*, *>).hideDialog()
        }
    }

    abstract fun getViewModel(): V

    @LayoutRes
    abstract fun getLayoutId(): Int


    fun getBaseActivity(): BaseActivity<*, *> {
        return mActivity
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding
    }




    fun onError(throwable: Throwable) {
        if (activity is BaseActivity<*, *>) {
            when (throwable) {
                is AuthenticationException -> {
                    //(activity as BaseActivity<*, *>).logout(Common.UNAUTHENTICATED)
                }
                is ConnectException -> showSnackBar("OOPS! NO INTERNET. Please check your network connection. Try Again")
                is UnknownHostException -> {
                    Toast.makeText(
                        context,
                        "OOPS! NO INTERNET. Please check your network connection. Try Again",
                        Toast.LENGTH_LONG
                    ).show()
                    //showSnackBar("OOPS! NO INTERNET. Please check your network connection. Try Again")
                }
                is ServerException -> {
                    showSnackBar(throwable.message!!)
                }
                else -> (activity as BaseActivity<*, *>).showSnackBar(throwable.message!!)
            }
        }
    }
}
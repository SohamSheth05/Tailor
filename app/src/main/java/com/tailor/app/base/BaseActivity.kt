package com.tailor.app.base


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.admin.DevicePolicyManager
import android.app.admin.SystemUpdatePolicy
import android.content.*
import android.net.ConnectivityManager
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.internal.service.Common

import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.tailor.app.R

import com.tailor.app.api.ApiServices
import com.tailor.app.manager.FragmentNavigationFactory
import com.tailor.app.manager.NavigationController


import io.reactivex.schedulers.Schedulers

import org.koin.android.ext.android.inject
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket
import java.util.*


abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : AppCompatActivity(),
    RootView {
 /*   val locationManager: LocationManager by inject ()

    val apiServices: ApiServices by inject()

    val appPreferences:AppPreferences by inject()*/

    val gson: Gson by inject()

    protected var toolbar: Toolbar? = null
    private var appBarLayout: AppBarLayout? = null
    private var toolbarTitle: AppCompatTextView? = null
    lateinit var navigationController: NavigationController
    lateinit var fragmentNavigationFactory: FragmentNavigationFactory
    var isBack = false

    private var materialAlertDialog: AlertDialog? = null
    private lateinit var settingsDialog: AlertDialog
    private lateinit var confirmationDialog: AlertDialog


    private lateinit var mViewDataBinding: T
    private var mViewModel: V? = null

    var outJsonKey: String = ""
    lateinit var apk: String
    override fun onCreate(savedInstanceState: Bundle?) {
        StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, findContentView())
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        navigationController = NavigationController(this)
        fragmentNavigationFactory = FragmentNavigationFactory(this)
        this.mViewModel = if (mViewModel == null) getViewModel() else mViewModel
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.executePendingBindings()
        onReady(savedInstanceState)
        hideStatusBar()
    }

    private fun hideStatusBar() { // Hide status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }



    abstract fun getBindingVariable(): Int
    fun getViewDataBinding(): T {
        return mViewDataBinding
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): V


    abstract fun onReady(savedInstanceState: Bundle?)

   /* fun setAnimation() {
        this.overridePendingTransition(R.anim.anim_left, R.anim.anim_right)
    }*/





    fun <F : BaseFragment<*, *>> getCurrentFragment(): androidx.fragment.app.Fragment? {
        return if (findFragmentPlaceHolder() == 0) null else supportFragmentManager.findFragmentById(
            findFragmentPlaceHolder()
        )
    }

    abstract fun findFragmentPlaceHolder(): Int

    @LayoutRes
    abstract fun findContentView(): Int

    override fun showSnackBar(message: String) {
        showSnackBar(message, false)
    }

    override fun showSnackBar(message: String, showOk: Boolean) {
        showSnackBar(findViewById(R.id.container), message, showOk)
    }

    override fun showSnackBar(view: View, message: String, showOk: Boolean) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        //snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.white_color))

        val sbView = snackBar.view
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        val textView = sbView.findViewById<TextView>(R.id.snackbar_text)
        //textView.setTextColor(ContextCompat.getColor(this, R.color.white_color))
        //textView.typeface = ResourcesCompat.getFont(this, R.font.raleway_regular)

        if (showOk)
            snackBar.setAction("Ok") { snackBar.dismiss() }

        snackBar.show()
    }

    override fun showLoader() {
        //AlertUtils.showCustomProgressDialog(this)
    }

    override fun hideLoader() {
        //AlertUtils.dismissDialog()
    }


    override fun showKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun hideKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun showDialogWithOneAction(
        title: String?, message: String?,
        positiveButton: String?,
        positiveFunction: (DialogInterface, Int) -> Unit
    ) {
        showDialogWithTwoActions(title, message, positiveButton, null, positiveFunction) { _, _ -> }
    }

    override fun showDialogWithTwoActions(
        title: String?, message: String?,
        positiveName: String?, negativeName: String?,
        positiveFunction: (DialogInterface, Int) -> Unit,
        negativeFunction: (DialogInterface, Int) -> Unit
    ) {

        materialAlertDialog?.dismiss()
        materialAlertDialog = MaterialAlertDialogBuilder(this).setCancelable(false).setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveName, positiveFunction)
            .setNegativeButton(negativeName, negativeFunction).create()
        materialAlertDialog?.show()

        val textView = materialAlertDialog?.findViewById<TextView>(android.R.id.message)
        if (textView != null) {
            //textView.typeface = ResourcesCompat.getFont(this, R.font.raleway_regular)
        }
    }

    override fun hideDialog() {
        if (materialAlertDialog?.isShowing!!) {
            materialAlertDialog?.dismiss()
        }
    }


    fun setSelectionBackgroundColor(button: AppCompatButton) {
       // button.setBackgroundColor(ContextCompat.getColor(this, R.color.red_color))
    }



    private fun isConnected(): Boolean {
        var connected = false
        try {
            val cm =
                applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val nInfo = cm.activeNetworkInfo
            connected = nInfo != null && nInfo.isAvailable && nInfo.isConnected
            return connected
        } catch (e: Exception) {
            Log.e("Connectivity Exception", e.message!!)
        }
        return connected
    }

    private fun isNetworkOnline(): Boolean {
        var isOnline = false
        try {
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 3000)
            // socket.connect(new InetSocketAddress("114.114.114.114", 53), 3000);
            isOnline = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return isOnline
    }

}
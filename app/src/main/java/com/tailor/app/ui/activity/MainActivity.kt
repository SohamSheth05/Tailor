package com.tailor.app.ui.activity


import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.tailor.app.R
import com.tailor.app.base.BaseActivity
import com.tailor.app.databinding.ActivityMainBinding
import com.tailor.app.ui.viewmodels.SplashViewModel
import okhttp3.ResponseBody
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.*


class MainActivity : BaseActivity<ActivityMainBinding, SplashViewModel>() {
    val splashViewModel by viewModel<SplashViewModel>()
    lateinit var binding : ActivityMainBinding
    override fun getBindingVariable(): Int {
        return 0
    }

    override fun getViewModel(): SplashViewModel {
        return splashViewModel
    }

    override fun onReady(savedInstanceState: Bundle?) {
        binding=getViewDataBinding()
        binding.downloadMp3.setOnClickListener {
            splashViewModel.getLogin()
        }
        setMainViewModelObserver()
    }

    override fun findFragmentPlaceHolder(): Int {
        return R.id.container
    }

    override fun findContentView(): Int {
        return R.layout.activity_main
    }

    private fun setMainViewModelObserver() {
        splashViewModel.loading.observe(this, Observer {
            if (it != null && it) {
                showLoader()
            }
        })
        splashViewModel.errorMessage.observe(this, Observer {
            if (it != null) {
                hideKeyBoard()
                hideLoader()
                showSnackBar(it)
            }
        })
        splashViewModel.loginObserver.observe(this, Observer {
            hideLoader()
            if (it != null) {
                binding.pBar3.visibility= View.VISIBLE
                val writtenToDisk: Boolean = writeResponseBodyToDisk(it)
                if(writtenToDisk){
                    binding.pBar3.visibility=View.GONE
                    showSnackBar("File Downloaded Successfully")
                }

            }
        }
        )
    }

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            // todo change the file location/name according to your needs
            val futureStudioIconFile =
                File(getExternalFilesDir(null) ,File.separator.toString() + "demo.mp3")
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()

                    Log.d(
                        "FragmentActivity.TAG",
                        "file download: $fileSizeDownloaded of $fileSize"
                    )
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }
}
package com.tailor.app.utils

import android.content.Context
import android.os.AsyncTask
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.tailor.app.R
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadTask(
    private val context: Context,
    private val buttonText: Button,
    downloadUrl: String
) {
    private var downloadUrl = ""
    private var downloadFileName = ""

    private inner class DownloadingTask :
        AsyncTask<Void?, Void?, Void?>() {
        var apkStorage: File? = null
        var outputFile: File? = null
        override fun onPreExecute() {
            super.onPreExecute()
            buttonText.isEnabled = false
            buttonText.setText(R.string.downloadStarted) //Set Button Text when download started
        }

        override fun onPostExecute(result: Void?) {
            try {
                if (outputFile != null) {
                    buttonText.isEnabled = true
                    buttonText.setText(R.string.downloadCompleted) //If Download completed then change button text
                } else {
                    buttonText.setText(R.string.downloadFailed) //If download failed change button text
                    Handler().postDelayed({
                        buttonText.isEnabled = true
                        buttonText.setText(R.string.downloadAgain) //Change button text again after 3sec
                    }, 3000)
                    Log.e(TAG, "Download Failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()

                //Change button text if exception occurs
                buttonText.setText(R.string.downloadFailed)
                Handler().postDelayed({
                    buttonText.isEnabled = true
                    buttonText.setText(R.string.downloadAgain)
                }, 3000)
                Log.e(
                    TAG,
                    "Download Failed with Exception - " + e.localizedMessage
                )
            }
            super.onPostExecute(result)
        }

        override fun doInBackground(vararg arg0: Void?): Void? {
            try {
                val url = URL(downloadUrl) //Create Download URl
                val c =
                    url.openConnection() as HttpURLConnection //Open Url Connection
                c.requestMethod = "GET" //Set Request Method to "GET" since we are grtting data
                c.connect() //connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(
                        TAG, "Server returned HTTP " + c.responseCode
                                + " " + c.responseMessage
                    )
                }


                //Get File if SD card is present
                if (CheckForSDCard().isSDCardPresent) {
                    apkStorage = File(
                        context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "/"
                                + Utils.downloadDirectory
                    )
                } else Toast.makeText(context, "Oops!! There is no SD Card.", Toast.LENGTH_SHORT)
                    .show()

                //If File is not present create directory
                if (!apkStorage!!.exists()) {
                    apkStorage!!.mkdir()
                    Log.e(TAG, "Directory Created.")
                }
                outputFile =
                    File(apkStorage, downloadFileName) //Create Output file in Main File

                //Create New File if not present
                if (!outputFile!!.exists()) {
                    outputFile!!.createNewFile()
                    Log.e(TAG, "File Created")
                }
                val fos =
                    FileOutputStream(outputFile) //Get OutputStream for NewFile Location
                val `is` = c.inputStream //Get InputStream for connection
                val buffer = ByteArray(1024) //Set buffer type
                var len1 = 0 //init length
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    fos.write(buffer, 0, len1) //Write new file
                }

                //Close all connection after doing task
                fos.close()
                `is`.close()
            } catch (e: Exception) {

                //Read exception if something went wrong
                e.printStackTrace()
                outputFile = null
                Log.e(
                    TAG,
                    "Download Error Exception " + e.message
                )
            }
            return null
        }
    }

    companion object {
        private const val TAG = "Download Task"
    }

    init {
        this.downloadUrl = downloadUrl
        downloadFileName = downloadUrl.replace(
            Utils.mainUrl,
            ""
        ) //Create file name by picking download file name from URL
        Log.e(TAG, downloadFileName)

        //Start Downloading Task
        DownloadingTask().execute()
    }
}
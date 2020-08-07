package com.tailor.app.utils

import android.os.Environment

class CheckForSDCard {
    //Check If SD Card is present or not method
    val isSDCardPresent: Boolean
        get() = if (Environment.getExternalStorageState() ==
            Environment.MEDIA_MOUNTED
        ) {
            true
        } else false
}
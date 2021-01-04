package com.isoftinc.film.util

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class AndroidUtilities {

    var linkPatterns = "#[/^#[A-Za-z0-9_-]*$/]+"



    fun isAndroid5(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    }

    fun hideKeyboard(view: View?) {
        if (view == null) {
            return
        }
        try {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (!imm.isActive) {
                return
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {

        }

    }

    fun getSavedBitmapPath(filename: String, context: Context, bmp: Bitmap): File? {
        try {
            val mediaStorageDir = File(context.cacheDir, "Downloads")

            if (!mediaStorageDir.exists()) {
                Log.d("check++", "checkkkk")
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("check++++", "checkkkk")
                    return null
                }
            }

            val mediaFile: File
            if (!TextUtils.isEmpty(filename)) {
                mediaFile = File(mediaStorageDir.path + File.separator + filename + ".jpeg")
            } else {
                return null
            }
            try {
                val out = FileOutputStream(mediaFile)
                bmp.compress(Bitmap.CompressFormat.JPEG, 85, out)
                out.flush()
                out.close()
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

            return mediaFile
        } catch (e: Exception) {

            e.printStackTrace()
            return null
        }

    }

    fun hasActiveInternetConnection(context: Context?): Boolean {
        if (isNetworkAvailable(context)) {
            try {
                val urlc: HttpURLConnection =
                    URL("http://www.google.com").openConnection() as HttpURLConnection
                urlc.setRequestProperty("User-Agent", "Test")
                urlc.setRequestProperty("Connection", "close")
                urlc.connectTimeout = 1500
                urlc.connect()
                return urlc.responseCode == 200
            } catch (e: IOException) {
                Log.e("Films", "Error checking internet connection", e)
            }
        } else {
            Log.d("Films", "No network available!")
        }
        return false
    }

    private fun isNetworkAvailable(context: Context?): Boolean {
        val connectivityManager =
           context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null
    }

    fun secToTime(sec: Int?): String? {
        val seconds = sec!! % 60
        var minutes = sec / 60
        if (minutes >= 60) {
            val hours = minutes / 60
            minutes %= 60
            if (hours >= 24) {
                val days = hours / 24
                return String.format("%d days %02d:%02d:%02d", days, hours % 24, minutes, seconds)
            }
            return String.format("%02d Hrs:%02d Min:%02d Sec", hours, minutes, seconds)
        }
        return String.format("%02d Min:%02d Sec", minutes, seconds)
    }


}

package com.deepak.movieinfo.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager

import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.*

object AppUtils {


    //    public static String BASE_API_KEY = "ee1345c4";   //Temporary Key

    var BASE_API_KEY = "9cfa95f8"     //Given Key

    var BASE_API_URL = "http://www.omdbapi.com/?apikey=$BASE_API_KEY"

    val MY_PERMISSIONS_REQUEST_INTERNET = 100

    fun getSearchURL(searchtoken: String, pageNo: Int): String {
        return "$BASE_API_URL&s=$searchtoken&page=$pageNo"
    }

    fun getTitleInformationURL(titleId: String): String {
        return "$BASE_API_URL&i=$titleId"
    }

    fun setAPIKey(apiKey: String) {
        BASE_API_KEY = apiKey
    }

    fun checkInternetPermission(activity: AppCompatActivity, permissiom: String): Boolean {
        if (ContextCompat.checkSelfPermission(activity, permissiom) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissiom)) {

            } else {
                if (permissiom === Manifest.permission.INTERNET) {
                    ActivityCompat.requestPermissions(activity, arrayOf(permissiom), MY_PERMISSIONS_REQUEST_INTERNET)
                }
            }
        } else {
            return true
        }
        return false
    }


    fun httpGetStringRequest(urlString: String): String? {

        var jsonData: String? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(urlString)
            urlConnection = url.openConnection() as HttpURLConnection
            val `in` = BufferedInputStream(urlConnection.inputStream)
            jsonData = convertInputStreamToString(`in`)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            urlConnection?.disconnect()
        }

        return jsonData
    }

    private fun convertInputStreamToString(inputStream: InputStream): String {
//        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
//        bufferedReader.readLine()
//        var line = ""
//        var result = ""
//        while ((line = bufferedReader.readLine()) != null)
//            result += line
//
//        inputStream.close()
//
        val inputString = inputStream.bufferedReader().use { it.readText() }
        return inputString

    }

    fun runRecyclerAnimation(context: Context, view: RecyclerView, animID: Int) {
        val animationController = AnimationUtils.loadLayoutAnimation(context, animID)
        view.layoutAnimation = animationController
        view.scheduleLayoutAnimation()
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showViewVisibility(view: View?, visibility: Int) {
        if (view != null)
            view.visibility = visibility
    }
}

package com.deepak.movieinfo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import com.deepak.movieinfo.fragments.MovieListFragment
import com.deepak.movieinfo.utils.AppUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        checkConnection()
    }

    fun checkConnection() {
        if (AppUtils.checkInternetPermission(this, Manifest.permission.INTERNET)) {
            loadMovieListFragment()
        }
    }

    private fun loadMovieListFragment() {
        showErrorPage(View.GONE)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val countryInfoListFragment = MovieListFragment()
        fragmentTransaction.replace(R.id.fragmentContainer, countryInfoListFragment)
        fragmentTransaction.commit()
    }

    private fun showErrorPage(visibility: Int) {
        errorContainer.visibility = visibility
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            AppUtils.MY_PERMISSIONS_REQUEST_INTERNET -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadMovieListFragment()
                } else {
                    showErrorPage(View.VISIBLE)
                }
                return
            }
        }
    }

    companion object {

        var tag = "Movie Info"
    }

}

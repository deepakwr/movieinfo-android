package com.deepak.movieinfo.fragments


import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

import org.json.JSONException
import org.json.JSONObject

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.deepak.movieinfo.MainActivity
import com.deepak.movieinfo.R
import com.deepak.movieinfo.adapters.MoviePagerAdapter
import com.deepak.movieinfo.models.MovieInfo
import com.deepak.movieinfo.utils.AppUtils
import com.deepak.movieinfo.utils.DummyContent
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.android.synthetic.main.fragment_movie_list.view.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class MovieListFragment : Fragment(), View.OnClickListener {

    private var movieMapList:MutableMap<Int, ArrayList<MovieInfo>> = mutableMapOf()

    var searchToken = ""
        private set

    private var moviePager: ViewPager? = null

    private var totalMovies = 0

    private val maxMoviesInPage = 10

    private var totalPages = 0

    private val searchTerm: String
        get() {
            return searchText?.text?.toString() ?: ""
        }

    init {
        movieMapList.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        setupViews()
    }

    private fun setupViews() {
        this.moviePager = movieViewPager

        val searchButton = searchButton
        searchButton?.setOnClickListener(this)
        updatePageNumber(0)

        this.moviePager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                updatePageNumber(position + 1)
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
    }

    fun updatePageNumber(pageNo: Int) {


        if (this.moviePager!!.adapter != null) {
            //            int pageNo = this.moviePager.getCurrentItem();
            val page = "" + pageNo + "/" + this.totalPages
            pageNumber.text = page
        }
    }

    private fun clearMovieList() {
        totalMovies = 0
        totalPages = 0
        movieMapList.clear()
        moviePager!!.adapter = null
    }


    fun getMovieArrayForPage(positon: Int): ArrayList<MovieInfo>? {
        return movieMapList[positon]
    }

    private fun loadMovieList() {
        this.moviePager!!.adapter = MoviePagerAdapter(this, this.totalPages)
    }

    private fun setApiKey() {
        val apiText = apiKey
        if (apiText != null) {
            AppUtils.setAPIKey(apiText.text.toString())
        }
        return
    }

    override fun onClick(view: View?) {
        if (view == null)
            return

        when (view.id) {
            R.id.searchButton -> {
                val searchToken:String = searchTerm
                if (searchToken.length > 2 && searchToken != this.searchToken){//, ignoreCase = true)) {
                    setApiKey()
                    this.searchToken = searchToken
                    clearMovieList()
                    RetrieveMovieListTask().execute(AppUtils.getSearchURL(this.searchToken, 1))
                    AppUtils.hideKeyboardFrom(this.context!!, getView()!!)
                } else {
                    if (searchToken.length < 3) {
                        Toast.makeText(context, "Please enter movie name or search term", Toast.LENGTH_LONG).show()
                    } else if (searchToken == this.searchToken){//, ignoreCase = true)) {
                        Toast.makeText(context, "Already searched.", Toast.LENGTH_LONG).show()
                    }
                }
            }

            else -> {
            }
        }//                dummyLoad();
    }

    fun loadTotalListDetails(jsonString: String?) {
        if (jsonString == null || jsonString.isEmpty()) {
            Log.e(MainActivity.tag, "Failed to retrieve JSON")
            return
        }

        try {
            val response = JSONObject(jsonString)

            val responseBool = response.getString("Response")
            if (responseBool == "False"){//, ignoreCase = true)) {
                Toast.makeText(context, "Invalid key or the response has failed", Toast.LENGTH_LONG).show()
                return
            }
            this.totalMovies = response.getInt("totalResults")
            this.totalPages = this.totalMovies / this.maxMoviesInPage
            val remainder = this.totalMovies % this.maxMoviesInPage
            if (remainder > 0) {
                this.totalPages++
            }

            loadMovieList()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    fun loadMovieDetails(jsonString: String, pageNo: Int): ArrayList<MovieInfo>? {
        val movieList = loadMovieDetails(jsonString)

        for (movie in movieList!!) {
            val additionalJsonString = AppUtils.httpGetStringRequest(AppUtils.getTitleInformationURL(movie.imdbID))
            movie.loadAdditonalInformation(additionalJsonString)
        }

        Collections.sort(movieList, Comparator { t1, t2 ->
            val returnIndex = 0
            if (java.lang.Float.parseFloat(t1.releasedYear) < java.lang.Float.parseFloat(t2.releasedYear)) {
                return@Comparator 1

            } else if (java.lang.Float.parseFloat(t1.releasedYear) > java.lang.Float.parseFloat(t2.releasedYear)) {
                return@Comparator -1
            }

            if (t1.ratings.isNotEmpty() && t2.ratings.isNotEmpty()
                    && t1.ratings != "N/A"//, ignoreCase = true)
                    && t2.ratings != "N/A"){//, ignoreCase = true)) {
                if (java.lang.Float.parseFloat(t1.ratings) < java.lang.Float.parseFloat(t2.ratings)) {
                    return@Comparator 1
                } else if (java.lang.Float.parseFloat(t1.ratings) > java.lang.Float.parseFloat(t2.ratings)) {
                    return@Comparator -1
                }
            }
            returnIndex
        })

        movieMapList.put(pageNo,movieList)

        return movieMapList[pageNo]
    }

    private fun loadMovieDetails(jsonString: String?): ArrayList<MovieInfo>? {
        if (jsonString == null || jsonString.isEmpty()) {
            Log.e(MainActivity.tag, "Failed to retrieve JSON")
            return null
        }

        try {
            val response = JSONObject(jsonString)

            val array = response.getJSONArray("Search")
            val movieList = ArrayList<MovieInfo>()


            for (i in 0 until array.length()) {
                val `object` = array.getJSONObject(i)
                val movieInfo = getMovieInfo(`object`)
                movieList.add(movieInfo!!)
            }
            return movieList


        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getMovieInfo(`object`: JSONObject): MovieInfo? {
        try {
            val movieName = `object`.getString("Title")
            val releaseYear = `object`.getString("Year")
            val imdbID = `object`.getString("imdbID")
//            val type = `object`.getString("Type")
            val url = `object`.getString("Poster")
            return MovieInfo(movieName, releaseYear, imdbID, url)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }


    @SuppressLint("NewApi")
    internal inner class RetrieveMovieListTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg urls: String): String? {
            // urls[0] is used, since are using single url.
            return AppUtils.httpGetStringRequest(urls[0])
        }

        override fun onPostExecute(jsonString: String) {
            loadTotalListDetails(jsonString)
        }
    }


    private fun dummyLoad() {
        val test1 = loadMovieDetails(DummyContent.JSON_PAGE_1)
        movieMapList[0] = test1!!

        val test2 = loadMovieDetails(DummyContent.JSON_PAGE_2)
        movieMapList[1] = test2!!

    }
}

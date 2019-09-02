package com.deepak.movieinfo.adapters

import android.annotation.SuppressLint
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import java.util.ArrayList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.deepak.movieinfo.R
import com.deepak.movieinfo.fragments.MovieListFragment
import com.deepak.movieinfo.models.MovieInfo
import com.deepak.movieinfo.utils.AppUtils
import kotlinx.android.synthetic.main.movie_list_page.view.*

class MoviePagerAdapter(fragment: MovieListFragment, pageCount: Int) : PagerAdapter() {

    private var fragment: MovieListFragment? = null

    private var pageCount = 0


    init {
        this.fragment = fragment
        this.pageCount = pageCount
    }

    override fun getCount(): Int {
        return this.pageCount
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = this.fragment!!.context!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView = inflater.inflate(R.layout.movie_list_page, container, false) as View


        val recyclerView = itemView.list

        val pageNumber = position + 1
        val movielist = fragment!!.getMovieArrayForPage(pageNumber)
        if (movielist == null) {
            AppUtils.showViewVisibility(itemView.loadingBar, View.VISIBLE)
            RetrieveMovieListTask(itemView, pageNumber).execute(AppUtils.getSearchURL(fragment!!.searchToken, pageNumber))
        } else {
            AppUtils.showViewVisibility(itemView.loadingBar, View.GONE)
            loadRecyclerView(recyclerView, movielist)
        }
        container.addView(itemView)


        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun loadRecyclerView(recyclerView: RecyclerView, movielist: ArrayList<MovieInfo>?) {
        recyclerView.layoutManager = LinearLayoutManager(fragment!!.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = MovieRecycleAdapter(fragment!!.context!!, movielist!!)
        AppUtils.runRecyclerAnimation(fragment!!.context!!, recyclerView, R.anim.layout_animation_from_bottom)


    }

    @SuppressLint("NewApi")
    internal inner class RetrieveMovieListTask(itemView: View, pageNumber: Int) : AsyncTask<String, Void, String>() {

        var pageNumber = 1
        var itemView: View? = null

        init {
            this.pageNumber = pageNumber
            this.itemView = itemView
        }

        override fun doInBackground(vararg urls: String): String? {
            // urls[0] is used, since are using single url.
            val jsonString = AppUtils.httpGetStringRequest(urls[0])
            fragment!!.loadMovieDetails(jsonString!!, this.pageNumber)
            return jsonString
        }

        override fun onPostExecute(jsonString: String) {
            val recyclerView = itemView!!.list
            AppUtils.showViewVisibility(itemView!!.loadingBar, View.GONE)
            loadRecyclerView(recyclerView, fragment!!.getMovieArrayForPage(pageNumber))
        }
    }
}

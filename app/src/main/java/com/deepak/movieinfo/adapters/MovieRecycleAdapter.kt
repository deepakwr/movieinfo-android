package com.deepak.movieinfo.adapters

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import androidx.recyclerview.widget.RecyclerView
import com.deepak.movieinfo.R
import com.deepak.movieinfo.models.MovieInfo
import kotlinx.android.synthetic.main.movie_row.view.*

class MovieRecycleAdapter internal constructor(context: Context, contentList: ArrayList<MovieInfo>) : RecyclerView.Adapter<MovieInfoHolder>() {

    var context: Context? = null
    var contentList: ArrayList<MovieInfo>? = null

    init {
        this.context = context
        this.contentList = contentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieInfoHolder {
        val inflater = this.context!!.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.movie_row, parent, false)
        return MovieInfoHolder(view)
    }

    override fun onBindViewHolder(holder: MovieInfoHolder, position: Int) {
        val item = contentList!![position]
        holder.name.text = item.name
        holder.description.text = item.description
        holder.rating.text = item.ratings
        holder.year.text = item.releasedYear
        holder.description.text = item.description
        //        Glide.with(context).asBitmap().load(item.getImageURL())
        //                .into(holder.moviePoster);


        Glide.with(context!!)
                .load(item.imageURL)
                .apply(RequestOptions.placeholderOf(R.drawable.clapperboard))
                .into(holder.moviePoster)
    }

    override fun getItemCount(): Int {
        return contentList!!.size
    }
}


class MovieInfoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var name: TextView
    var description: TextView
    var rating: TextView
    var year: TextView
    var moviePoster: ImageView

    init {
        name = itemView.name
        description = itemView.description
        rating = itemView.rating
        moviePoster = itemView.moviePoster
        year = itemView.releasedYear
    }
}

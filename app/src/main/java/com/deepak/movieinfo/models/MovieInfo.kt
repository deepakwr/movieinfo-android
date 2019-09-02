package com.deepak.movieinfo.models

import android.util.Log
import com.deepak.movieinfo.MainActivity

import org.json.JSONException
import org.json.JSONObject

class MovieInfo(name: String, releasedYear: String, imdbID: String, imageURL: String) {

    var name = ""

    var description = ""
        private set

    var ratings = ""
        private set

    var imdbID = ""

    var imageURL: String? = null

    //    private Date releaseDate = null;

    //    public String getReleaseDate() {
    //        if(releaseDate==null)
    //            return "N/A";
    //        return releaseDate.toString();
    //    }

    var releasedYear:String = ""
        private set

    init {
        this.name = name
        this.releasedYear = releasedYear

        if (this.releasedYear.length > 4) {
            this.releasedYear = this.releasedYear.substring(0, 4)
        }

        this.imdbID = imdbID
        this.imageURL = imageURL
    }

    fun loadAdditonalInformation(jsonString: String?) {
        if (jsonString == null || jsonString.isEmpty()) {
            Log.e(MainActivity.tag, "Failed to retrieve JSON")
            return
        }

        try {
            val response = JSONObject(jsonString)

            this.description = response.getString("Plot")
            this.ratings = response.getString("imdbRating")

            //Todo:: SimpleDateFormat Parsing issues with the value. Requires more attention. Also the specific date is unavailable here for certain titles.
            //            String releaseDate = "05 Jan 1990";//response.getString("Released");
            //            if(releaseDate!=null && !releaseDate.equalsIgnoreCase("N/A"))
            //            {
            //                DateFormat format = new SimpleDateFormat("dd mmm yyyy",Locale.US);
            //                this.releaseDate  = format.parse(releaseDate);
            //            }

        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}

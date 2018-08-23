package e.com.retrofitmovieproject.Model

import com.google.gson.annotations.SerializedName

class MovieResponse(
    @SerializedName("page") val page: Int = 0,
    @SerializedName("results") val results: List<Movie>? = null,
    @SerializedName("total_results") val totalResults: Int = 0,
    @SerializedName("total_pages") val totalPages: Int = 0
)
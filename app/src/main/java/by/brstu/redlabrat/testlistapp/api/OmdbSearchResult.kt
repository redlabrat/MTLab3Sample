package by.brstu.redlabrat.testlistapp.api

import com.google.gson.annotations.SerializedName

data class OmdbSearchResult (
    @SerializedName("Search")
    val results: List<OmdbSearchResultMovie>,
    @SerializedName("totalResults")
    val totalResults: String
)
package by.brstu.redlabrat.testlistapp.db

import androidx.room.*
import by.brstu.redlabrat.testlistapp.api.OmdbSearchResultMovie

@Dao
interface MovieDao {

    @Query("select * from search_result_movie")
    fun getAllSavedMovies(): List<OmdbSearchResultMovie>

    @Query("select * from search_result_movie where imdbId == :id")
    fun getMovieById(id: String): OmdbSearchResultMovie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addMovieToFavorites(movie: OmdbSearchResultMovie)

    @Delete
    fun removeFromFavorites(movie: OmdbSearchResultMovie)
}
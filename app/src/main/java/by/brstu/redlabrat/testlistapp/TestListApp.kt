package by.brstu.redlabrat.testlistapp

import android.app.Application
import androidx.room.Room
import by.brstu.redlabrat.testlistapp.api.OmdbApi
import by.brstu.redlabrat.testlistapp.db.MovieDao
import by.brstu.redlabrat.testlistapp.db.MoviesDatabase
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TestListApp : Application() {

    lateinit var omdbApi: OmdbApi
    lateinit var omdbDao: MovieDao

    override fun onCreate() {
        super.onCreate()

        omdbApi = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(OmdbApi::class.java)

        omdbDao = Room.databaseBuilder(
            applicationContext,
            MoviesDatabase::class.java,
            "favorite_movies.db")
            .build()
            .getFavoritesDao()
    }
}
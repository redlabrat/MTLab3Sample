package by.brstu.redlabrat.testlistapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import by.brstu.redlabrat.testlistapp.api.OmdbSearchResultMovie

@Database(entities = [OmdbSearchResultMovie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun getFavoritesDao(): MovieDao
}
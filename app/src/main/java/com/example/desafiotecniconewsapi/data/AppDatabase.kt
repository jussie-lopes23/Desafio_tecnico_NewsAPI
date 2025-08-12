package com.example.desafiotecniconewsapi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.desafiotecniconewsapi.dao.SearchQueryDao
import com.example.desafiotecniconewsapi.model.SearchQuery

@Database(entities = [SearchQuery::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "news_api_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
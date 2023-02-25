package com.example.currentsnews.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NewsEntity::class], version = 6)
abstract class NewsDataBase: RoomDatabase() {
    abstract fun newsDao(): NewsDao

    companion object{
        @Volatile
        private var INSTANCE: NewsDataBase?= null

        fun getDatabase(appContext: Context): NewsDataBase{

            return INSTANCE ?: synchronized(this){
                Room.databaseBuilder(
                    context = appContext,
                    NewsDataBase::class.java,
                    name= "news_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
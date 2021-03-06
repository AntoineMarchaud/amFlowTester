package com.amarchaud.amflowtester.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amarchaud.amflowtester.model.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 1)
@TypeConverters(GenreConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
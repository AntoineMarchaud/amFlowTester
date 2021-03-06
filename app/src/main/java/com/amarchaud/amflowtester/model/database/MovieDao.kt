package com.amarchaud.amflowtester.model.database

import androidx.room.*
import com.amarchaud.amflowtester.model.entity.MovieEntity

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies order by popularity DESC")
    suspend fun getAll(): List<MovieEntity>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Delete
    suspend fun delete(movie: MovieEntity)

    @Delete
    suspend fun deleteAll(movie: List<MovieEntity>)
}
package com.amarchaud.amflowtester.model.entity

import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.amarchaud.amflowtester.model.network.trending.MovieResult

@Entity(tableName = "movies")
data class MovieEntity(
    @NonNull
    @PrimaryKey
    val id: Int,
    val title: String?,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val genre_ids: List<Int>?
) {
    // from Api to Entity
    constructor(movieResult: MovieResult) : this(
        movieResult.id,
        movieResult.title,
        movieResult.overview,
        movieResult.popularity,
        movieResult.poster_path,
        movieResult.genre_ids
    )

    class MovieDiffUtil : DiffUtil.ItemCallback<MovieEntity>() {
        override fun areItemsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieEntity, newItem: MovieEntity): Boolean {
            return oldItem == newItem
        }
    }

}
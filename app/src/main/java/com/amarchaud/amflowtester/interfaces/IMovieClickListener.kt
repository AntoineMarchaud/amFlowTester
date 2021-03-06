package com.amarchaud.amflowtester.interfaces

import com.amarchaud.amflowtester.model.entity.MovieEntity

interface IMovieClickListener {
    fun onMovieClicked(movie: MovieEntity)
}
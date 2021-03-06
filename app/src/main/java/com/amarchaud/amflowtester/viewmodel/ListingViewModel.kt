package com.amarchaud.amflowtester.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.network.trending.TrendingResponse
import com.amarchaud.amflowtester.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    val app: Application,
    private val movieRepository: MovieRepository
) : AndroidViewModel(app) {

    private val _movieList = MutableLiveData<ResultFlow>()
    val movieList: LiveData<ResultFlow>
        get() = _movieList

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            movieRepository.fetchTrendingMovies().collect {
                _movieList.value = it
            }
        }
    }
}
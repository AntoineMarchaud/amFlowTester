package com.amarchaud.amflowtester.viewmodel

import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.flow.sub.ErrorFlow
import com.amarchaud.amflowtester.model.flow.sub.InitFlow
import com.amarchaud.amflowtester.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class ListingViewModel @Inject constructor(
    val app: Application,
    private val movieRepository: MovieRepository
) : AndroidViewModel(app) {


    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData


    private val _movieListLiveData = MutableLiveData<ResultFlow>()
    val movieListLiveData: LiveData<ResultFlow>
        get() = _movieListLiveData

    private val _movieListStateFlow = MutableStateFlow<ResultFlow>(InitFlow())
    val movieListStateFlow: StateFlow<ResultFlow>
        get() = _movieListStateFlow

    fun fetchMovies() {
        viewModelScope.launch {
            movieRepository.fetchTrendingMovies()
                .onStart {
                    _loadingLiveData.value = true
                }.onCompletion {
                    _loadingLiveData.value = false
                }.collect {
                    //_movieListLiveData.value = it
                    _movieListStateFlow.value = it
                }
        }
    }
}
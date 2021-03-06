package com.amarchaud.amflowtester.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * ViewModel for Movie details screen
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    val app: Application,
    private val movieRepository: MovieRepository
) : AndroidViewModel(app) {

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    // given by View
    var id = MutableLiveData<Int>()

    // when id change...
    private val _movieLiveData: LiveData<ResultFlow> = id.distinctUntilChanged().switchMap { id ->
        liveData {
            movieRepository.fetchMovie(id).onStart {
                _loadingLiveData.postValue(true)
            }.onCompletion {
                _loadingLiveData.postValue(false)
            }.collect {
                emit(it)
            }
        }
    }

    val movieLiveData: LiveData<ResultFlow>
        get() = _movieLiveData

}
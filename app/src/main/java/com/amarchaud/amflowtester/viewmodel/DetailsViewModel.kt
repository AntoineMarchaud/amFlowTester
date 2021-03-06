package com.amarchaud.amflowtester.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.flow.sub.IdFlow
import com.amarchaud.amflowtester.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
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

    // given by View
    var id = MutableLiveData<Int>()

    // when id change...
    private val _movie: LiveData<ResultFlow> = id.distinctUntilChanged().switchMap { id ->
        liveData {
            movieRepository.fetchMovie(id).collect {
                emit(it)
            }
        }
    }

    val movie: LiveData<ResultFlow>
        get() = _movie

}
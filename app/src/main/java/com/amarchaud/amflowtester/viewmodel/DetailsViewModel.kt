package com.amarchaud.amflowtester.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.flow.sub.ErrorFlow
import com.amarchaud.amflowtester.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.security.PrivateKey
import javax.inject.Inject

/**
 * ViewModel for Movie details screen
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    val app: Application,
    private val movieRepository: MovieRepository,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(app) {

    private var _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData


    private var id = MutableLiveData<Int>(savedStateHandle.get("id"))


    // when id change...
    private val _movieLiveData: LiveData<ResultFlow> = id.distinctUntilChanged().switchMap { id ->
        liveData {
            movieRepository.fetchMovie(id).onStart {
                _loadingLiveData.value = true
            }.onCompletion {
                _loadingLiveData.value = false
            }.collect {
                emit(it)
            }

            // ou a la place du .collect :  .asLiveData(viewModelScope.coroutineContext)
        }
    }

    val movieLiveData: LiveData<ResultFlow>
        get() = _movieLiveData

}
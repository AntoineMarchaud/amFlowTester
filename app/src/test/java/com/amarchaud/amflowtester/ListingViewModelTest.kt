package com.amarchaud.amflowtester

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.amarchaud.amflowtester.model.database.MovieDao
import com.amarchaud.amflowtester.model.entity.MovieEntity
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.flow.sub.MovieEntityFlow
import com.amarchaud.amflowtester.network.MovieApi
import com.amarchaud.amflowtester.repository.MovieRepository
import com.amarchaud.amflowtester.viewmodel.ListingViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import org.mockito.Mockito.*

class ListingViewModelTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Mock
    lateinit var mockResultFlowObserver: Observer<ResultFlow>

    @Mock
    lateinit var applicationMock: Application

    @Mock
    private lateinit var movieRepositoryMock: MovieRepository

    @Captor
    private lateinit var captor: ArgumentCaptor<ResultFlow>


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testListViewModelFlow() {

        runBlocking{

            val flow = flow {

                emit(
                    MovieEntityFlow(
                        listOf(
                            MovieEntity(1, "title", "overview", 0.0, null, null),
                            MovieEntity(2, "title2", "overview2", 0.0, null, null)
                        )
                    )
                )

                delay(10)

                emit(
                    MovieEntityFlow(
                        listOf(
                            MovieEntity(3, "title3", "overview", 0.0, null, null)
                        )
                    )
                )
            }

            // init viewmodel
            val viewModel = ListingViewModel(applicationMock, movieRepositoryMock)
            `when`(movieRepositoryMock.fetchTrendingMovies()).thenReturn(flow)

            // add the observer
            viewModel.movieListLiveData.observeForever(mockResultFlowObserver)

            // launch viewModel method
            // do not use suspend fun
            viewModel.fetchMovies()

            // check first flow info
            verify(mockResultFlowObserver, times(1)).onChanged(captor.capture())
            assertEquals(true, captor.value is MovieEntityFlow)
            assertEquals(2, (captor.value as MovieEntityFlow).movies?.size)
            assertEquals(1, (captor.value as MovieEntityFlow).movies?.get(0)?.id)
            assertEquals("title2", (captor.value as MovieEntityFlow).movies?.get(1)?.title)

            // next flow
            coroutinesTestRule.advanceTimeBy(10)

            // check second flow info
            verify(mockResultFlowObserver, times(2)).onChanged(captor.capture())
            assertEquals(true, captor.value is MovieEntityFlow)
            assertEquals(1, (captor.value as MovieEntityFlow).movies?.size)
            assertEquals(3, (captor.value as MovieEntityFlow).movies?.get(0)?.id)
            assertEquals("title3", (captor.value as MovieEntityFlow).movies?.get(0)?.title)

            // remove the observer
            viewModel.movieListLiveData.removeObserver(mockResultFlowObserver)
        }
    }
}
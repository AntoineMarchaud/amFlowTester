package com.amarchaud.amflowtester

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.amarchaud.amflowtester.model.database.MovieDao
import com.amarchaud.amflowtester.model.entity.MovieEntity
import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.flow.sub.ErrorFlow
import com.amarchaud.amflowtester.model.flow.sub.MovieEntityFlow
import com.amarchaud.amflowtester.network.MovieApi
import com.amarchaud.amflowtester.repository.MovieRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.*
import retrofit2.Response

class RepositoryTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    @Mock
    private lateinit var movieDao: MovieDao

    @Mock
    private lateinit var movieApi: MovieApi

    @Spy
    @InjectMocks
    private lateinit var movieRepositoryMock: MovieRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testRepoWithApiError() {
        runBlocking {

            Mockito.`when`(movieDao.getAll()).thenReturn(
                listOf(
                    MovieEntity(1, "title", "overview", 0.0, null, null),
                    MovieEntity(2, "title2", "overview2", 0.0, null, null)
                )
            )

            Mockito.`when`(movieApi.getPopularMovies()).thenReturn(
                Response.error(
                    404,
                    "{\"key\":[\"not found\"]}"
                        .toResponseBody("application/json".toMediaTypeOrNull())
                )
            )

            movieRepositoryMock.fetchTrendingMovies().collect {
                when (it) {
                    is MovieEntityFlow -> {
                        Assert.assertEquals(2, it.movies?.size)
                        Assert.assertEquals(1, it.movies?.get(0)?.id)
                        Assert.assertEquals(
                            "title2",
                            it.movies?.get(1)?.title
                        )
                    }
                    is ErrorFlow -> {
                        Assert.assertEquals(404, it.status_code)
                    }
                }
            }
        }
    }
}
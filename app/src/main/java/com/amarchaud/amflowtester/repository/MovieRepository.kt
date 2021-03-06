package com.amarchaud.amflowtester.repository


import com.amarchaud.amflowtester.model.flow.ResultFlow
import com.amarchaud.amflowtester.model.database.MovieDao
import com.amarchaud.amflowtester.model.entity.MovieEntity
import com.amarchaud.amflowtester.model.flow.sub.ErrorFlow
import com.amarchaud.amflowtester.model.flow.sub.DetailApiFlow
import com.amarchaud.amflowtester.model.flow.sub.MovieEntityFlow
import com.amarchaud.amflowtester.model.network.trending.TrendingResponse
import com.amarchaud.amflowtester.network.MovieApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Repository which fetches data from Remote or Local data sources
 */
class MovieRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val movieApi: MovieApi
) {

    /**
     * For all movies
     */
    suspend fun fetchTrendingMovies() = flow {

        // first : database
        emit(MovieEntityFlow(movieDao.getAll()))

        // secondly : loading api
        emit(ResultFlow(ResultFlow.Companion.TypeResponse.LOADING))

        // last : remote movies
        val result = movieApi.getPopularMovies()
        //Cache to database if response is successful
        if (result.isSuccessful) {

            // get data
            val responseBody = (result.body() as TrendingResponse)
            responseBody.results?.map { MovieEntity(it) }?.let {
                movieDao.deleteAll(it)
                movieDao.insertAll(it)

                // emit database again
                emit(MovieEntityFlow(it))
            }

        } else {
            emit(ErrorFlow(result.code(), result.message()))
        }

    }.flowOn(Dispatchers.IO)


    /**
     * For details only
     */
    suspend fun fetchMovie(id: Int) = flow {

        emit(ResultFlow(ResultFlow.Companion.TypeResponse.LOADING))

        val result = movieApi.getMovie(id)
        if (result.isSuccessful) {
            emit(DetailApiFlow(result.body())) // emit api data
        } else {
            emit(ErrorFlow(result.code(), result.message()))
        }

    }.flowOn(Dispatchers.IO)

}
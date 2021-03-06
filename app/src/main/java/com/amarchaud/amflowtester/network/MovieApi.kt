package com.amarchaud.amflowtester.network

import com.amarchaud.amflowtester.model.network.detail.DetailResponse
import com.amarchaud.amflowtester.model.network.trending.TrendingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApi {

    @GET("/3/trending/movie/week")
    suspend fun getPopularMovies() : Response<TrendingResponse>

    @GET("/3/movie/{movie_id}")
    suspend fun getMovie(@Path("movie_id") id: Int) : Response<DetailResponse>
}
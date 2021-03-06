package com.amarchaud.amflowtester.injection

import com.amarchaud.amflowtester.model.database.MovieDao
import com.amarchaud.amflowtester.network.MovieApi
import com.amarchaud.amflowtester.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MovieRepositoryModule {
    @Provides
    @Singleton
    fun provideMovieRepository(movieDao : MovieDao, movieApi: MovieApi): MovieRepository {
        return MovieRepository(movieDao, movieApi)
    }
}
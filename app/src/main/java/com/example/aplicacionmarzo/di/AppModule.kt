package com.example.aplicacionmarzo.di

import com.example.aplicacionmarzo.data.repository.RestauranteRepositoryImpl
import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRestauranteRepository(
        restauranteRepositoryImpl: RestauranteRepositoryImpl
    ): RestauranteRepository
}

package com.example.aplicacionmarzo.di

import android.content.Context
import com.example.aplicacionmarzo.data.repository.RestauranteRepositoryImpl
import com.example.aplicacionmarzo.data.service.ApiService
import com.example.aplicacionmarzo.domain.repository.RestauranteRepository
import com.example.aplicacionmarzo.utils.JwtManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://10.0.2.2:8081/" // Ajusta según tu API

    @Provides
    @Singleton
    fun provideOkHttpClient(jwtManager: JwtManager): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${jwtManager.getToken()}")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
    @Provides
    @Singleton
    fun provideRestauranteRepository(apiService: ApiService): RestauranteRepository {
        return RestauranteRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideJwtManager(@ApplicationContext context: Context): JwtManager {
        return JwtManager(context)
    }
}
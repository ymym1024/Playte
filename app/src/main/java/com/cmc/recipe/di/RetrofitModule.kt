package com.cmc.recipe.di

import com.cmc.recipe.BuildConfig
import com.cmc.recipe.MainApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Singleton
    @Provides
    fun providesNetworkInterceptor() = Interceptor { chain ->
        val token = MainApplication.tokenManager.getAccessToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "$token")
            .build()
        val response = chain.proceed(newRequest)

        response.newBuilder().build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            connectTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            addInterceptor(providesNetworkInterceptor())
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
    }

    @Provides
    @Singleton
    @Named("RecipeApi")
    fun provideRetrofit(
        client: OkHttpClient.Builder
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("${BuildConfig.BASE_URL}")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }

    @Provides
    @Singleton
    @Named("GoogleLogin")
    fun provideRetrofitForGoogle(
        client: OkHttpClient.Builder
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("${BuildConfig.GOOGLE_BASE_URL}")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }
}
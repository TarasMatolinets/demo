package com.demo.api.builder

import com.demo.api.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiBuilderImpl @Inject constructor() : ApiBuilder {

    private val builder = Retrofit.Builder()

    init {
        builder.baseUrl(BASE_URL)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    override fun provideAPIClient(): Retrofit {
        return builder.build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder = Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(BASIC)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }

    companion object {
        private const val BASE_URL = "https://api.rawg.io/api/"
    }
}
package com.demo.api.builder

import androidx.annotation.NonNull
import retrofit2.Retrofit

interface ApiBuilder {
    @NonNull
    fun provideAPIClient(): Retrofit
}
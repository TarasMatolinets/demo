package com.demo.di

import com.demo.api.builder.ApiBuilder
import com.demo.api.builder.ApiBuilderImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ApiModule {

    @Binds
    abstract fun provideApiBuilder(apiBuilder: ApiBuilderImpl): ApiBuilder
}
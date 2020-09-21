package com.demo.di.module

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.demo.GameImplm
import com.demo.api.builder.ApiBuilder
import com.demo.api.builder.ApiBuilderImpl
import com.demo.di.ViewModelFactory
import com.demo.di.scope.ViewModelKey
import com.demo.mvvm.view.activity.GameDetailsActivity
import com.demo.mvvm.view.activity.MainActivity
import com.demo.mvvm.viewModel.GameDetailsActivityViewModel
import com.demo.mvvm.viewModel.MainActivityViewModel
import com.demo.networking.NetworkHandler
import com.demo.repository.GameRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import javax.inject.Provider
import javax.inject.Singleton

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [GameDetailsActivityModule::class])
    abstract fun bindGameDetailsActivity(): GameDetailsActivity
}

@Module
abstract class MainActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainViewModelKey(viewModel: MainActivityViewModel): ViewModel
}

@Module
abstract class GameDetailsActivityModule {
    @Binds
    @IntoMap
    @ViewModelKey(GameDetailsActivityViewModel::class)
    abstract fun bindGameDetailsViewModelKey(viewModel: GameDetailsActivityViewModel): ViewModel
}

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    fun bindGamesRepository(networkHandler: NetworkHandler, apiBuilder: ApiBuilder):
            GameRepository = GameImplm(networkHandler, apiBuilder)

    companion object {
        @Provides
        fun provideViewModelFactory(providers: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory =
            ViewModelFactory(providers)
    }
}

@Module
abstract class ApiModule {
    @Binds
    abstract fun provideApiBuilder(apiBuilder: ApiBuilderImpl): ApiBuilder
}

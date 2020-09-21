package com.demo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.demo.models.GameResult
import com.demo.mvvm.adapter.State
import com.demo.mvvm.model.GameModelUI
import com.demo.networking.Failure
import com.demo.repository.GameRepository
import com.demo.usecase.UseCaseProvideGamesResult


class GameDataSourceFactory(
    private val gameRepository: GameRepository,
    private val contextProvider: CoroutineContextProvider, private val search: String
) : DataSource.Factory<Int, GameModelUI>() {
    private val gameDataSourceMutableLiveData = MutableLiveData<GameDataSource>()
    val gameDataSourceLiveData: LiveData<GameDataSource> = gameDataSourceMutableLiveData

    override fun create(): DataSource<Int, GameModelUI> {
        val gameDataSource = GameDataSource(gameRepository, contextProvider, search)
        gameDataSourceMutableLiveData.postValue(gameDataSource)
        return gameDataSource
    }
}

class GameDataSource(
    private val gameRepository: GameRepository,
    private val contextProvider: CoroutineContextProvider,
    private val order: String,
) : PageKeyedDataSource<Int, GameModelUI>() {

    private val stateMutable: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = stateMutable
    private var loadMore: Boolean = true
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, GameModelUI>) {
        val useCaseSearch = UseCaseProvideGamesResult(gameRepository, contextProvider.main, contextProvider.io, order, 1, 10)

        useCaseSearch {
            updateState(State.Loading)
            fun handleGameResult(gameResult: GameResult) {
                updateState(State.Done)
                callback.onResult(
                    transformGameUI(gameResult.results()),
                    null,
                    1
                )
            }

            fun handleFailure(failure: Failure) {
                updateState(State.Error(failure))
            }

            it.either(
                ::handleFailure,
                ::handleGameResult
            )
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, GameModelUI>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, GameModelUI>) {
        updateState(State.Loading)
        if (loadMore) {
            val useCaseSearch = UseCaseProvideGamesResult(gameRepository, contextProvider.main, contextProvider.io, order, params.key, 10)

            useCaseSearch {
                fun handleGameResult(gameResult: GameResult) {
                    loadMore = !gameResult.next().isNullOrEmpty()
                    updateState(State.Done)
                    callback.onResult(
                        transformGameUI(gameResult.results()),
                        params.key + 1,
                    )
                }

                fun handleFailure(failure: Failure) {
                    updateState(State.Error(failure))
                }

                it.either(
                    ::handleFailure,
                    ::handleGameResult
                )
            }
        }
    }

    private fun updateState(state: State) {
        stateMutable.postValue(state)
    }
}
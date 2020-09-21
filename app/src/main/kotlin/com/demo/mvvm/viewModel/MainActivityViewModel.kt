package com.demo.mvvm.viewModel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.demo.mvvm.adapter.AdapterPosition.HORIZONTAL
import com.demo.mvvm.adapter.AdapterPosition.VERTICAL
import com.demo.mvvm.adapter.GameAdapter
import com.demo.mvvm.adapter.GameInfo
import com.demo.mvvm.adapter.State
import com.demo.networking.Failure
import com.demo.repository.GameRepository
import com.demo.utils.CoroutineContextProvider
import com.demo.utils.Event
import com.demo.utils.GameDataSource
import com.demo.utils.GameDataSourceFactory
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    gameRepository: GameRepository,
    contextProvider: CoroutineContextProvider
) : ViewModel() {
    private var mutableFailure: MutableLiveData<Failure> = MutableLiveData()
    var failure: LiveData<Failure> = mutableFailure
    private val pageSize = 5

    private var mutableGameClicked: MutableLiveData<Event<GameInfo>> = MutableLiveData()
    var gameClicked: LiveData<Event<GameInfo>> = mutableGameClicked

    private val config = PagedList.Config.Builder()
        .setPageSize(pageSize)
        .setInitialLoadSizeHint(pageSize * 2)
        .setEnablePlaceholders(false)
        .build()

    private val createdSourceFactory = GameDataSourceFactory(gameRepository, contextProvider, "created")
    val createdGameList = LivePagedListBuilder(createdSourceFactory, config).build()

    private val releasedSourceFactory = GameDataSourceFactory(gameRepository, contextProvider, "released")
    val releasedGameList = LivePagedListBuilder(releasedSourceFactory, config).build()

    private val ratingSourceFactory = GameDataSourceFactory(gameRepository, contextProvider, "rating")
    val ratingGameList = LivePagedListBuilder(ratingSourceFactory, config).build()

    var progress: ObservableBoolean = ObservableBoolean(false)
    var actionButton: ObservableBoolean = ObservableBoolean(true)

    val adapterCreated = GameAdapter(adapterPosition = HORIZONTAL) { mutableGameClicked.value = Event(it) }
    val adapterReleased = GameAdapter(adapterPosition = VERTICAL) { mutableGameClicked.value = Event(it) }
    val adapterMostRated = GameAdapter(adapterPosition = HORIZONTAL) { mutableGameClicked.value = Event(it) }

    init {
        progress.set(true)
        actionButton.set(false)
    }

    fun getCreatedState(): LiveData<State> = Transformations.switchMap(
        createdSourceFactory.gameDataSourceLiveData,
        GameDataSource::state
    )

    fun getReleasedState(): LiveData<State> = Transformations.switchMap(
        releasedSourceFactory.gameDataSourceLiveData,
        GameDataSource::state
    )

    fun getRatingState(): LiveData<State> = Transformations.switchMap(
        ratingSourceFactory.gameDataSourceLiveData,
        GameDataSource::state
    )
}

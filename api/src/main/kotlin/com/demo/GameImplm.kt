package com.demo

import com.demo.api.Api
import com.demo.api.builder.ApiBuilder
import com.demo.models.GameResult
import com.demo.models.GameResultEntity
import com.demo.networking.Either
import com.demo.networking.Failure
import com.demo.networking.NetworkHandler
import com.demo.repository.GameRepository
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class GameImplm @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val apiBuilder: ApiBuilder
) : GameRepository, Api {

    private val api: Api = lazy {
        val retrofit = apiBuilder.provideAPIClient()
        retrofit.create(Api::class.java)
    }.value

    override fun gameResultAsync(page: Int, pageSize: Int, search: String): Deferred<GameResultEntity> {
        return api.gameResultAsync(page, pageSize, search)
    }

    override suspend fun provideGameResult(page: Int, pageSize: Int, query: String): Either<Failure, GameResult> {
        return if (networkHandler.isConnected) {
            Either.Right(gameResultAsync(page, pageSize, query).await())
        } else {
            Either.Left(Failure.NetworkConnection)
        }
    }
}
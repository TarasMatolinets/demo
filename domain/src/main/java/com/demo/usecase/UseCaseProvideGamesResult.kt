package com.demo.usecase

import com.demo.models.GameResult
import com.demo.networking.Either
import com.demo.networking.Failure
import com.demo.repository.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class UseCaseProvideGamesResult(
    private val repository: GameRepository,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
    private val search: String,
    private val page: Int = 1,
    private val pageSize: Int = 20
) {
    operator fun invoke(onResult: (Either<Failure, GameResult>) -> Unit = {}) {
        val job = CoroutineScope(ioContext).async { repository.provideGameResult(page, pageSize, search) }
        CoroutineScope(mainContext).launch { onResult(job.await()) }
    }
}
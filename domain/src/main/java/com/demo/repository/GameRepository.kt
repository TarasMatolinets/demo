package com.demo.repository

import com.demo.models.GameResult
import com.demo.networking.Either
import com.demo.networking.Failure

interface GameRepository {
    suspend fun provideGameResult(page: Int, pageSize: Int, query: String): Either<Failure, GameResult>
}
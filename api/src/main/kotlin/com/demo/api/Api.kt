package com.demo.api

import com.demo.models.GameResultEntity
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("games")
    fun gameResultAsync(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("ordering") search: String
    ): Deferred<GameResultEntity>
}
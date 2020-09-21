package com.demo.utils

import com.demo.models.Game
import com.demo.models.Rating
import com.demo.models.ShortScreenShoot
import com.demo.mvvm.model.GameModelUI
import com.demo.mvvm.model.RatingUI
import com.demo.mvvm.model.ShortScreenShootUI

fun transformGameUI(list: List<Game>): List<GameModelUI> {
    fun transformShortScreenUI(listScreen: List<ShortScreenShoot>) =
        listScreen.map { ShortScreenShootUI(it.id(), it.image()) }

    fun transformRatingUI(listRating: List<Rating>) =
        listRating.map { RatingUI(it.id(), it.title(), it.percent()) }

    return list.map {
        GameModelUI(
            it.id(),
            it.name(),
            it.backgroundImage(),
            it.rating(),
            transformShortScreenUI(it.shotScreenShoot()),
            transformRatingUI(it.ratings())
        )
    }
}
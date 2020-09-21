package com.demo.models

import com.google.gson.annotations.SerializedName

data class GameResultEntity(
    @SerializedName("next")
    val next: String?,
    @SerializedName("count")
    val count: Int,
    @SerializedName("results")
    val results: List<GameEntity>
) : GameResult {

    override fun results(): List<Game> {
        return this.results
    }

    override fun next(): String? {
        return this.next
    }

    override fun count(): Int {
        return this.count
    }
}

data class GameEntity(
    @SerializedName("name")
    val name: String,
    @SerializedName("background_image")
    val backgroundImage: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("rating_top")
    val ratingTop: Int,
    @SerializedName("short_screenshots")
    val shortScreenshots: List<ShortScreenShootEntity>,
    @SerializedName("ratings")
    val ratings: List<RatingEntity>,
    @SerializedName("released")
    val released: String,
    @SerializedName("added")
    val added: Int,
    @SerializedName("rating")
    val rating: Double
) : Game {
    override fun name(): String {
        return this.name
    }

    override fun backgroundImage(): String? {
        return this.backgroundImage
    }

    override fun id(): Int {
        return this.id
    }

    override fun ratings(): List<Rating> {
        return this.ratings
    }

    override fun shotScreenShoot(): List<ShortScreenShoot> {
        return this.shortScreenshots
    }

    override fun added(): Int {
        return this.added
    }

    override fun released(): String {
        return this.released
    }

    override fun rating(): Double {
        return this.rating
    }
}

data class ShortScreenShootEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String
) : ShortScreenShoot {
    override fun id(): Int {
        return this.id
    }

    override fun image(): String {
        return this.image
    }
}

data class RatingEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("percent")
    val percent: Double,
    @SerializedName("count")
    val count: Int
) : Rating {
    override fun id(): Int {
        return this.id
    }

    override fun title(): String {
        return this.title
    }

    override fun percent(): Double {
        return this.percent
    }
}


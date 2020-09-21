package com.demo.mvvm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GameModelUI(
    val id: Int,
    val name: String,
    val backgroundImage: String?,
    val rating: Double,
    val shortScreenShot: List<ShortScreenShootUI>,
    val ratings: List<RatingUI>
) : Parcelable

@Parcelize
data class ShortScreenShootUI(
    val id: Int,
    val image: String
) : Parcelable

@Parcelize
data class RatingUI(
    val id: Int,
    val title: String,
    val percent: Double
) : Parcelable

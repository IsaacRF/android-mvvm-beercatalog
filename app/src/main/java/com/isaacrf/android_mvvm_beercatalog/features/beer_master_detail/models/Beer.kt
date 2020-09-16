package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db.BeerTypeConverters

/**
 * A representation of a beer
 */
@Entity
data class Beer (
    @PrimaryKey
    val id: Int,
    val name: String,
    val tagline: String,
    val description: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("abv")
    val alcoholByVolume: Double,
    @SerializedName("ibu")
    val bitterness: Double,
    @SerializedName("food_pairing")
    @TypeConverters(BeerTypeConverters::class)
    val foodPairing: List<String>,
    var available: Boolean
) {}
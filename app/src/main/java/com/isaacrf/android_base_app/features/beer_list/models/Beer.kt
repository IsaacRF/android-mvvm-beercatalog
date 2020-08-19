package com.isaacrf.android_base_app.features.beer_list.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.isaacrf.android_base_app.features.beer_list.db.BeerTypeConverters

/**
 * A representation of a beer product
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
package com.isaacrf.android_base_app.features.beer_list.models

import com.google.gson.annotations.SerializedName

/**
 * A representation of a beer product
 */
data class Beer (
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
    val foodPairing: List<String>,
    val available: Boolean
) {}
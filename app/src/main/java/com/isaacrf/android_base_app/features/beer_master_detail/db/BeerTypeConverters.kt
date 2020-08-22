package com.isaacrf.android_base_app.features.beer_master_detail.db

import androidx.room.TypeConverter

object BeerTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToStringList(data: String?): List<String>? {
        return data?.let {
            it.split(",")
        }
    }

    @TypeConverter
    @JvmStatic
    fun stringListToString(strings: List<String>?): String? {
        return strings?.joinToString(",")
    }
}
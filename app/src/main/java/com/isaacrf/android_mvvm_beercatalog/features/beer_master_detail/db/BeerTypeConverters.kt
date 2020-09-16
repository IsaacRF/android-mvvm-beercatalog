package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db

import androidx.room.TypeConverter

object BeerTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToStringList(data: String?): List<String>? {
        return data?.split(",")
    }

    @TypeConverter
    @JvmStatic
    fun stringListToString(strings: List<String>?): String? {
        return strings?.joinToString(",")
    }
}
package com.isaacrf.android_base_app.features.beer_list.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.isaacrf.android_base_app.features.beer_list.models.Beer

@Database(entities = [Beer::class], version = 1, exportSchema = false)
@TypeConverters(BeerTypeConverters::class)
abstract class BeerDatabase: RoomDatabase() {
    abstract fun beerDao(): BeerDao
}
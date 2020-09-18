package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db

import androidx.room.RoomDatabase

/**
 * Beer database representation. Gives access to dao objects
 */
interface BeerDatabase {
    fun beerDao(): BeerDao
}
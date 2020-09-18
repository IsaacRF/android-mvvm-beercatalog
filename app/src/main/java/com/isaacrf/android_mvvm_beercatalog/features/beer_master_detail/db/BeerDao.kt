package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db

import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer

/**
 * Data access layer for Beer
 */
interface BeerDao {
    fun insert(vararg beers: Beer)
    fun insert(beers: List<Beer>)
    fun update(beer: Beer)
    fun load(beerId: Int): Beer?
    fun load(): List<Beer>?
}
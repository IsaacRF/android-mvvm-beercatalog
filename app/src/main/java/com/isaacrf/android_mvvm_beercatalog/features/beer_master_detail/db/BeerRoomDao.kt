package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db

import androidx.room.*
import androidx.room.OnConflictStrategy.*
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer

/**
 * Room implementation of BeerDao
 */
@Dao
abstract class BeerRoomDao: BeerDao {
    @Insert(onConflict = ABORT)
    abstract override fun insert(vararg beers: Beer)

    @Insert(onConflict = IGNORE)
    abstract override fun insert(beers: List<Beer>)

    @Update
    abstract override fun update(beer: Beer)

    @Query("SELECT * FROM Beer WHERE id = :beerId")
    abstract override fun load(beerId: Int): Beer?

    @Query("SELECT * FROM Beer")
    abstract override fun load(): List<Beer>?
}
package com.isaacrf.android_base_app.features.beer_list.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.*
import com.isaacrf.android_base_app.features.beer_list.models.Beer

@Dao
abstract class BeerDao {
    @Insert(onConflict = ABORT)
    abstract fun insert(vararg beers: Beer)

    @Insert(onConflict = IGNORE)
    abstract fun insert(beers: List<Beer>)

    @Update
    abstract fun update(beer: Beer)

    @Query("SELECT * FROM Beer WHERE id = :beerId")
    abstract fun load(beerId: Int): LiveData<Beer>?

    @Query("SELECT * FROM Beer")
    abstract fun load(): List<Beer>?
}
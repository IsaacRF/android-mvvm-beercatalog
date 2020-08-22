package com.isaacrf.android_base_app

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.isaacrf.android_base_app.features.beer_master_detail.db.BeerDao
import com.isaacrf.android_base_app.features.beer_master_detail.db.BeerDatabase
import com.isaacrf.android_base_app.helpers.MockBeersHelper
import junit.framework.Assert.*
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import java.io.IOException

/**
 * Instrumented database test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BeerDaoTest : MockBeersHelper() {
    private lateinit var beerDao: BeerDao
    private lateinit var db: BeerDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BeerDatabase::class.java
        ).build()
        beerDao = db.beerDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDbOperations() {
        val beers = getMockBeers()

        beerDao.insert(beers)

        //Insert and retrieve tests
        val retrievedBeers = beerDao.load()
        assertNotNull(retrievedBeers)
        assertThat(
            "Wrong retrieved beer list size. Expected 20. Got ${retrievedBeers?.size}",
            retrievedBeers?.size,
            `is`(20)
        )
        assertThat(
            "First beer wrong name. Expected Buzz. Got ${retrievedBeers?.get(0)?.name}",
            retrievedBeers?.get(0)?.name,
            `is`("Buzz")
        )

        //Update tests
        assertTrue(
            "Beer availability error. Expected true. Got ${retrievedBeers?.get(0)?.available}",
            retrievedBeers?.get(0)?.available!!
        )
        val beerToUpdate = beers[0]
        beerToUpdate.available = false
        beerDao.update(beerToUpdate)
        val beerRetrieved = beerDao.load(beerToUpdate.id)
        assertNotNull("Failed retrieving updated beer", beerRetrieved)
        assertFalse("Beer availability change error. Expected change to false. Got ${beerRetrieved?.available}", beerRetrieved?.available!!)
    }
}
package com.isaacrf.android_mvvm_beercatalog.helpers

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.services.BeerDeserializer
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Helper methods to manipulate mocked beers
 */
abstract class MockBeersHelper {
    private val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(Beer::class.java, BeerDeserializer())
            .setLenient()
            .create()
    }

    /**
     * Get mocked beers from BeersResponseSample.json file
     */
    fun getMockBeers(): List<Beer> {
        val reader =
            BufferedReader(InputStreamReader(javaClass.classLoader!!.getResourceAsStream("mock_responses/BeersResponseSample.json")))
        return gson.fromJson(reader, Array<Beer>::class.java).toList()
    }
}
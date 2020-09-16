package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.services

import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service to handle http calls to beers backend
 */
interface BeerListService {
    /**
     * @GET Method. Retrieves all beers
     */
    @GET("beers")
    fun getBeers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<List<Beer>>
}
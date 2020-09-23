package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.services

import androidx.lifecycle.LiveData
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import com.isaacrf.android_mvvm_beercatalog.shared.api.ApiResponse

/**
 * Service representation to handle http calls to beers backend
 */
interface BeerListService {
    /**
     * @GET Method. Retrieves all beers
     */
    fun getBeers(page: Int, perPage: Int): LiveData<ApiResponse<List<Beer>>>
}
package com.isaacrf.android_base_app.features.beer_list.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isaacrf.android_base_app.features.beer_list.models.Beer
import com.isaacrf.android_base_app.features.beer_list.services.BeerListService
import com.isaacrf.android_base_app.shared.NetworkResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Layer to abstract data access to repo list info
 */
@Singleton
class BeerListRepository @Inject constructor(
    private val beerListService: BeerListService
){
    /**
     * GET all beers
     */
    fun getBeers(): LiveData<NetworkResource<List<Beer>>> {
        val data = MutableLiveData<NetworkResource<List<Beer>>>()
        data.value = NetworkResource.loading(null)
        beerListService.getBeers(1).enqueue(object : Callback<List<Beer>> {
            override fun onResponse(call: Call<List<Beer>>, response: Response<List<Beer>>) {
                data.value = NetworkResource.success(response.body())
            }
            override fun onFailure(call: Call<List<Beer>>, t: Throwable) {
                Log.d("BeerListRepository", "getBeers() - Failure")
                data.value = NetworkResource.error(t.message!!, null)
            }
        })
        return data
    }
}
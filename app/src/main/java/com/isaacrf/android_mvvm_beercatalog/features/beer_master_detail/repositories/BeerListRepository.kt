package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db.BeerDao
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db.BeerDatabase
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.services.BeerListService
import com.isaacrf.android_mvvm_beercatalog.shared.helpers.AppExecutors
import com.isaacrf.android_mvvm_beercatalog.shared.helpers.NetworkResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Layer to abstract data access to beers
 */
@Singleton
class BeerListRepository @Inject constructor(
    private val beerListService: BeerListService,
    private val beerDatabase: BeerDatabase,
    private val beerDao: BeerDao,
    private val appExecutors: AppExecutors
) {
    /**
     * GET all beers
     */
    fun getBeers(): MutableLiveData<NetworkResource<List<Beer>>> {
        val data = MutableLiveData<NetworkResource<List<Beer>>>()
        data.value = NetworkResource.loading(null)
        beerListService.getBeers(1, 80).enqueue(object : Callback<List<Beer>> {
            override fun onResponse(call: Call<List<Beer>>, response: Response<List<Beer>>) {
                appExecutors.diskIO().execute {
                    beerDao.insert(response.body()!!)
                    val beers = beerDao.load()

                    appExecutors.mainThread().execute {
                        data.value = NetworkResource.success(beers)
                    }
                }
            }

            override fun onFailure(call: Call<List<Beer>>, t: Throwable) {
                Log.d("BeerListRepository", "getBeers() - Failure")
                data.value = NetworkResource.error(t.message!!, null)
            }
        })
        return data
    }

    /**
     * Update Beer info in DB
     */
    fun updateBeer(beer: Beer): LiveData<Beer> {
        val beerUpdated: MutableLiveData<Beer> = MutableLiveData()

        appExecutors.diskIO().execute {
            beerDao.update(beer)
            val dbBeer = beerDao.load(beer.id)

            appExecutors.mainThread().execute {
                beerUpdated.value = dbBeer
            }
        }

        return beerUpdated
    }
}
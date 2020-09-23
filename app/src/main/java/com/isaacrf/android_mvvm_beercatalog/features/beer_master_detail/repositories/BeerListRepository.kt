package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db.BeerDao
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.db.BeerDatabase
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.services.BeerListService
import com.isaacrf.android_mvvm_beercatalog.shared.api.NetworkBoundResource
import com.isaacrf.android_mvvm_beercatalog.shared.helpers.AppExecutors
import com.isaacrf.android_mvvm_beercatalog.shared.api.NetworkResource
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
        return object : NetworkBoundResource<List<Beer>, List<Beer>>(appExecutors) {
            override fun saveCallResult(result: List<Beer>) {
                beerDao.insert(result)
            }

            //Always fetch from network by default for this example purposes
            override fun shouldFetch(data: List<Beer>?): Boolean = true

            override fun loadFromDb() = beerDao.load()

            override fun apiCall() = beerListService.getBeers(1, 80)
        }.asMutableLiveData()
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
package com.isaacrf.android_base_app.features.beer_master_detail.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.isaacrf.android_base_app.features.beer_master_detail.models.Beer
import com.isaacrf.android_base_app.features.beer_master_detail.repositories.BeerListRepository
import com.isaacrf.android_base_app.shared.helpers.NetworkResource

class BeerListViewModel @ViewModelInject constructor(
    private val beerListRepository: BeerListRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    //Observable data list
    private val beers: MutableLiveData<NetworkResource<List<Beer>>> by lazy {
        loadBeers()
    }

    private fun loadBeers(): MutableLiveData<NetworkResource<List<Beer>>> {
        return beerListRepository.getBeers()
    }

    fun getBeers(): LiveData<NetworkResource<List<Beer>>> {
        return beers
    }

    fun getBeer(beerId: Int?): Beer? {
        return beers.value?.data?.find { beer -> beer.id == beerId }
    }

    fun changeAvailability(beerId: Int): LiveData<Beer> {
        val beer = beers.value?.data?.find { beer -> beer.id == beerId }
        beer?.available = !beer?.available!!
        refreshData()
        return beerListRepository.updateBeer(beer)
    }

    fun refreshData() {
        beers.value = beers.value
    }
}
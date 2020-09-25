package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.repositories.BeerListRepository
import com.isaacrf.android_mvvm_beercatalog.shared.api.NetworkResource

class BeerListViewModel @ViewModelInject constructor(
    private val beerListRepository: BeerListRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    //Observable data list
    private val _beers: MutableLiveData<NetworkResource<List<Beer>>> by lazy {
        loadBeers()
    }
    val beers: LiveData<NetworkResource<List<Beer>>>
        get() = _beers

    private fun loadBeers(): MutableLiveData<NetworkResource<List<Beer>>> {
        return beerListRepository.getBeers()
    }

    fun getBeer(beerId: Int?): Beer? {
        return _beers.value?.data?.find { beer -> beer.id == beerId }
    }

    fun changeAvailability(beerId: Int): LiveData<Beer> {
        val beer = _beers.value?.data?.find { beer -> beer.id == beerId }
        beer?.available = !beer?.available!!
        refreshData()
        return beerListRepository.updateBeer(beer)
    }

    private fun refreshData() {
        _beers.value = _beers.value
    }
}
package com.isaacrf.android_base_app.features.beer_list.viewmodels

import android.net.Network
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.isaacrf.android_base_app.features.beer_list.models.Beer
import com.isaacrf.android_base_app.features.beer_list.repositories.BeerListRepository
import com.isaacrf.android_base_app.shared.NetworkResource

class BeerListViewModel @ViewModelInject constructor(
    private val beerListRepository: BeerListRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    //Observable data list
    private val beers: MutableLiveData<NetworkResource<List<Beer>>> by lazy {
        loadBeers()
    }

    fun getBeers(): LiveData<NetworkResource<List<Beer>>> {
        return beers
    }

    private fun loadBeers(): MutableLiveData<NetworkResource<List<Beer>>> {
        return beerListRepository.getBeers()
    }

    fun changeAvailability(beerId: Int) {
        val beer = beers.value?.data?.find { beer -> beer.id == beerId }
        beer?.available = !beer?.available!!
        beers.value = beers.value
    }
}
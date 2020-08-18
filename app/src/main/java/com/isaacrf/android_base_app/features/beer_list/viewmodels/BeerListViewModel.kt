package com.isaacrf.android_base_app.features.beer_list.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.isaacrf.android_base_app.features.beer_list.models.Beer
import com.isaacrf.android_base_app.features.beer_list.repositories.BeerListRepository
import com.isaacrf.android_base_app.shared.NetworkResource

class BeerListViewModel @ViewModelInject constructor (
    private val beerListRepository: BeerListRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    val beerList: LiveData<NetworkResource<List<Beer>>> = beerListRepository.getBeers()
}
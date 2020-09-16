package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer

class BeerDetailViewModel @ViewModelInject constructor(
    @Assisted private val state: SavedStateHandle
): ViewModel() {
    private val beer: MutableLiveData<Beer> by lazy {
        MutableLiveData<Beer>()
    }

    fun getBeer(): LiveData<Beer> {
        return beer
    }

    fun setBeer(beer: Beer?) {
        this.beer.value = beer
    }

    fun changeAvailability() {
        this.beer.value?.available = !this.beer.value?.available!!
        this.beer.value = this.beer.value
    }
}
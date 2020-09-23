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
    private val _beer: MutableLiveData<Beer> by lazy {
        MutableLiveData<Beer>()
    }
    val beer: LiveData<Beer>
        get() = _beer

    fun setBeer(beer: Beer?) {
        this._beer.value = beer
    }

    fun changeAvailability() {
        this._beer.value?.available = !this._beer.value?.available!!
        this._beer.value = this._beer.value
    }
}
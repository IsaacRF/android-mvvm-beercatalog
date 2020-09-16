package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.viewmodels

import com.isaacrf.android_mvvm_beercatalog.helpers.MockBeersHelper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import junit.framework.Assert.assertTrue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class BeerDetailViewModelTest: MockBeersHelper() {
    @Rule
    @JvmField
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var state: SavedStateHandle

    @Mock
    private lateinit var observer: Observer<Beer>
    private lateinit var beerDetailViewModel: BeerDetailViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val mockBeers = getMockBeers()

        MockitoAnnotations.initMocks(this)

        beerDetailViewModel = BeerDetailViewModel(state)
        beerDetailViewModel.getBeer().observeForever(observer)
        beerDetailViewModel.setBeer(mockBeers.get(0))
    }

    @Test
    fun `Test Beer not Null and observer attached`() {
        Assert.assertNotNull("beer LiveData is null", beerDetailViewModel.getBeer())
        Assert.assertTrue(
            "beer has no observer attached",
            beerDetailViewModel.getBeer().hasObservers()
        )
    }

    @Test
    fun `Update Beer Availability Test`() {
        assertTrue(beerDetailViewModel.getBeer().value?.available!!)
        beerDetailViewModel.changeAvailability()
        assertTrue(!beerDetailViewModel.getBeer().value?.available!!)
    }
}
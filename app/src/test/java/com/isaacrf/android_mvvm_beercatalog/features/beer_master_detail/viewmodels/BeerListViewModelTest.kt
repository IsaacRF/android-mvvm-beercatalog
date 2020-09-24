package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.viewmodels

import com.isaacrf.android_mvvm_beercatalog.helpers.MockBeersHelper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.repositories.BeerListRepository
import com.isaacrf.android_mvvm_beercatalog.shared.api.NetworkResource
import com.isaacrf.android_mvvm_beercatalog.shared.api.Status
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


/**
 * Repo List ViewModel test
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BeerListViewModelTest: MockBeersHelper() {
    @Rule
    @JvmField
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var beerListRepository: BeerListRepository

    @Mock
    private lateinit var state: SavedStateHandle

    @Mock
    private lateinit var observer: Observer<NetworkResource<List<Beer>>>
    private lateinit var beerListViewModel: BeerListViewModel

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `Test Beer List not Null and observer attached`() {
        `when`(beerListRepository.getBeers()).thenReturn(MutableLiveData<NetworkResource<List<Beer>>>())
        beerListViewModel = BeerListViewModel(beerListRepository, state)
        beerListViewModel.beers.observeForever(observer)
        assertNotNull("beerList LiveData is null", beerListViewModel.beers)
        assertTrue("beerList has no observer attached", beerListViewModel.beers.hasObservers())
    }

    @Test
    fun `Get Beers Test - Success`() {
        val mockBeers = getMockBeers()
        val mockData: MutableLiveData<NetworkResource<List<Beer>>> =
            MutableLiveData(
                NetworkResource(
                    Status.SUCCESS,
                    mockBeers,
                    ""
                )
            )

        `when`(beerListRepository.getBeers()).thenReturn(mockData)
        beerListViewModel = BeerListViewModel(beerListRepository, state)
        beerListViewModel.beers.observeForever(observer)

        //TODO: TEST DB
        //val test = BeerRoomDao.load(1)

        verify(observer).onChanged(mockData.value)
        assertNotNull("beerList LiveData is null", beerListViewModel.beers)
        assertNotNull("beerList value is null", beerListViewModel.beers.value)
        assertTrue(
            "beerList value has incorrect size. Expected: 20 / Got: ${beerListViewModel.beers.value?.data?.size}",
            beerListViewModel.beers.value?.data?.size == 20
        )
        assertTrue(beerListViewModel.beers.value?.data?.get(0)?.name == "Buzz")
        assertTrue(beerListViewModel.beers.value?.data?.get(1)?.name == "Trashy Blonde")
    }

    @Test
    fun `Get Beers Test - fail`() {
        //Empty response body
        val mockBeers: List<Beer>? = null
        val mockData: MutableLiveData<NetworkResource<List<Beer>>> =
            MutableLiveData<NetworkResource<List<Beer>>>(
                NetworkResource(
                    Status.ERROR,
                    mockBeers,
                    "Test failure"
                )
            )

        `when`(beerListRepository.getBeers()).thenReturn(mockData)
        beerListViewModel = BeerListViewModel(beerListRepository, state)
        beerListViewModel.beers.observeForever(observer)

        verify(observer).onChanged(mockData.value)
        assertNull(beerListViewModel.beers.value?.data)
    }

    @Test
    fun `Update Availability Test`() {
        val mockBeers = getMockBeers()
        val mockData: MutableLiveData<NetworkResource<List<Beer>>> =
            MutableLiveData(
                NetworkResource(
                    Status.SUCCESS,
                    mockBeers,
                    ""
                )
            )

        `when`(beerListRepository.getBeers()).thenReturn(mockData)
        beerListViewModel = BeerListViewModel(beerListRepository, state)
        beerListViewModel.beers.observeForever(observer)

        beerListViewModel.changeAvailability(1)
        assertTrue(!beerListViewModel.beers.value?.data?.find { beer -> beer.id == 1 }!!.available)

        beerListViewModel.changeAvailability(1)
        assertTrue(beerListViewModel.beers.value?.data?.find { beer -> beer.id == 1 }!!.available)
    }
}
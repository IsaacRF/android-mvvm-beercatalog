package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.services

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.isaacrf.android_mvvm_beercatalog.helpers.getOrAwaitValue
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.isaacrf.android_mvvm_beercatalog.shared.api.ApiSuccessResponse
import com.isaacrf.android_mvvm_beercatalog.shared.helpers.RetrofitLiveDataCallAdapterFactory
import junit.framework.Assert.*
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

/**
 * Beer List Service test
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BeerListServiceTest {
    @Rule @JvmField
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var gson: Gson
    private lateinit var beerListService: BeerListRetrofitService

    @Before
    @Throws(Exception::class)
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        gson = GsonBuilder()
            .setLenient()
            .create()
        beerListService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RetrofitLiveDataCallAdapterFactory())
            .build()
            .create(BeerListRetrofitService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getBeers() - Call Success`() {
        enqueueResponse("BeersResponseSample.json")

        val response = (beerListService.getBeers(1, 20).getOrAwaitValue() as ApiSuccessResponse)
        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/beers?page=1&per_page=20"))
        assertTrue(response.body.size == 20)

        val beer = response.body[0]
        assertNotNull(beer)
        assertThat(beer.id, `is`(1))
        assertThat(beer.name, `is`("Buzz"))
        assertThat(beer.tagline, `is`("A Real Bitter Experience."))
        assertThat(beer.description, `is`("A light, crisp and bitter IPA brewed with English and American hops. A small batch brewed only once."))
        assertThat(beer.alcoholByVolume, `is`(4.5))
        assertTrue(beer.foodPairing?.size == 3)
        assertThat(beer.bitterness, `is`(60.0))
        assertThat(beer.imageUrl, `is`("https://images.punkapi.com/v2/keg.png"))

        val beer2 = response.body[1]
        assertNotNull(beer2)
        assertThat(beer2.id, `is`(2))
        assertThat(beer2.name, `is`("Trashy Blonde"))
    }

    /**
     * Helper method to enqueue mock responses
     */
    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap(), responseCode: Int = HttpURLConnection.HTTP_OK, networkStatus: SocketPolicy? = null) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("mock_responses/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()

        if (networkStatus != null) {
            mockResponse.socketPolicy = networkStatus
        }
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
                .setResponseCode(responseCode)
        )
    }
}
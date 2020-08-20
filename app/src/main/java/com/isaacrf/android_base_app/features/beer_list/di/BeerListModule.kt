package com.isaacrf.android_base_app.features.beer_list.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.isaacrf.android_base_app.features.beer_list.db.BeerDao
import com.isaacrf.android_base_app.features.beer_list.db.BeerDatabase
import com.isaacrf.android_base_app.features.beer_list.models.Beer
import com.isaacrf.android_base_app.features.beer_list.services.BeerDeserializer
import com.isaacrf.android_base_app.features.beer_list.services.BeerListService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Provides Beer List Service instantiations for injections
 */
@Module
@InstallIn(ApplicationComponent::class)
object BeerListModule {
    /**
     * Builds and returns the Gson converter
     */
    private fun getGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(Beer::class.java, BeerDeserializer())
            .setLenient()
            .create()
    }

    /**
     * Builds and returns the OkHttpClient
     */
    private fun getOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    /**
     * Builds and returns the Retrofit client
     *
     * @param baseUrl API endpoint
     */
    private fun getRetrofit(baseUrl: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Provides instantiation for http client interface
     */
    @Provides
    @Singleton
    fun provideBeerListService(): BeerListService {
        val gson = getGson()
        val okHttpClient = getOkHttpClient()
        val retrofit = getRetrofit("https://api.punkapi.com/v2/", okHttpClient, gson)

        return retrofit.create(BeerListService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): BeerDatabase {
        return Room
            .databaseBuilder(app, BeerDatabase::class.java, "beer.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideBeerDao(db: BeerDatabase): BeerDao {
        return db.beerDao()
    }
}
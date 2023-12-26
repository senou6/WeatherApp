package pt.pedro.ccti.weatherapp.network

import pt.pedro.ccti.weatherapp.model.Search.Search
import pt.pedro.ccti.weatherapp.utils.Constants.API_KEY
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherApi {
    @GET(value = "v1/forecast.json")
    suspend fun getWeather(
        @Query("q") query : String,
        @Query("key") appid: String = API_KEY,
        @Query("days") days: String = "6",
    ): Weather

    @GET(value = "v1/search.json")
    suspend fun searchWeather(
        @Query("q") query : String,
        @Query("key") appid: String = API_KEY
    ): Search
}
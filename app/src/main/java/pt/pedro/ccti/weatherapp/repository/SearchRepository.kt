package pt.pedro.ccti.weatherapp.repository

import android.util.Log
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.di.RegionSearchApi
import pt.pedro.ccti.weatherapp.model.Search.Search
import pt.pedro.ccti.weatherapp.network.WeatherApi
import javax.inject.Inject

class SearchRepository @Inject constructor(@RegionSearchApi private val api: WeatherApi) {

    suspend fun getSearchResults(stringQuery: String)
            : DataOrException<Search, Boolean, Exception> {
        val response = try {
            api.searchWeather(query = stringQuery)

        }catch (e: Exception){
            return DataOrException(e = e)
        }
        return  DataOrException(data = response)

    }

}
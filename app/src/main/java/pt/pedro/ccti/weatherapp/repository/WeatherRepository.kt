package pt.pedro.ccti.weatherapp.repository

import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.di.RegularApi
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.network.WeatherApi

import javax.inject.Inject

class WeatherRepository @Inject constructor(@RegularApi private val api: WeatherApi) {

    suspend fun getWeather(cityQuery: String)
            : DataOrException<Weather, Boolean, Exception> {
        val response = try {
            api.getWeather(query = cityQuery)

        }catch (e: Exception){
            return DataOrException(e = e)
        }
        return  DataOrException(data = response)

    }

}
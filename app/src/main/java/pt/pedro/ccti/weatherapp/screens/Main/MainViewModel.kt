package pt.pedro.ccti.weatherapp.screens.mainscreen

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import pt.pedro.ccti.weatherapp.domain.location.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.domain.use_cases.LocationUtils
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.repository.WeatherRepository
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
) : ViewModel() {


    // Fetches weather data for a given city
    suspend fun getWeatherData(context : Context): DataOrException<Weather, Boolean, Exception> {
        val location = LocationUtils.getAddressFromCoordinates(context,locationTracker)
        Log.d("TAG", "getWeatherData: $location")
        return repository.getWeather(cityQuery = location)
    }
}




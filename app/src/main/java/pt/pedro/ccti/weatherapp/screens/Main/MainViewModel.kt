package pt.pedro.ccti.weatherapp.screens.mainscreen

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pt.pedro.ccti.weatherapp.domain.location.LocationTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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
    private val _currentLocation = MutableStateFlow("")
    val currentLocation = _currentLocation.asStateFlow()

    private val _weatherData = MutableStateFlow<DataOrException<Weather, Boolean, Exception>?>(null)
    val weatherData: StateFlow<DataOrException<Weather, Boolean, Exception>?> = _weatherData.asStateFlow()

    init {
        viewModelScope.launch {
            currentLocation.collect { location ->
                if (location.isNotEmpty()) {
                    _weatherData.value = getWeatherData(location)
                }
            }
        }
    }

    suspend fun getWeatherData(location: String): DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather(cityQuery = location)
    }

    fun startLocationUpdates(context: Context) {
        viewModelScope.launch {
            while (isActive) {
                updateLocation(context)
                delay(50000)
            }
        }
    }

    private suspend fun updateLocation(context: Context) {
        val location = LocationUtils.getAddressFromCoordinates(context, locationTracker)
        _currentLocation.value = location
        _weatherData.value = getWeatherData(location)
    }
}



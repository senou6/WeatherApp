package pt.pedro.ccti.weatherapp.screens.homescreen
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.repository.WeatherRepository
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _weatherData = MutableStateFlow<List<DataOrException<Weather, Boolean, Exception>?>>(emptyList())
    val weatherData: StateFlow<List<DataOrException<Weather, Boolean, Exception>?>> = _weatherData.asStateFlow()

    private val _hasFavoriteLocation = MutableStateFlow(true)
    val hasFavoriteLocation: StateFlow<Boolean> = _hasFavoriteLocation.asStateFlow()

    fun fetchUserPrincipalLocation() {
        viewModelScope.launch {
            _weatherData.value = emptyList()
            val userId = firebaseAuth.currentUser?.uid ?: return@launch
            try {
                val querySnapshot = firestore
                    .collection("users")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .await()

                val document = querySnapshot.documents.firstOrNull()
                if (document != null) {
                    val favoriteLocations = document.get("favorite_locations") as? List<String>
                    if (!favoriteLocations.isNullOrEmpty()) {
                        _hasFavoriteLocation.value = true
                        getWeatherDataForLocation(favoriteLocations)
                    }
                } else {
                    _hasFavoriteLocation.value = false
                }
            } catch (e: Exception) {
                _hasFavoriteLocation.value = false
                Log.e("TAG", "Error fetching user favorite location", e)
            }
        }
    }

    private suspend fun getWeatherDataForLocation(locations: List<String>) {
        viewModelScope.launch {
            val weatherDataResults = locations.map { location ->
                async { repository.getWeather(cityQuery = location) }
            }.awaitAll()

            _weatherData.value = weatherDataResults
        }
    }
}



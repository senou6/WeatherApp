package pt.pedro.ccti.weatherapp.screens.SearchedLocation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.domain.location.LocationTracker
import pt.pedro.ccti.weatherapp.domain.use_cases.LocationUtils
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.repository.SearchRepository
import pt.pedro.ccti.weatherapp.repository.WeatherRepository
import javax.inject.Inject




@HiltViewModel
class SearchedLocationViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    val isFavorite = MutableStateFlow(false)
    private val _weatherData = MutableStateFlow<DataOrException<Weather, Boolean, Exception>?>(null)
    val weatherData: StateFlow<DataOrException<Weather, Boolean, Exception>?> = _weatherData.asStateFlow()

    fun fetchUserSearchedLocation(location : String) {
        viewModelScope.launch {
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
                    isFavorite.value = location in favoriteLocations.orEmpty()
                }
                getWeatherDataForLocation(location)
            } catch (e: Exception) {
                isFavorite.value = false
                Log.e("ViewModelLog", "Error fetching location", e)
            }
        }
    }

    fun removeFavorite(favoriteToRemove: String) {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch
            try {
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .await()

                val userDocument = querySnapshot.documents.firstOrNull()
                if (userDocument != null) {
                    firestore.runTransaction { transaction ->
                        val favorites = userDocument.get("favorite_locations") as? List<String> ?: return@runTransaction
                        if (favoriteToRemove in favorites) {
                            val updatedFavorites = favorites.toMutableList().apply {
                                remove(favoriteToRemove)
                            }
                            transaction.update(userDocument.reference, "favorite_locations", updatedFavorites)
                        }
                    }.await()
                }
            } catch (e: Exception) {
                Log.e("ViewModelLog", "Error removing favorite location", e)
            }
        }
    }

    fun addFavorite(favoriteToAdd: String) {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch
            try {
                val querySnapshot = firestore.collection("users")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .await()

                val userDocument = querySnapshot.documents.firstOrNull()
                if (userDocument != null) {
                    firestore.runTransaction { transaction ->
                        val favorites = userDocument.get("favorite_locations") as? MutableList<String> ?: mutableListOf()
                        if (favoriteToAdd !in favorites) {
                            favorites.add(favoriteToAdd)
                            transaction.update(userDocument.reference, "favorite_locations", favorites)
                        }
                    }.await()
                }
            } catch (e: Exception) {
                Log.e("ViewModelLog", "Error adding favorite location", e)
            }
        }
    }

    private suspend fun getWeatherDataForLocation(location: String) {
        _weatherData.value = repository.getWeather(cityQuery = location)
    }
}




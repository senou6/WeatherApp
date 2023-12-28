package pt.pedro.ccti.weatherapp.screens.FavoriteLocations

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pt.pedro.ccti.weatherapp.repository.WeatherRepository
import javax.inject.Inject


@HiltViewModel
class FavoriteLocationsViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _userFavoriteLocations =  MutableStateFlow<List<String>>(emptyList())
    val userFavoriteLocations = _userFavoriteLocations.asStateFlow()

    fun fetchUserPrincipalLocation() {
        viewModelScope.launch {
            _userFavoriteLocations.value = emptyList()
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
                        _userFavoriteLocations.value = favoriteLocations
                    }
                }
            } catch (e: Exception) {

                Log.e("TAG", "Error fetching user favorite locations", e)
            }
        }
    }

}


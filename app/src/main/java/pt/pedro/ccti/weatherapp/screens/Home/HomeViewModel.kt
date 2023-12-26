package pt.pedro.ccti.weatherapp.screens.homescreen
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
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.repository.WeatherRepository
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel(){

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _userPrincipalLocation = MutableStateFlow<String?>(null)
    val userPrincipalLocation = _userPrincipalLocation.asStateFlow()


    fun fetchUserFavouriteLocation() {
        viewModelScope.launch {
            val userId = firebaseAuth.currentUser?.uid ?: return@launch
            try {
                val querySnapshot = FirebaseFirestore.getInstance()
                    .collection("users")
                    .whereEqualTo("user_id", userId)
                    .get()
                    .await()

                val document = querySnapshot.documents.firstOrNull() // Assuming there's only one document per user
                if (document != null) {
                    _userPrincipalLocation.value = document.getString("principal_location")
                }
            } catch (e: Exception) {
                Log.e("TAG", "Error fetching user favorite location", e)
            }
        }
    }

    suspend fun getWeatherData(): DataOrException<Weather, Boolean, Exception> {
            return repository.getWeather(cityQuery = userPrincipalLocation.value!!)
    }

}
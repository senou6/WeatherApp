package pt.pedro.ccti.weatherapp.screens.registerscreen

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.wait
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.domain.location.LocationTracker
import pt.pedro.ccti.weatherapp.domain.use_cases.LocationUtils
import pt.pedro.ccti.weatherapp.model.Search.Search
import pt.pedro.ccti.weatherapp.model.User.MUser
import pt.pedro.ccti.weatherapp.repository.SearchRepository
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val locationTracker: LocationTracker,
    private val repository: SearchRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private val _searchData = MutableStateFlow("")

    fun registerUser(
        email: String,
        password: String,
        repeatPassword: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!validateRegistrationInput(email, password, repeatPassword, onError)) return

        isLoading = true
        viewModelScope.launch {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val displayName = result.user?.email?.split("@")?.get(0) ?: ""
                createUser(displayName, context)
                onSuccess()
            } catch (e: Exception) {
                onError(e.localizedMessage ?: "An unknown error occurred")
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun performSearch(context : Context) {
        val location = LocationUtils.getAddressFromCoordinates(context, locationTracker)
        try {
            val result = repository.getSearchResults(stringQuery = location)
            if (result.data?.isNotEmpty() == true) {
                _searchData.value = "${result.data!![0].name},${result.data!![0].country},${result.data!![0].url}"

            }
        } catch (e: Exception) {
            Log.e("Error", "performSearch: Error", e)
        }
    }

    private suspend fun createUser(displayName: String, context : Context) {
        performSearch(context)
        val userId = firebaseAuth.currentUser?.uid ?: return
        val user = MUser(userId = userId, username = displayName, favoriteLocations = listOf(_searchData.value), id = null).toMap()

        try {
            firestore.collection("users").add(user)
        } catch (e: Exception) {
            Log.e("Error", "createUser: Error adding user to Firestore", e)
        }
    }

    private fun validateRegistrationInput(
        email: String, password: String, repeatPassword: String, onError: (String) -> Unit
    ): Boolean {
        when {
            email.isBlank() || password.isBlank() || repeatPassword.isBlank() -> {
                onError("Email and password cannot be empty")
                return false
            }
            password != repeatPassword -> {
                onError("Passwords do not match")
                return false
            }
            !email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()) -> {
                onError("Invalid Email")
                return false
            }
        }
        return true
    }
}

package pt.pedro.ccti.weatherapp.screens.registerscreen

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import pt.pedro.ccti.weatherapp.domain.location.LocationTracker
import pt.pedro.ccti.weatherapp.domain.use_cases.LocationUtils
import pt.pedro.ccti.weatherapp.model.User.MUser
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val locationTracker: LocationTracker) : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var isLoading by mutableStateOf(false)
        private set

    fun registerUser(
        email: String,
        password: String,
        repeatPassword: String,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,

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

    private suspend fun createUser(displayName: String, context : Context) {
        val location = LocationUtils.getAddressFromCoordinates(context,locationTracker)
        val userId = firebaseAuth.currentUser?.uid
        val user = MUser(userId = userId.toString(), username = displayName, principalLocaltion = location, favoriteLocations = listOf(), id = null).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)

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

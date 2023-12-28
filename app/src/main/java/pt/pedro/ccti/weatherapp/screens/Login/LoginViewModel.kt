package pt.pedro.ccti.weatherapp.screens.loginscreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {

        if (email.isBlank() || password.isBlank()) {
            onError("Email and password cannot be empty")
            return
        }
        if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex())){
            onError("Invalid Email")
            return
        }

        isLoading = true

        viewModelScope.launch {
            try {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    isLoading = false
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError(task.exception?.message ?: "An unknown error occurred")
                    }
                }
            } catch (e: Exception) {
                isLoading = false
                onError(e.message ?: "An unknown error occurred")
            }
        }
    }
}

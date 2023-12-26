package pt.pedro.ccti.weatherapp


import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import pt.pedro.ccti.weatherapp.components.Loading
import pt.pedro.ccti.weatherapp.navigation.WeatherNavigation
import pt.pedro.ccti.weatherapp.ui.theme.WeatherAppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissionResponseReceived = mutableStateOf(false)

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permissionResponseReceived.value = isGranted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request permission if not already granted
        if (!isPermissionGranted()) {
            locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Set content based on permission status
        setContent {
            if (permissionResponseReceived.value || isPermissionGranted()) {
                WeatherApp()
            } else {
                Loading()
            }
        }
    }

    // Helper function to check permission
    private fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

}

@SuppressLint("MissingPermission")
@Composable
fun WeatherApp(){

    WeatherAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                WeatherNavigation()
            }

        }
    }

}
package pt.pedro.ccti.weatherapp.screens.mainscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.asStateFlow
import pt.pedro.ccti.weatherapp.components.Loading
import pt.pedro.ccti.weatherapp.components.UserScaffold
import pt.pedro.ccti.weatherapp.components.WeatherScaffold
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.navigation.WeatherScreens
import pt.pedro.ccti.weatherapp.utils.getThemeForWeather


@Composable
fun MainScreen(
        navController: NavController,
        mainViewModel: MainViewModel = hiltViewModel()
) {
        val context = LocalContext.current
        val weatherData = mainViewModel.weatherData.collectAsState().value
        val currentLocation = mainViewModel.currentLocation.collectAsState()

        LaunchedEffect(Unit) {
                mainViewModel.startLocationUpdates(context)
        }

        UserScaffold(false,navController, WeatherScreens.MainScreen.name) {
                when {
                        weatherData == null || weatherData.loading == true -> Loading()
                        weatherData.data != null ->

                                WeatherScaffold(weatherData.data!!, navController)

                        else -> Text(text = "error")
                }
        }
}



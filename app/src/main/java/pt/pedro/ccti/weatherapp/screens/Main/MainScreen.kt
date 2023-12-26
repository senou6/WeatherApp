package pt.pedro.ccti.weatherapp.screens.mainscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pt.pedro.ccti.weatherapp.components.Loading
import pt.pedro.ccti.weatherapp.components.WeatherScaffold
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.model.Weather.Weather

@Composable
fun MainScreen(
        navController: NavController,
        mainViewModel: MainViewModel = hiltViewModel(),

        ) {
        val oContexto = LocalContext.current

        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
                initialValue = DataOrException(loading = true)
        ) {
                value = mainViewModel.getWeatherData(oContexto)
        }.value

        if (weatherData.loading == true) {
                Loading()
        } else if (weatherData.data != null) {
                // Display weather and location data
                Column{
                        WeatherScaffold(weatherData.data!!,navController, false)
                }

        }
}




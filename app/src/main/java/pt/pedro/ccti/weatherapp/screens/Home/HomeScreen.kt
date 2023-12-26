package pt.pedro.ccti.weatherapp.screens.homescreen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pt.pedro.ccti.weatherapp.components.Loading
import pt.pedro.ccti.weatherapp.components.WeatherScaffold
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.model.Weather.Weather


@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel= viewModel()) {

    val userFavouriteLocation by homeViewModel.userPrincipalLocation.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.fetchUserFavouriteLocation()
    }


    if(userFavouriteLocation != null) {
        Log.d("TAG", "HomeScreen:$userFavouriteLocation ")
        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = homeViewModel.getWeatherData()!!
        }.value

        if (weatherData.loading == true) {
            Loading()
        } else if (weatherData.data != null) {
            WeatherScaffold(weatherData.data!!,navController, true)
        }
    }else{
        Loading()
    }


}
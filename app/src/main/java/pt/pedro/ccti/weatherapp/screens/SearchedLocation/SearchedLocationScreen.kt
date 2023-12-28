package pt.pedro.ccti.weatherapp.screens.SearchedLocation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pt.pedro.ccti.weatherapp.components.Loading
import pt.pedro.ccti.weatherapp.components.UserScaffold
import pt.pedro.ccti.weatherapp.components.WeatherScaffold
import pt.pedro.ccti.weatherapp.data.DataOrException
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.navigation.WeatherScreens
import pt.pedro.ccti.weatherapp.screens.homescreen.HomeViewModel




@Composable
fun SearchedLocationScreen(
    navController: NavController,
    location : String,
    searchLocationViewModel: SearchedLocationViewModel = hiltViewModel()
) {
    val weatherData = searchLocationViewModel.weatherData.collectAsState().value
    val isFavorite by searchLocationViewModel.isFavorite.collectAsState()

    LaunchedEffect(Unit,location) {
        searchLocationViewModel.fetchUserSearchedLocation(location)
    }
    when {
        weatherData == null || weatherData.loading == true -> Loading()
        weatherData.data != null ->
            UserScaffold(true,navController, WeatherScreens.SearchedLocationScreen.name) {
                FavoriteFunction(searchLocationViewModel,location, isFavorite)
                WeatherScaffold(weatherData.data!!, navController)
            }
        else -> Text(text = "Unable to load data. Please try again.")
    }
}



@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FavoriteFunction(searchLocationViewModel: SearchedLocationViewModel = viewModel(), location : String, isFavorite : Boolean){
    val favorite = remember { mutableStateOf(isFavorite) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 0.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        IconButton(onClick = {
            if (favorite.value) {
                searchLocationViewModel.removeFavorite(location)
            } else {
                searchLocationViewModel.addFavorite(location)
            }
            favorite.value = !favorite.value
        }) {
            AnimatedContent(
                targetState = favorite.value,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 500)) with
                            fadeOut(animationSpec = tween(durationMillis = 500))
                }, label = "Fade Star"
            ) { targetState ->
                if (targetState) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Remove from Favorite",
                        modifier = Modifier.size(32.5.dp),
                        tint = Color.Yellow
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.StarBorder,
                        contentDescription = "Add to Favorite",
                        modifier = Modifier.size(32.5.dp),
                        tint = Color.Yellow
                    )
                }
            }
        }
    }
}

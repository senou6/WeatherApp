package pt.pedro.ccti.weatherapp.screens.homescreen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pt.pedro.ccti.weatherapp.components.Loading
import pt.pedro.ccti.weatherapp.components.UserScaffold
import pt.pedro.ccti.weatherapp.components.WeatherScaffold
import pt.pedro.ccti.weatherapp.navigation.WeatherScreens


//@Composable
//fun HomeScreen(
//    navController: NavController,
//    homeViewModel: HomeViewModel = hiltViewModel()
//) {
//    val weatherData = homeViewModel.weatherData.collectAsState().value
//    val hasFavoriteLocation = homeViewModel.hasFavoriteLocation.collectAsState().value
//
//    LaunchedEffect(Unit) {
//        homeViewModel.fetchUserPrincipalLocation()
//    }
//
//    when {
//        weatherData == null || weatherData.loading == true -> Loading()
//        weatherData.data != null ->
//            UserScaffold(true,navController, WeatherScreens.HomeScreen.name) {
//                WeatherScaffold(weatherData.data!!, navController)
//            }
//        !hasFavoriteLocation -> Text(text="No fav")
//        else -> Text(text = "error")
//    }
//}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val weatherData = homeViewModel.weatherData.collectAsState().value
    val hasFavoriteLocation = homeViewModel.hasFavoriteLocation.collectAsState().value

    LaunchedEffect(Unit) {
        homeViewModel.fetchUserPrincipalLocation()
    }

    UserScaffold(true, navController, WeatherScreens.HomeScreen.name) {
        if (weatherData.isNotEmpty()) {
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                weatherData.size
            }

            HorizontalPager(
                modifier = Modifier,
                state = pagerState,
                pageSpacing = 0.dp,
                userScrollEnabled = true,
                reverseLayout = false,
                contentPadding = PaddingValues(0.dp),
                beyondBoundsPageCount = 0,
                pageSize = PageSize.Fill,
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                key = { index -> weatherData[index].hashCode() },
                pageNestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
                    Orientation.Horizontal
                )
            ) { index ->
                weatherData.getOrNull(index)?.data?.let { data ->
                    WeatherScaffold(data, navController)
                } ?: Text(text = "No Data")
            }
        } else if (hasFavoriteLocation) {
            Loading()
        } else {
            Text(text = "User has no favorite locations")
        }
    }
}



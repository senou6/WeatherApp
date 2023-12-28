package pt.pedro.ccti.weatherapp.screens.FavoriteLocations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pt.pedro.ccti.weatherapp.navigation.WeatherScreens


@Composable
fun FavoriteLocationsScreen(
    navController: NavController,
    favoriteLocationsViewModel: FavoriteLocationsViewModel = hiltViewModel()
) {
    val userFavoriteLocations by favoriteLocationsViewModel.userFavoriteLocations.collectAsState()

    LaunchedEffect(Unit){
        favoriteLocationsViewModel.fetchUserPrincipalLocation()
    }

    LazyColumn {
        items(userFavoriteLocations) { location ->
            LocationRow(navController,location = location)
        }
    }
}
// https://www.youtube.com/watch?v=ro3a-GmaLqE
@Composable
fun LocationRow(navController: NavController,location: String) {
    val locationName = location.split(",")[0]
    val locationCountry = location.split(",")[1]
    val locationUrl = location.split(",")[2]
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(20.dp))
            .padding(5.dp, 5.dp)
            .background(Color(0x43EBE4E4))
            .clickable { navController.navigate(WeatherScreens.SearchedLocationScreen.withArgs("${locationName},${locationCountry},${locationUrl}")) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "${locationName},${locationCountry}", modifier = Modifier.padding(10.dp, 0.dp))
    }

}
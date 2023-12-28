package pt.pedro.ccti.weatherapp.screens.searchscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import pt.pedro.ccti.weatherapp.model.Search.SearchItem
import pt.pedro.ccti.weatherapp.navigation.WeatherScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, searchViewModel: SearchViewModel = hiltViewModel()) {
    val searchText by searchViewModel.searchText.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    LaunchedEffect(searchText) {
        if (searchText.isNotBlank() && searchText.length >= 3) {
            delay(1000)
            searchViewModel.performSearch()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()) {
        Row{
            OutlinedTextField(
                value = searchText,
                onValueChange = searchViewModel::onSearchTextChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp, 2.5.dp)
                    .focusRequester(focusRequester),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                leadingIcon = {IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Search")
                }},
                maxLines = 1,
                singleLine = true,
            )
        }
        ShowData(searchViewModel, navController)
    }
}


@Composable
fun ShowData(searchViewModel: SearchViewModel, navController: NavController) {
    val searchData = searchViewModel.searchData.collectAsState().value

    if (searchData.data != null) {
        searchData.data!!.forEach { item: SearchItem ->
            SearchResult(item,navController)
            Divider()
        }
    } else if (searchData.e != null) {
        // Handle the exception case
        Text("Error: ${searchData.e}")
    }
}


@Composable
fun SearchResult(item: SearchItem,navController: NavController){
    Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically,modifier = Modifier
        .fillMaxWidth()
        .height(65.dp)
        .padding(25.dp, 0.dp)
        .verticalScroll(rememberScrollState())
        .clickable {
            navController.navigate(WeatherScreens.SearchedLocationScreen.withArgs("${item.name},${item.country},${item.url}"))
        }
    ){
        Text(text="${item.name}, ${item.region}, ${item.country}")
    }
}
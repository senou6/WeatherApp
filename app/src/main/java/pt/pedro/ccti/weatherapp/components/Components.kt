package pt.pedro.ccti.weatherapp.components

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import pt.pedro.ccti.weatherapp.R
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.navigation.WeatherScreens
import pt.pedro.ccti.weatherapp.utils.epochToWeekDay


@Composable
fun UnauthenticatedTopBar(navController: NavController, currentScreen: String, content: @Composable () -> Unit){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if(currentScreen == "MainScreen"){
            Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    navController.navigate(WeatherScreens.LoginScreen.name)
                }) {
                    Icon(Icons.Default.Login, contentDescription = "Search", tint = Color.White, modifier = Modifier.size(32.5.dp))
                }
            }
        }else{
            Row( verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth())  {
                IconButton(onClick = {
                    navController.navigate(WeatherScreens.MainScreen.name)
                }) {
                    Icon(Icons.Default.ArrowBackIos, contentDescription = "Back", tint = Color.White, modifier = Modifier.size(32.5.dp))
                }
            }
        }

    }

}
@Composable
fun AuthenticatedTopBar(navController: NavController, currentScreen: String, content: @Composable () -> Unit){

    var expanded by remember { mutableStateOf(false) }
    val items = when (currentScreen) {
        "FavoriteLocationsScreen" -> {
            listOf("Home", "Logout")
        }
        "HomeScreen" -> {
            listOf("Favorites", "Logout")
        }
        else -> {
            listOf("Home","Favorites", "Logout")
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            navController.navigate(WeatherScreens.SearchScreen.name)
        }) {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White, modifier = Modifier.size(32.5.dp))
        }

        Column {
            IconButton(onClick = { expanded = true
            }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White, modifier = Modifier.size(32.5.dp))
            }
            Box(modifier = Modifier
                .offset(x=40.dp,y= (-40).dp)) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },

                    ) {
                    items.forEachIndexed { index, item ->
                        DropdownMenuItem(onClick = {
                            if (item == "Favorites") {
                                Log.d("TAG", "AuthenticatedTopBar: $item")
                                expanded = false
                                navController.navigate(WeatherScreens.FavoriteLocationsScreen.name)


                            } else if(item == "Home" ) {
                                Log.d("TAG", "AuthenticatedTopBar: $item")
                                expanded = false
                                navController.navigate(WeatherScreens.HomeScreen.name)


                            }else{
                                expanded = false
                                FirebaseAuth.getInstance().signOut().run {
                                    navController.navigate(WeatherScreens.MainScreen.name) {
                                        popUpTo("authenticated_graph") {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }) {
                            Text(text = item)
                        }
                    }
                }
            }
        }

    }

}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserScaffold(loggedIn: Boolean, navController: NavController, currentScreen: String, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            if (loggedIn) {
                AuthenticatedTopBar(navController, currentScreen, content)
            } else {
                UnauthenticatedTopBar(navController, currentScreen, content)
            }
        },
        backgroundColor = MaterialTheme.colorScheme.primary,
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .fillMaxHeight()
        ) {
            content()
        }
    }
}


@Composable
fun FrontImage(){
    Image(
        painter = painterResource(id = R.mipmap.front_image_foreground),
        contentDescription = "Image",
        modifier = Modifier
            .size(250.dp)
    )
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(value = valueState.value,
        onValueChange = { valueState.value = it},
        label = { Text(text = labelId, color = Color.White)},
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            cursorColor = Color.White,
            textColor = Color.White,
            disabledTextColor = Color.White,
            placeholderColor = Color.White,
            disabledPlaceholderColor = Color.White,
            leadingIconColor = Color.White,
        ),

        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()
        ,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction)
}


@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default,
) {

    val visualTransformation = if (passwordVisibility.value) VisualTransformation.None else
        PasswordVisualTransformation()
    OutlinedTextField(value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        label = { Text(text = labelId, color = Color.White)},
        singleLine = true,
        textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White,
            cursorColor = Color.White,
            textColor = Color.White,
            disabledTextColor = Color.White,
            placeholderColor = Color.White,
            disabledPlaceholderColor = Color.White,
        ),
        modifier = modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction),
        visualTransformation = visualTransformation,
        trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)},
        keyboardActions = onAction)
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>) {
    val visible = passwordVisibility.value
    IconButton(onClick = { passwordVisibility.value = !visible}) {
        if(passwordVisibility.value){
            Icon(Icons.Default.VisibilityOff, contentDescription = "Hide password", tint = Color.White)
        }else{
            Icon(Icons.Default.Visibility, contentDescription = "Show password", tint = Color.White)
        }
    }

}

@Composable
fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(color = Color.White)
    }
}


@Composable
fun WeatherScaffold(
    weather: Weather,
    navController: NavController,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight()
    ) {
        CurrentWeather(weather)
        WeatherForecast(weather)
    }

}

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CurrentWeather(weather: Weather){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight(0.5f)
    ) {


        Text(text = "${weather.location.name},", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text(text = weather.location.country, style = MaterialTheme.typography.headlineSmall, color = Color.White)

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .padding(top = 25.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter("https://${weather.forecast.forecastday[0].day.condition.icon}"),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(125.dp)
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${weather.current.temp_c}ºC", fontSize = 25.sp, textAlign = TextAlign.Center, color = Color.White)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${weather.current.condition.text}, ${weather.forecast.forecastday[0].day.mintemp_c.toInt()}º/${weather.forecast.forecastday[0].day.maxtemp_c.toInt()}º",textAlign = TextAlign.Center, color = Color.White)
            }
            Spacer(modifier = Modifier.height(5.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "${weather.current.humidity}%\uD83D\uDCA7", textAlign = TextAlign.Center, color = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "${weather.current.wind_kph} kpm ༄", textAlign = TextAlign.Center, color = Color.White)
            }
        }
    }
}
@Composable
fun WeatherForecast(weather: Weather){
    Column(verticalArrangement = Arrangement.SpaceEvenly,modifier = Modifier
        .padding(10.dp, 10.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(Color(0x43EBE4E4))
        .fillMaxHeight()
    ) {
        weather.forecast.forecastday.forEach {
                forecastday ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(15.dp,0.dp,25.dp,0.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth(0.5f)) {
                    Image(
                        painter = rememberAsyncImagePainter("https://${forecastday.day.condition.icon}"),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(text = epochToWeekDay(forecastday.date_epoch.toLong()))
                }
                Row(horizontalArrangement = Arrangement.End,modifier = Modifier.fillMaxWidth()){
                    Text(text = "${forecastday.day.mintemp_c.toInt()}º/ ${forecastday.day.maxtemp_c.toInt()}º", color = Color.DarkGray, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
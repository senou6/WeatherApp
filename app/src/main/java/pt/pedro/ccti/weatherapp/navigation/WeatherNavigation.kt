package pt.pedro.ccti.weatherapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import pt.pedro.ccti.weatherapp.components.UserScaffold
import pt.pedro.ccti.weatherapp.screens.FavoriteLocations.FavoriteLocationsScreen
import pt.pedro.ccti.weatherapp.screens.homescreen.HomeScreen
import pt.pedro.ccti.weatherapp.screens.homescreen.HomeViewModel
import pt.pedro.ccti.weatherapp.screens.loginscreen.LoginScreen
import pt.pedro.ccti.weatherapp.screens.loginscreen.LoginViewModel
import pt.pedro.ccti.weatherapp.screens.mainscreen.MainScreen
import pt.pedro.ccti.weatherapp.screens.registerscreen.RegisterScreen
import pt.pedro.ccti.weatherapp.screens.registerscreen.RegisterViewModel
import pt.pedro.ccti.weatherapp.screens.searchscreen.SearchScreen


@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    val isAuthenticated = !FirebaseAuth.getInstance().currentUser?.email.isNullOrBlank()
    val registerViewModel: RegisterViewModel = viewModel()
    val loginViewModel: LoginViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()

    NavHost(navController = navController, startDestination = if (isAuthenticated) "authenticated_graph" else "unauthenticated_graph") {
        navigation(startDestination = WeatherScreens.HomeScreen.name, route = "authenticated_graph") {

            composable(WeatherScreens.HomeScreen.name) {
                UserScaffold(true,navController, WeatherScreens.HomeScreen.name) {
                    HomeScreen(navController = navController, homeViewModel)
                }
            }
            composable(WeatherScreens.FavoriteLocationsScreen.name) {
                UserScaffold(true,navController, WeatherScreens.FavoriteLocationsScreen.name) {
                    FavoriteLocationsScreen()
                }
            }
            composable(WeatherScreens.SettingsScreen.name) {
                UserScaffold(true,navController, WeatherScreens.SettingsScreen.name) {
                    //SettingsScreen(navController = navController)
                }
            }
            composable(WeatherScreens.SearchScreen.name) {
                UserScaffold(true,navController, WeatherScreens.SearchScreen.name) {
                    SearchScreen(navController = navController)
                }
            }
            composable(WeatherScreens.SearchScreen.name) {
                    SearchScreen(navController = navController)
            }
        }

        navigation(startDestination = WeatherScreens.MainScreen.name, route = "unauthenticated_graph") {
            composable(WeatherScreens.LoginScreen.name) {
                UserScaffold(false, navController, WeatherScreens.LoginScreen.name) {
                    LoginScreen(navController = navController, loginViewModel)
                }
            }
            composable(WeatherScreens.RegisterScreen.name) {
                UserScaffold(false, navController, WeatherScreens.RegisterScreen.name) {
                    RegisterScreen(navController = navController, registerViewModel)
                }
            }
            composable(WeatherScreens.MainScreen.name) {
                UserScaffold(false, navController, WeatherScreens.MainScreen.name) {
                    MainScreen(navController = navController)
                }
            }
        }
    }
}


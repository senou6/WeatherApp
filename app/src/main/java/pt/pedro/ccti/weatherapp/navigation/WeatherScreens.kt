package pt.pedro.ccti.weatherapp.navigation

enum class WeatherScreens {
    MainScreen,
    HomeScreen,
    LoginScreen,
    RegisterScreen,
    SettingsScreen,
    FavoriteLocationsScreen,
    SearchScreen;
    companion object{
        fun fromRoute(route : String?) : WeatherScreens
            = when(route?.substringBefore("/")){
                MainScreen.name -> MainScreen
                FavoriteLocationsScreen.name -> FavoriteLocationsScreen
                LoginScreen.name -> LoginScreen
                RegisterScreen.name -> RegisterScreen
                SearchScreen.name -> RegisterScreen
                HomeScreen.name -> HomeScreen
                SettingsScreen.name -> SettingsScreen
                null -> MainScreen
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }


    }
}
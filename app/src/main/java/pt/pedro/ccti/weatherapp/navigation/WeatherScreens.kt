package pt.pedro.ccti.weatherapp.navigation

enum class WeatherScreens {
    MainScreen,
    HomeScreen,
    LoginScreen,
    RegisterScreen,
    FavoriteLocationsScreen,
    SearchScreen,
    SearchedLocationScreen;

    fun withArgs(vararg args: String): String {
        return buildString {
            append(name)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    companion object {
        fun fromRoute(route: String?): WeatherScreens {
            val routeWithoutArgs = route?.substringBefore("?")?.substringBefore("/")
            return when (routeWithoutArgs) {
                MainScreen.name -> MainScreen
                FavoriteLocationsScreen.name -> FavoriteLocationsScreen
                SearchedLocationScreen.name -> SearchedLocationScreen
                LoginScreen.name -> LoginScreen
                RegisterScreen.name -> RegisterScreen
                SearchScreen.name -> SearchScreen
                HomeScreen.name -> HomeScreen
                null -> MainScreen
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
        }
    }
}
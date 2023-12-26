package pt.pedro.ccti.weatherapp.model.Weather

data class Weather(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)
package pt.pedro.ccti.weatherapp.utils


import androidx.compose.material3.ColorScheme
import pt.pedro.ccti.weatherapp.model.Weather.Weather
import pt.pedro.ccti.weatherapp.ui.theme.SunnyThemeColors

// Function to choose theme based on weather
fun getThemeForWeather(weather: Weather): ColorScheme {
    return when (weather.current.condition.code) {
        1000 -> SunnyThemeColors
        1003 -> SunnyThemeColors //Ligh Dark
        1006 -> SunnyThemeColors //Ligh Dark
        1009 -> SunnyThemeColors //Ligh Dark
        1030 -> SunnyThemeColors //Ligh Blue
        1063 -> SunnyThemeColors //Ligh Dark
        1066 -> SunnyThemeColors
        1069 -> SunnyThemeColors
        1072 -> SunnyThemeColors
        1087 -> SunnyThemeColors
        1114 -> SunnyThemeColors
        1117 -> SunnyThemeColors
        1135 -> SunnyThemeColors
        1147 -> SunnyThemeColors
        1150 -> SunnyThemeColors
        1153 -> SunnyThemeColors
        1168 -> SunnyThemeColors
        1171 ->SunnyThemeColors
        1180 ->SunnyThemeColors
        1183 -> SunnyThemeColors
        1186 -> SunnyThemeColors
        1189 -> SunnyThemeColors
        1192 -> SunnyThemeColors
        1195 -> SunnyThemeColors
        1198 -> SunnyThemeColors
        1201 -> SunnyThemeColors
        1204 -> SunnyThemeColors
        1207 -> SunnyThemeColors
        1210 -> SunnyThemeColors
        1213 -> SunnyThemeColors
        1216 -> SunnyThemeColors
        1219 -> SunnyThemeColors
        1222 -> SunnyThemeColors
        1225 -> SunnyThemeColors
        1237 -> SunnyThemeColors
        1240 -> SunnyThemeColors
        1243 -> SunnyThemeColors
        1246 -> SunnyThemeColors
        1249 -> SunnyThemeColors
        1252 -> SunnyThemeColors
        1255 -> SunnyThemeColors
        1258 -> SunnyThemeColors
        1261 -> SunnyThemeColors
        1264 -> SunnyThemeColors
        1273 -> SunnyThemeColors
        1276 -> SunnyThemeColors
        1279 -> SunnyThemeColors
        1282 -> SunnyThemeColors
          else -> SunnyThemeColors
    }
}

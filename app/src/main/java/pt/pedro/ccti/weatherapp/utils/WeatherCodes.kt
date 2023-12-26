package pt.pedro.ccti.weatherapp.utils
//
//import pt.pedro.ccti.weatherapp.R
//
//object WeatherImageUtil {
//    fun getImageForWeatherCondition(code: Int): Int {
//        return when (code) {
//            1000 -> R.mipmap.sunny_image
//            1003 -> R.drawable.partly_cloudy_image
//            1006 -> R.drawable.cloudy_image
//            1009 -> R.drawable.overcast_image
//            1030 -> R.drawable.mist_image
//            1063 -> R.drawable.patchy_rain_possible_image
//            1066 -> R.drawable.patchy_snow_possible_image
//            1069 -> R.drawable.patchy_sleet_possible_image
//            1072 -> R.drawable.patchy_freezing_drizzle_possible_image
//            1087 -> R.drawable.thundery_outbreaks_possible_image
//            1114 -> R.drawable.blowing_snow_image
//            1117 -> R.drawable.blizzard_image
//            1135 -> R.drawable.fog_image
//            1147 -> R.drawable.freezing_fog_image
//            1150 -> R.drawable.patchy_light_drizzle_image
//            1153 -> R.drawable.light_drizzle_image
//            1168 -> R.drawable.freezing_drizzle_image
//            1171 -> R.drawable.heavy_freezing_drizzle_image
//            1180 -> R.drawable.patchy_light_rain_image
//            1183 -> R.drawable.light_rain_image
//            1186 -> R.drawable.moderate_rain_at_times_image
//            1189 -> R.drawable.moderate_rain_image
//            1192 -> R.drawable.heavy_rain_at_times_image
//            1195 -> R.drawable.heavy_rain_image
//            1198 -> R.drawable.light_freezing_rain_image
//            1201 -> R.drawable.moderate_or_heavy_freezing_rain_image
//            1204 -> R.drawable.light_sleet_image
//            1207 -> R.drawable.moderate_or_heavy_sleet_image
//            1210 -> R.drawable.patchy_light_snow_image
//            1213 -> R.drawable.light_snow_image
//            1216 -> R.drawable.patchy_moderate_snow_image
//            1219 -> R.drawable.moderate_snow_image
//            1222 -> R.drawable.patchy_heavy_snow_image
//            1225 -> R.drawable.heavy_snow_image
//            1237 -> R.drawable.ice_pellets_image
//            1240 -> R.drawable.light_rain_shower_image
//            1243 -> R.drawable.moderate_or_heavy_rain_shower_image
//            1246 -> R.drawable.torrential_rain_shower_image
//            1249 -> R.drawable.light_sleet_showers_image
//            1252 -> R.drawable.moderate_or_heavy_sleet_showers_image
//            1255 -> R.drawable.light_snow_showers_image
//            1258 -> R.drawable.moderate_or_heavy_snow_showers_image
//            1261 -> R.drawable.light_showers_of_ice_pellets_image
//            1264 -> R.drawable.moderate_or_heavy_showers_of_ice_pellets_image
//            1273 -> R.drawable.patchy_light_rain_with_thunder_image
//            1276 -> R.drawable.moderate_or_heavy_rain_with_thunder_image
//            1279 -> R.drawable.patchy_light_snow_with_thunder_image
//            1282 -> R.drawable.moderate_or_heavy_snow_with_thunder_image
//              else -> R.mipmap.sunny_image
//        }
//    }
//}

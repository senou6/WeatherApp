package pt.pedro.ccti.weatherapp.domain.use_cases

import android.content.Context
import android.location.Geocoder
import android.util.Log
import pt.pedro.ccti.weatherapp.domain.location.LocationTracker
import java.util.Locale

@Suppress("DEPRECATION")
class LocationUtils {
    companion object {
        suspend fun getAddressFromCoordinates(context: Context, locationTracker: LocationTracker): String {
            val geocoder = Geocoder(context, Locale.ENGLISH)
            val location = locationTracker.getCurrentLocation()

            return location?.let {
                try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    Log.d("TAG", "Adresses: ${addresses}")
                    if (addresses!!.isNotEmpty()) {
                        Log.d("TAG", "Adress: ${addresses[0].subAdminArea}")
                        "${addresses[0].subAdminArea}-${addresses[0].countryName}"

                    } else {
                        "Maia"
                    }
                } catch (e: Exception) {
                    "Maia"
                }
            } ?: "Maia"
        }
    }
}
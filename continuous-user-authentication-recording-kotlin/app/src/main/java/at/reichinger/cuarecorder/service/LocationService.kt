package at.reichinger.cuarecorder.service

import android.content.Context
import android.location.Location
import com.google.android.gms.location.*

/**
 * LocationService that registers for location updates and can then be used to get the current location
 * This class is using a displacement of 75 meters and an update time of 30 seconds to save power and
 * not constantly get fine grained location updates
 */
class LocationService(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
        context
    )
) {

    private var locationCallback: LocationCallback? = null
    private var currentLocations: List<Location>? = null
    private var locationUpdatesEnabled: Boolean = false

    companion object {
        private const val HALF_A_MINUTE: Long = 30 * 1000  // 30 * 1000ms
        private const val DISPLACEMENT: Float = 75f    // Minimum displacement in meters
    }

    /**
     * Starts location updates according to the internal location request settings
     */
    fun startLocationUpdates() {
        queryLastLocation()
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = HALF_A_MINUTE
        locationRequest.fastestInterval = HALF_A_MINUTE
        locationRequest.smallestDisplacement = DISPLACEMENT
        locationCallback = createCallback()
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        locationUpdatesEnabled = true
    }

    /**
     * Stops location updates
     */
    fun stopLocationUpdates() {
        if (locationCallback != null) {
            locationClient.removeLocationUpdates(locationCallback)
        }

        locationUpdatesEnabled = false
    }

    /**
     * Returns whether the service is receiving location updates or not
     */
    fun locationUpdatesEnabled(): Boolean = locationUpdatesEnabled


    /**
     * Returns a list of currently acquired locations (null if none have been acquired)
     */
    fun getCurrentLocations(): List<Location>? = currentLocations

    private fun createCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    currentLocations = null
                    return
                }
                currentLocations = locationResult.locations
            }
        }
    }

    private fun queryLastLocation() {
        locationClient.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            location?.let {
                currentLocations = listOf(it)
            }
        }
    }
}
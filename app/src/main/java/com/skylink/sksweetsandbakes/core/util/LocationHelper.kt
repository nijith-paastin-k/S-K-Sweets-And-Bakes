package com.skylink.sksweetsandbakes.core.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

data class UserLocationInfo(
    val area: String,
    val city: String,
    val state: String = "Tamil Nadu",
    val pincode: String = "641002",
    val fullDetailedAddress: String = "#14, 2nd Cross, RS Puram, Coimbatore - 641002, Tamil Nadu",
    val fullDisplay: String = if (area.isNotBlank() && area != city) "$area, $city" else city,
    val source: String = "Auto"
)

object LocationHelper {
    private const val PREFS_NAME = "sk_location_prefs"
    private const val KEY_AREA = "key_area"
    private const val KEY_CITY = "key_city"
    private const val KEY_STATE = "key_state"
    private const val KEY_PINCODE = "key_pincode"
    private const val KEY_FULL_ADDRESS = "key_full_address"
    private const val KEY_SOURCE = "key_source"

    fun getCachedLocation(context: Context): UserLocationInfo {
        val sp: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val area = sp.getString(KEY_AREA, null)
        val city = sp.getString(KEY_CITY, null)
        val state = sp.getString(KEY_STATE, null)
        val pincode = sp.getString(KEY_PINCODE, null)
        val fullAddr = sp.getString(KEY_FULL_ADDRESS, null)
        val source = sp.getString(KEY_SOURCE, null)

        return if (!fullAddr.isNullOrBlank()) {
            UserLocationInfo(
                area = area ?: "",
                city = city ?: "Coimbatore",
                state = state ?: "Tamil Nadu",
                pincode = pincode ?: "641002",
                fullDetailedAddress = fullAddr,
                source = source ?: "Saved"
            )
        } else {
            UserLocationInfo(
                area = "RS Puram",
                city = "Coimbatore",
                state = "Tamil Nadu",
                pincode = "641002",
                fullDetailedAddress = "#14, 2nd Cross, Sweet Garden Layout, RS Puram, Coimbatore - 641002, Tamil Nadu",
                source = "Default"
            )
        }
    }

    fun saveSelectedLocation(context: Context, info: UserLocationInfo) {
        val sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sp.edit()
            .putString(KEY_AREA, info.area)
            .putString(KEY_CITY, info.city)
            .putString(KEY_STATE, info.state)
            .putString(KEY_PINCODE, info.pincode)
            .putString(KEY_FULL_ADDRESS, info.fullDetailedAddress)
            .putString(KEY_SOURCE, info.source)
            .apply()
    }

    @SuppressLint("MissingPermission")
    suspend fun getLiveLocation(context: Context, forceGps: Boolean = false): UserLocationInfo {
        return withContext(Dispatchers.IO) {
            val hasFine = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val hasCoarse = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasFine || hasCoarse) {
                try {
                    val locationManager =
                        context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                    if (locationManager != null) {
                        var bestLocation: Location? = null
                        val providers = listOf(
                            LocationManager.GPS_PROVIDER,
                            LocationManager.NETWORK_PROVIDER,
                            LocationManager.PASSIVE_PROVIDER
                        )
                        for (provider in providers) {
                            try {
                                if (locationManager.isProviderEnabled(provider)) {
                                    val loc = locationManager.getLastKnownLocation(provider)
                                    if (loc != null) {
                                        if (bestLocation == null || loc.accuracy < bestLocation.accuracy) {
                                            bestLocation = loc
                                        }
                                    }
                                }
                            } catch (_: Exception) {
                            }
                        }

                        if (bestLocation != null) {
                            val geocoded = reverseGeocode(
                                context,
                                bestLocation.latitude,
                                bestLocation.longitude
                            )
                            if (geocoded != null) {
                                saveSelectedLocation(context, geocoded)
                                return@withContext geocoded
                            }
                        }
                    }
                } catch (_: Exception) {
                }
            }

            try {
                val networkLocation = fetchIpLocation()
                if (networkLocation != null) {
                    saveSelectedLocation(context, networkLocation)
                    return@withContext networkLocation
                }
            } catch (_: Exception) {
            }
            getCachedLocation(context)
        }
    }

    private fun reverseGeocode(
        context: Context,
        latitude: Double,
        longitude: Double
    ): UserLocationInfo? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())

            @Suppress("DEPRECATION")
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val fullAddressLine = addr.getAddressLine(0) ?: ""
                val subLoc = addr.subLocality ?: addr.featureName ?: ""
                val locality = addr.locality ?: addr.subAdminArea ?: ""
                val state = addr.adminArea ?: "Tamil Nadu"
                val postal = addr.postalCode ?: ""

                val cleanCity = if (locality.isNotBlank()) locality else state
                val cleanArea = if (subLoc.isNotBlank() && !subLoc.equals(
                        cleanCity,
                        ignoreCase = true
                    )
                ) subLoc else ""

                val fullAddr = if (fullAddressLine.isNotBlank()) {
                    fullAddressLine
                } else {
                    listOfNotNull(
                        if (cleanArea.isNotBlank()) cleanArea else null,
                        cleanCity,
                        if (postal.isNotBlank()) "- $postal" else null,
                        state
                    ).joinToString(", ")
                }

                UserLocationInfo(
                    area = cleanArea,
                    city = cleanCity,
                    state = state,
                    pincode = postal,
                    fullDetailedAddress = fullAddr,
                    source = "GPS"
                )
            } else null
        } catch (_: Exception) {
            null
        }
    }

    private fun fetchIpLocation(): UserLocationInfo? {
        return try {
            val url = URL("https://ipwho.is/")
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 3000
            conn.readTimeout = 3000
            conn.requestMethod = "GET"
            conn.setRequestProperty("User-Agent", "SKSweetsApp/1.0")

            if (conn.responseCode == 200) {
                val responseText = conn.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(responseText)
                if (json.optBoolean("success", false)) {
                    val city = json.optString("city", "").trim()
                    val region = json.optString("region", "").trim()
                    val postal = json.optString("postal", "").trim()

                    if (city.isNotBlank()) {
                        val detailed = if (postal.isNotBlank()) {
                            "Opposite Town Hall, $city - $postal, $region"
                        } else {
                            "$city Central, $region"
                        }
                        return UserLocationInfo(
                            area = "Central",
                            city = city,
                            state = region,
                            pincode = postal,
                            fullDetailedAddress = detailed,
                            source = "Network"
                        )
                    }
                }
            }
            null
        } catch (_: Exception) {
            null
        }
    }
}

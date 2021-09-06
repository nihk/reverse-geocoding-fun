package nick.template.geo

import android.location.Address
import android.location.Geocoder
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GeocoderRepository {
    suspend fun locate(lat: Double, lng: Double): Location
}

class AndroidGeocoderRepository @Inject constructor(
    private val geocoder: Geocoder
) : GeocoderRepository {
    override suspend fun locate(lat: Double, lng: Double): Location {
        val addresses = withContext(Dispatchers.IO) {
            geocoder.getFromLocation(lat, lng, 1)
        }

        return if (addresses.isEmpty()) {
            Location.Unknown
        } else {
            val address: Address = addresses.single()
            Location.Found(
                address = address.getAddressLine(0).orEmpty(),
                city = address.locality.orEmpty(),
                area = address.subLocality.orEmpty()
            )
        }
    }
}

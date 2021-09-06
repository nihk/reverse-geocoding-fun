package nick.template.geo

import android.location.Address
import android.location.Geocoder
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

interface GeocoderRepository {
    fun locate(lat: Double, lng: Double): Single<Location>
}

class AndroidGeocoderRepository @Inject constructor(
    private val geocoder: Geocoder
) : GeocoderRepository {
    override fun locate(lat: Double, lng: Double): Single<Location> {
        return Single
            .fromCallable {
                geocoder.getFromLocation(lat, lng, 1)
            }
            .map { addresses ->
                if (addresses.isEmpty()) {
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
            .subscribeOn(Schedulers.io())
    }
}

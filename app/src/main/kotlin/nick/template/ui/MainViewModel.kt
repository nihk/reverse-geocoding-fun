package nick.template.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject
import nick.template.geo.GeocoderRepository
import nick.template.geo.Location

class MainViewModel(private val repository: GeocoderRepository) : ViewModel() {
    private val locateEvents = PublishSubject.create<LocateEvent>()
    val locations: Observable<Location> = locateEvents.flatMapSingle { event: LocateEvent ->
        repository.locate(event.lat, event.lng)
    }

    fun locate(lat: Double, lng: Double) {
        val event = LocateEvent(lat = lat, lng = lng)
        locateEvents.onNext(event)
    }

    class Factory @Inject constructor(private val repository: GeocoderRepository) {
        fun create(owner: SavedStateRegistryOwner): AbstractSavedStateViewModelFactory {
            return object : AbstractSavedStateViewModelFactory(owner, null) {
                override fun <T : ViewModel?> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    @Suppress("UNCHECKED_CAST")
                    return MainViewModel(repository) as T
                }
            }
        }
    }

    private data class LocateEvent(
        val lat: Double,
        val lng: Double
    )
}

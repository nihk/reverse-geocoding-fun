package nick.template.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nick.template.geo.GeocoderRepository
import nick.template.geo.Location

class MainViewModel(private val repository: GeocoderRepository) : ViewModel() {
    private val locateEvents = MutableSharedFlow<LocateEvent>()
    val locations = locateEvents.mapLatest { event ->
        repository.locate(event.lat, event.lng)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = Location.Unknown
    )

    fun locate(lat: Double, lng: Double) {
        viewModelScope.launch {
            val event = LocateEvent(lat = lat, lng = lng)
            locateEvents.emit(event)
        }
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

package nick.template.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.maps.android.ktx.awaitMap
import nick.template.R
import nick.template.databinding.MainFragmentBinding
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import nick.template.geo.Location

class MainFragment @Inject constructor(
    private val vmFactory: MainViewModel.Factory
) : Fragment(R.layout.main_fragment) {
    private val viewModel: MainViewModel by viewModels { vmFactory.create(this) }
    private var mapView: MapView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = MainFragmentBinding.bind(view)
        val mapView = MapView(view.context).also { mapView = it }
        binding.container.addView(mapView)
        mapView.bindToLifecycle(viewLifecycleOwner.lifecycle, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            val map = mapView.awaitMap()
            onMapLoaded(map)
        }

        viewModel.locations
            .onEach { location ->
                binding.location.text = when (location) {
                    is Location.Found -> """
                        ${location.address},
                        ${location.area},
                        ${location.city}
                    """.trimIndent()
                    Location.Unknown -> "Unknown"
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onMapLoaded(map: GoogleMap) {
        queryLocation(map) // Kick us off
        map.setOnCameraIdleListener {
           queryLocation(map)
        }
    }

    private fun queryLocation(map: GoogleMap) {
        val position = map.cameraPosition.target
        viewModel.locate(position.latitude, position.longitude)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}

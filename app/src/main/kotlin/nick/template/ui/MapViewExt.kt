package nick.template.ui

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.MapView

fun MapView.bindToLifecycle(lifecycle: Lifecycle, savedInstanceState: Bundle? = null) {
    val observer = LifecycleEventObserver { source, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate(savedInstanceState)
            Lifecycle.Event.ON_START -> onStart()
            Lifecycle.Event.ON_RESUME -> onResume()
            Lifecycle.Event.ON_PAUSE -> onPause()
            Lifecycle.Event.ON_STOP -> onStop()
            Lifecycle.Event.ON_DESTROY -> onDestroy()
            Lifecycle.Event.ON_ANY -> {}
        }
    }

    lifecycle.addObserver(observer)
}

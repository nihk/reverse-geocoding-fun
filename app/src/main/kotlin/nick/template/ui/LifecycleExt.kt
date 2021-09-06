package nick.template.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.disposables.CompositeDisposable

fun Lifecycle.disposeOnDestroy(): CompositeDisposable {
    val disposable = CompositeDisposable()

    doOnEvent(Lifecycle.Event.ON_DESTROY) {
        disposable.clear()
    }

    return disposable
}

fun Lifecycle.doOnEvent(which: Lifecycle.Event, block: () -> Unit) {
    val observer = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (which != event) return
            source.lifecycle.removeObserver(this)
            block()
        }
    }

    addObserver(observer)
}

package com.example.chatapp.other

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

// class that can create only one observer for live data
class SingleLiveEvent<T>: MutableLiveData<T>()  {

    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (hasActiveObservers()) {
            Log.d("SingleLiveEvent", "Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner, object : Observer<T> {
                override fun onChanged(t: T) {
                    if (pending.compareAndSet(true, false)) {
                        observer.onChanged(t)
                    }
                }
            })
    }

    @MainThread
    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }
}
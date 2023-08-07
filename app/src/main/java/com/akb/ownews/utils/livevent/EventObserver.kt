package com.akb.ownews.utils.livevent

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class EventObserver<T> (private val action: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(value: Event<T>) {
        value.data?.let { item ->
            action.invoke(item)
        }
    }
}

fun <T> LiveData<Event<T>>.observe(
    lifecycleOwner: LifecycleOwner,
    observer: (T) -> Unit
) = observe(lifecycleOwner, EventObserver(observer))
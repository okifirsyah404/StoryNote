package com.okifirsyah.domain.utils.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.okifirsyah.data.network.ApiResponse
import com.okifirsyah.domain.utils.config.ObserverConfiguration


fun <T> LiveData<ApiResponse<T>>.observeResult(
    lifecycleOwner: LifecycleOwner,
    observerConfig: ObserverConfiguration<T>.() -> Unit
) {
    val config = ObserverConfiguration<T>().apply(observerConfig)
    this.observe(lifecycleOwner) { result ->
        when (result) {
            is ApiResponse.Loading -> config.onLoading?.invoke()
            is ApiResponse.Success -> config.onSuccess?.invoke(result.data)
            is ApiResponse.Error -> config.onError?.invoke(result.errorMessage)
            is ApiResponse.Empty -> config.onEmpty?.invoke()
        }
    }
}

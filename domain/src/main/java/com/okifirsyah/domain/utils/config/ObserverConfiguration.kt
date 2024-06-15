package com.okifirsyah.domain.utils.config

class ObserverConfiguration<T> {
    var onLoading: (() -> Unit)? = null
    var onSuccess: ((T) -> Unit)? = null
    var onError: ((String) -> Unit)? = null
    var onEmpty: (() -> Unit)? = null

    fun onLoading(block: () -> Unit) {
        onLoading = block
    }

    fun onSuccess(block: T.() -> Unit) {
        onSuccess = block
    }

    fun onError(block: String.() -> Unit) {
        onError = block
    }

    fun onEmpty(block: () -> Unit) {
        onEmpty = block
    }
}


package com.isaacrf.android_base_app.shared

/**
 * Status of a resource that is provided to the UI.
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<NetworkResource<T>>` to pass back the latest data to the UI with its fetch status.
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class NetworkResource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): NetworkResource<T> {
            return NetworkResource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): NetworkResource<T> {
            return NetworkResource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): NetworkResource<T> {
            return NetworkResource(Status.LOADING, data, null)
        }
    }
}
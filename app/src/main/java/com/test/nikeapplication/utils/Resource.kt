package com.test.nikeapplication.utils

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?,
    val messageID: Int?
) {

    enum class Status {
        SUCCESS,
        ERROR,
    }

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(message: String?, messageID: Int? = null, data: T? = null): Resource<T> {

            return Resource(Status.ERROR, data, message, messageID)


        }
    }


}
package net.m3mobile.core

fun interface RequestCallback<T: Any> {

    /**
     * Called when the asynchronous operation is complete.
     *
     * This function will be invoked on the main thread.
     * Exactly one of the parameters will be non-null.
     *
     * @param result The successful result of the operation, or null if an error occurred.
     * @param error An exception representing the error, or null if the operation was successful.
     */
    fun onComplete(result: T?, error: Exception?)
}
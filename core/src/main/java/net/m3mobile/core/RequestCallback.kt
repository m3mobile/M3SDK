package net.m3mobile.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

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

@JvmSynthetic
@InternalM3Api
fun launchOnMain(task: suspend () -> Unit): Job {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    scope.launch {
        task()
    }
    return scope.coroutineContext.job
}
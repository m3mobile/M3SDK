package net.m3mobile.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.launch

fun interface RequestCallback<T: Any> {
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
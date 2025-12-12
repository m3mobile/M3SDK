package net.m3mobile.core.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.m3mobile.core.InternalM3Api

@JvmSynthetic
@InternalM3Api
fun launchOnMain(task: suspend () -> Unit): Job {
    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    scope.launch {
        task()
    }
    return scope.coroutineContext[Job]!!
}
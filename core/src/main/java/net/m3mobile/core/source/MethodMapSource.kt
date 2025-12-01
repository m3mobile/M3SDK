package net.m3mobile.core.source

interface MethodMapSource {
    fun<V: Any> get(): Map<String, V>
}
package net.m3mobile.core.source

public interface MethodMapSource {
    public fun<V: Any> get(): Map<String, V>
}
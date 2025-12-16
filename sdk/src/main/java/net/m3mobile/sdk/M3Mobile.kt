@file:Suppress("DEPRECATION_ERROR")
package net.m3mobile.sdk

/**
 * Provides access to the M3 SDK.
 * This serves as the main entry point for interacting with the M3 APIs.
 *
 * Use [M3Mobile.instance] to get a concrete implementation of the [M3Sdk] interface.
 */
public object M3Mobile {

    public lateinit var instance: M3Sdk
}
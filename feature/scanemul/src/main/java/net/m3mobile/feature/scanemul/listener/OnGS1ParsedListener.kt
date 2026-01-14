package net.m3mobile.feature.scanemul.listener

import net.m3mobile.feature.scanemul.params.GS1ParsedData

/**
 * Interface definition for a callback to be invoked when a GS1 barcode scan result is received.
 */
public fun interface OnGS1ParsedListener {
    /**
     * Called when a GS1 barcode has been successfully parsed.
     *
     * @param result The parsed GS1 data containing the AI (Application Identifiers) and their values
     */
    public fun onGS1Parsed(result: List<GS1ParsedData>)
}
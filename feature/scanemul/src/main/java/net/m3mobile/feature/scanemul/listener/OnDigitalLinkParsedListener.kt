package net.m3mobile.feature.scanemul.listener

import net.m3mobile.feature.scanemul.params.DigitalLinkParsedData

/**
 * Interface definition for a callback to be invoked when a Digital Link barcode scan result is received.
 */
public fun interface OnDigitalLinkParsedListener {
    /**
     * Called when a GS1 barcode has been successfully parsed.
     *
     * @param result The parsed Digital Link data containing the AI (Application Identifiers) and their values
     */
    public fun onDigitalLinkParsed(result: List<DigitalLinkParsedData>)
}
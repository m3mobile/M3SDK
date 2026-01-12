package net.m3mobile.feature.scanemul.requester.scanner

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Message
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import net.m3mobile.core.requester.MessageRequester
import net.m3mobile.feature.scanemul.params.DigitalLinkParsedData
import net.m3mobile.feature.scanemul.params.GS1ParsedData
import net.m3mobile.feature.scanemul.params.ScanResult

internal class OnDecodeCompleteRequester(override val context: Context): MessageRequester() {

    private val _scanResult = MutableSharedFlow<ScanResult>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.SUSPEND
    )
    val scanResult = _scanResult.asSharedFlow()

    private val _gs1ParsedData = MutableSharedFlow<List<GS1ParsedData>>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.SUSPEND
    )
    val gs1ParsedResult = _gs1ParsedData.asSharedFlow()

    private val _digitalLinkParsedData = MutableSharedFlow<List<DigitalLinkParsedData>>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.SUSPEND
    )
    val digitalLinkParsed = _digitalLinkParsedData.asSharedFlow()

    override val intent = Intent("net.m3mobile.m3sdk.connection").setPackage("net.m3mobile.app.scanemul")

    override fun onMessage(msg: Message) {
        when (msg.what) {
            DECODE_COMPLETE -> {
                val bundle = msg.obj as Bundle

                val barcode = bundle.getString("m3scannerdata")
                val codeType = bundle.getString("m3scanner_code_type")
                val rawData = bundle.getByteArray("m3scannerdata_raw")
                _scanResult.tryEmit(ScanResult(barcode!!, codeType!!, rawData!!))
            }
            GS1_PARSING_COMPLETE -> {
                val bundle = msg.obj as Bundle

                val bundleList = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelableArrayList("list")
                } else {
                    bundle.getParcelableArrayList("list", Bundle::class.java)
                }

                val parsedList = mutableListOf<GS1ParsedData>()
                bundleList?.forEach {
                    val bundle = (it as Bundle)
                    val ai = bundle.getString("ai")
                    val data = bundle.getString("data")
                    val description = bundle.getString("description")
                    parsedList.add(GS1ParsedData(ai = ai!!, data = data!!, description = description!!))
                }
                _gs1ParsedData.tryEmit(parsedList)
            }
            DIGITAL_LINK_PARSING_COMPLETE -> {
                val bundle = msg.obj as Bundle

                val bundleList = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    bundle.getParcelableArrayList("list")
                } else {
                    bundle.getParcelableArrayList("list", Bundle::class.java)
                }

                val parsedList = mutableListOf<DigitalLinkParsedData>()
                bundleList?.forEach {
                    val bundle = (it as Bundle)
                    val ai = bundle.getString("ai")
                    val data = bundle.getString("data")
                    val description = bundle.getString("description")
                    parsedList.add(DigitalLinkParsedData(ai = ai!!, data = data!!, description = description!!))
                }
                _digitalLinkParsedData.tryEmit(parsedList)
            }
        }
    }

    private companion object {
        private const val DECODE_COMPLETE = 1
        private const val GS1_PARSING_COMPLETE = 2
        private const val DIGITAL_LINK_PARSING_COMPLETE = 3
    }
}
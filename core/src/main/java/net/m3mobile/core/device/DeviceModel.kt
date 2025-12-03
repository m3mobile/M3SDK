@file:JvmSynthetic
package net.m3mobile.core.device

import android.os.Build
import net.m3mobile.core.InternalM3Api

internal val currentDeviceModel by lazy {
    initCurrentDeviceModel()
}

@InternalM3Api
enum class DeviceModel {
    UNKNOWN,
    SM10,
    SM10LTE,
    SM15,
    SM20,
    SM20_U,
    SM30,
    TN15,
    TN15_OREO,
    TX15,
    UL20_OREO,
    UL20_PIE,
    UX20_Q,
    UL20_A10,
    UL20F,
    UL30,
    TL20,
    US20,
    US30,
    SL10,
    SL10K,
    SL20,
    SL20P,
    SL20K,
    SL25,
    PC10;

    companion object {
        val setOfEntriesName = entries.map { it.name }.toSet()
    }
}

private fun initCurrentDeviceModel(): DeviceModel {
    return if (Build.MODEL.contains("M3SM10_LTE")) {
        DeviceModel.SM10LTE
    } else if (Build.MODEL.contains("M3SM10")) {
        DeviceModel.SM10
    } else if (Build.MODEL.contains("M3SM15")) {
        DeviceModel.SM15
    } else if (Build.MODEL.contains("M3UL20") || Build.MODEL.contains("M3US20")) {
        when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.P -> DeviceModel.UL20_PIE
            Build.VERSION_CODES.Q -> DeviceModel.UX20_Q
            else -> DeviceModel.UL20_OREO
        }
    } else if (Build.MODEL.contains("UL30")) {
        DeviceModel.UL30
    } else if (Build.MODEL.contains("TN15")) {
        DeviceModel.TN15
    } else if (Build.MODEL.contains("TX15")) {
        DeviceModel.TX15
    } else if (Build.MODEL.contains("TL20")) {
        DeviceModel.TL20
    } else if (Build.MODEL.contains("SL20")) {
        if (Build.MODEL.contains("K")) DeviceModel.SL20K
        else if (Build.MODEL.contains("P")) DeviceModel.SL20P
        else DeviceModel.SL20
    } else if (Build.MODEL.contains("SM20")) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            DeviceModel.SM20_U
        } else {
            DeviceModel.SM20
        }
    } else if (Build.MODEL.contains("PC10")) {
        DeviceModel.PC10
    } else if (Build.MODEL.contains("US30")) {
        DeviceModel.US30
    } else if (Build.MODEL.contains("SM30")) {
        DeviceModel.SM30
    } else if (Build.MODEL.contains("SL25")) {
        DeviceModel.SL25
    } else DeviceModel.UNKNOWN
}
package net.m3mobile.core.device

import android.os.Build
import net.m3mobile.core.InternalM3Api

@InternalM3Api
enum class DeviceModel {
    UNKNOWN,
    SM10,
    SM10LTE,
    SM15,
    SM15_OREO,
    SM15_A10,
    SM20,
    SM30,
    TN15,
    TN15_OREO,
    TX15,
    UL20_OREO,
    UL20_PIE,
    UL20_A10,
    UL20F,
    UL30,
    TL20,
    US20,
    US30,
    SL10,
    SL10K,
    SL20,
    SL20P;

    companion object {
        val setOfEntriesName = entries.map { it.name }.toSet()
    }
}

@InternalM3Api
@JvmSynthetic
internal fun getCurrentDeviceModel(): DeviceModel {
    return if (Build.MODEL.contains("M3SM10_LTE")) DeviceModel.SM10LTE
        else if (Build.MODEL.contains("M3SM10")) DeviceModel.SM10
        else if (Build.MODEL.contains("M3SM15")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) DeviceModel.SM15_A10
            else DeviceModel.SM15_OREO
        } else if (Build.MODEL.contains("M3UL20")) {
            when (Build.VERSION.SDK_INT) {
                Build.VERSION_CODES.Q -> DeviceModel.UL20_A10
                Build.VERSION_CODES.P -> DeviceModel.UL20_PIE
                else -> DeviceModel.UL20_OREO
            }
        } else if (Build.MODEL.contains("TN15")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) DeviceModel.TN15_OREO
            else DeviceModel.TN15
        } else if (Build.MODEL.contains("TX15")) DeviceModel.TX15
        else if (Build.MODEL.contains("TL20")) DeviceModel.TL20
        else if (Build.MODEL.contains("SL10K")) DeviceModel.SL10K
        else if (Build.MODEL.contains("SL10")) DeviceModel.SL10
        else if (Build.MODEL.contains("US20")) DeviceModel.US20
        else if (Build.MODEL.contains("SM20")) DeviceModel.SM20
        else if (Build.MODEL.contains("SL20")) DeviceModel.SL20
        else if (Build.MODEL.contains("SL20P")) DeviceModel.SL20P
        else if (Build.MODEL.contains("xchengtech")) DeviceModel.SL20
        else if (Build.MODEL.contains("M3UL20F")) DeviceModel.UL20F
        else if (Build.MODEL.contains("M3UL30")) DeviceModel.UL30
        else if (Build.MODEL.contains("M3US30")) DeviceModel.US30
        else if (Build.MODEL.contains("M3SM30")) DeviceModel.SM30
        else DeviceModel.UNKNOWN
}
package net.m3mobile.feature.scanemul.constants

internal object RequestAction {

    const val START_SCAN = "android.intent.action.M3SCANNER_BUTTON_DOWN"
    const val STOP_SCAN = "android.intent.action.M3SCANNER_BUTTON_UP"
    const val GET_SCANNER_MODULE = "com.android.server.scannerservice.m3onoff.ison"
    const val SET_SCANNER_SETTING = "com.android.server.scannerservice.settingchange"
}
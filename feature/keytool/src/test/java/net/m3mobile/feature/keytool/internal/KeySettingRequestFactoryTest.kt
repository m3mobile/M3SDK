package net.m3mobile.feature.keytool.internal

import net.m3mobile.core.device.DeviceModel
import net.m3mobile.feature.keytool.constants.ExtraKey
import net.m3mobile.feature.keytool.constants.KeyToolContract
import net.m3mobile.feature.keytool.constants.RequestAction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class KeySettingRequestFactoryTest {

    @Test
    fun functionOnlyUsesUnifiedContractWithoutWakeUp() {
        val request = KeySettingRequestFactory.keyFunctionRequest(
            KeyToolContract.LEFT_SCAN,
            "Scan",
        )

        assertEquals(KeyToolContract.SL20_PACKAGE, request.packageName)
        assertEquals(RequestAction.ACTION_SET_KEY, request.action)
        assertEquals(true, request.ordered)
        assertEquals(KeyToolContract.LEFT_SCAN, request.extra(ExtraKey.KEY_TITLE))
        assertEquals("Scan", request.extra(ExtraKey.KEY_FUNCTION))
        assertNull(request.extra(ExtraKey.KEY_WAKEUP))
    }

    @Test
    fun functionAndWakeUpUseOneUnifiedContract() {
        val request = KeySettingRequestFactory.keyFunctionRequest(
            KeyToolContract.LEFT_SCAN,
            "Scan",
            true,
        )

        assertEquals(KeyToolContract.SL20_PACKAGE, request.packageName)
        assertEquals(RequestAction.ACTION_SET_KEY, request.action)
        assertEquals(true, request.ordered)
        assertEquals(KeyToolContract.LEFT_SCAN, request.extra(ExtraKey.KEY_TITLE))
        assertEquals("Scan", request.extra(ExtraKey.KEY_FUNCTION))
        assertEquals(true, request.extra(ExtraKey.KEY_WAKEUP))
        assertNull(request.extra(ExtraKey.ENABLE_WAKEUP))
    }

    @Test
    fun legacyLeftEnableUsesLegacyContract() {
        val request = KeySettingRequestFactory.scanWakeUpRequest(
            DeviceModel.SL20P,
            KeyToolContract.LEFT_SCAN,
            true,
        )

        assertEquals(KeyToolContract.LEGACY_WAKE_UP_PACKAGE, request.packageName)
        assertEquals(RequestAction.LEFT_SCAN_WAKEUP, request.action)
        assertEquals(false, request.ordered)
        assertEquals(true, request.extra(ExtraKey.ENABLE_WAKEUP))
        assertNull(request.extra(ExtraKey.KEY_WAKEUP))
    }

    @Test
    fun legacyRightDisableUsesLegacyContract() {
        val request = KeySettingRequestFactory.scanWakeUpRequest(
            DeviceModel.SL20P,
            KeyToolContract.RIGHT_SCAN,
            false,
        )

        assertEquals(RequestAction.RIGHT_SCAN_WAKEUP, request.action)
        assertEquals(false, request.extra(ExtraKey.ENABLE_WAKEUP))
    }

    @Test
    fun unifiedLeftEnableUsesSetKeyContract() {
        val request = KeySettingRequestFactory.scanWakeUpRequest(
            DeviceModel.SM24,
            KeyToolContract.LEFT_SCAN,
            true,
        )

        assertEquals(KeyToolContract.SL20_PACKAGE, request.packageName)
        assertEquals(RequestAction.ACTION_SET_KEY, request.action)
        assertEquals(true, request.ordered)
        assertEquals(KeyToolContract.LEFT_SCAN, request.extra(ExtraKey.KEY_TITLE))
        assertEquals(true, request.extra(ExtraKey.KEY_WAKEUP))
        assertNull(request.extra(ExtraKey.ENABLE_WAKEUP))
        assertNull(request.extra(ExtraKey.KEY_FUNCTION))
    }

    @Test
    fun unifiedRightDisableUsesSetKeyContract() {
        val request = KeySettingRequestFactory.scanWakeUpRequest(
            DeviceModel.SM24,
            KeyToolContract.RIGHT_SCAN,
            false,
        )

        assertEquals(KeyToolContract.RIGHT_SCAN, request.extra(ExtraKey.KEY_TITLE))
        assertEquals(false, request.extra(ExtraKey.KEY_WAKEUP))
        assertFalse(request.extras().containsKey(ExtraKey.KEY_FUNCTION))
    }

    @Test
    fun unsupportedModelKeepsLegacyCompatibility() {
        val request = KeySettingRequestFactory.scanWakeUpRequest(
            DeviceModel.UNKNOWN,
            KeyToolContract.LEFT_SCAN,
            true,
        )

        assertEquals(KeyToolContract.LEGACY_WAKE_UP_PACKAGE, request.packageName)
        assertEquals(RequestAction.LEFT_SCAN_WAKEUP, request.action)
    }
}

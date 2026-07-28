package net.m3mobile.feature.keytool.internal

import net.m3mobile.core.device.DeviceModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class KeyToolVersionPolicyTest {

    @Test
    fun keyFunctionMinimumVersionsMatchDeviceIntroduction() {
        assertEquals("1.2.6", KeyToolVersionPolicy.keyFunctionMinimumVersion(DeviceModel.SL20))
        assertEquals("1.2.6", KeyToolVersionPolicy.keyFunctionMinimumVersion(DeviceModel.SL20K))
        assertEquals("1.2.6", KeyToolVersionPolicy.keyFunctionMinimumVersion(DeviceModel.SL20P))
        assertEquals("1.2.6", KeyToolVersionPolicy.keyFunctionMinimumVersion(DeviceModel.SL25))
        assertEquals("1.2.6", KeyToolVersionPolicy.keyFunctionMinimumVersion(DeviceModel.WD10))
        assertEquals("1.3.8", KeyToolVersionPolicy.keyFunctionMinimumVersion(DeviceModel.SM24))
        assertEquals("1.3.16", KeyToolVersionPolicy.keyFunctionMinimumVersion(DeviceModel.SM25))
    }

    @Test
    fun versionBoundariesAreInclusive() {
        assertFalse(KeyToolVersionPolicy.versionSatisfied("1.2.5", "1.2.6"))
        assertTrue(KeyToolVersionPolicy.versionSatisfied("1.2.6", "1.2.6"))
        assertFalse(KeyToolVersionPolicy.versionSatisfied("1.3.7", "1.3.8"))
        assertTrue(KeyToolVersionPolicy.versionSatisfied("1.3.8", "1.3.8"))
        assertFalse(KeyToolVersionPolicy.versionSatisfied("1.3.15", "1.3.16"))
        assertTrue(KeyToolVersionPolicy.versionSatisfied("1.3.16", "1.3.16"))
        assertFalse(KeyToolVersionPolicy.versionSatisfied("1.4.0", "1.4.1"))
        assertTrue(KeyToolVersionPolicy.versionSatisfied("1.4.1", "1.4.1"))
    }

    @Test
    fun productSuffixesKeepTheirNumericVersion() {
        assertTrue(KeyToolVersionPolicy.versionSatisfied("1.4.1_alpha", "1.4.1"))
        assertTrue(KeyToolVersionPolicy.versionSatisfied("1.3.4F", "1.3.4"))
        assertTrue(KeyToolVersionPolicy.versionSatisfied("1.4.0AD", "1.4.0"))
        assertFalse(KeyToolVersionPolicy.versionSatisfied("1.4.0AD", "1.4.1"))
    }
}

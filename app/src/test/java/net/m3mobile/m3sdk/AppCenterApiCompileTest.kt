package net.m3mobile.m3sdk

import net.m3mobile.sdk.M3Mobile
import org.junit.Test

class AppCenterApiCompileTest {

    @Test
    fun appCenterApiIsExposedThroughM3MobileInstance() {
        // Compilation of compileOnlyReferences verifies the public facade without Android initialization.
    }

    private fun compileOnlyReferences() {
        val changePassword: (String, String) -> Unit = M3Mobile.instance::changeKioskAdminPassword
        val setSleepPolicy: (Boolean) -> Unit = M3Mobile.instance::setKeepAdminModeOnSleep

        @Suppress("UNUSED_VARIABLE")
        val publicMethods = listOf(changePassword, setSleepPolicy)
    }
}

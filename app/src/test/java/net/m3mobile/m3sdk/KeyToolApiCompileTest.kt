package net.m3mobile.m3sdk

import net.m3mobile.sdk.M3Mobile
import org.junit.Test

class KeyToolApiCompileTest {

    @Test
    fun keyToolApiIsExposedThroughM3MobileInstance() {
        // Compilation of compileOnlyReferences verifies the public facade without requiring Android initialization.
    }

    private fun compileOnlyReferences() {
        val setKeyFunction: (String, String) -> Unit = M3Mobile.instance::setKeyFunction
        val enableFn: () -> Unit = M3Mobile.instance::enableFN
        val enableLeftScanWakeUp: () -> Unit = M3Mobile.instance::enableLeftScanWakeUp
        val enableHomeButton: () -> Unit = M3Mobile.instance::enableHomeButton
        val disableHomeButton: () -> Unit = M3Mobile.instance::disableHomeButton
        val enableRecentButton: () -> Unit = M3Mobile.instance::enableRecentButton
        val disableRecentButton: () -> Unit = M3Mobile.instance::disableRecentButton

        @Suppress("UNUSED_VARIABLE")
        val publicMethods = listOf(
            setKeyFunction,
            enableFn,
            enableLeftScanWakeUp,
            enableHomeButton,
            disableHomeButton,
            enableRecentButton,
            disableRecentButton
        )
    }
}

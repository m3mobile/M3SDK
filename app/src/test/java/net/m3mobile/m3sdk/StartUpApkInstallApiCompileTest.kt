package net.m3mobile.m3sdk

import net.m3mobile.sdk.M3Mobile
import org.junit.Test

class StartUpApkInstallApiCompileTest {

    @Test
    fun apkInstallOverloadsAreExposedThroughM3MobileInstance() {
        // Compilation of compileOnlyReferences verifies the public facade without Android initialization.
    }

    private fun compileOnlyReferences() {
        val installLocal: (String) -> Unit = M3Mobile.instance::installLocalApk
        val reinstallLocal: (String, Boolean) -> Unit = M3Mobile.instance::installLocalApk
        val installAndLaunchLocal: (String, Boolean, Boolean) -> Unit =
            M3Mobile.instance::installLocalApk
        val installRemote: (String) -> Unit = M3Mobile.instance::installRemoteApk
        val reinstallRemote: (String, Boolean) -> Unit = M3Mobile.instance::installRemoteApk
        val installAndLaunchRemote: (String, Boolean, Boolean) -> Unit =
            M3Mobile.instance::installRemoteApk

        @Suppress("UNUSED_VARIABLE")
        val publicMethods = listOf(
            installLocal,
            reinstallLocal,
            installAndLaunchLocal,
            installRemote,
            reinstallRemote,
            installAndLaunchRemote
        )
    }
}

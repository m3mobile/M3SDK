package net.m3mobile.feature.keytool

import android.content.pm.PackageInfo
import net.m3mobile.core.UnsatisfiedVersionException
import net.m3mobile.core.UnsupportedDeviceModelException
import net.m3mobile.core.device.DeviceModel
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], manifest = Config.NONE)
class KeyToolAppGuardTest {
    private val context get() = RuntimeEnvironment.getApplication()

    @Test
    fun backRequiresInstalledKeyTool() {
        assertThrows(KeyToolAppUnavailableException::class.java) {
            KeyToolAppGuard(context).assertBackButtonAvailable("disableBackButton", DeviceModel.SM24)
        }
    }

    @Test
    fun backRejectsOlderKeyTool() {
        installKeyTool("1.4.2")
        assertThrows(UnsatisfiedVersionException::class.java) {
            KeyToolAppGuard(context).assertBackButtonAvailable("disableBackButton", DeviceModel.SM24)
        }
    }

    @Test
    fun backAcceptsKeyTool143OnSm24() {
        installKeyTool("1.4.3")
        KeyToolAppGuard(context).assertBackButtonAvailable("disableBackButton", DeviceModel.SM24)
    }

    @Test
    fun backRejectsOtherModelsBeforeCheckingThePackage() {
        listOf(DeviceModel.SM25, DeviceModel.SL20, DeviceModel.UNKNOWN).forEach { model ->
            assertThrows(UnsupportedDeviceModelException::class.java) {
                KeyToolAppGuard(context).assertBackButtonAvailable("disableBackButton", model)
            }
        }
    }

    private fun installKeyTool(version: String) {
        val packageInfo = PackageInfo().apply {
            packageName = "com.m3.keytoolsl20"
            versionName = version
        }
        shadowOf(context.packageManager).installPackage(packageInfo)
    }
}

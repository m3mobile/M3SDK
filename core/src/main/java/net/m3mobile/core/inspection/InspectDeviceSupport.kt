package net.m3mobile.core.inspection

import net.m3mobile.core.InternalM3Api
import net.m3mobile.core.SupportedModels
import net.m3mobile.core.UnsupportedDeviceModelException
import net.m3mobile.core.UnsupportedModels
import net.m3mobile.core.device.DeviceModel
import net.m3mobile.core.device.currentDeviceModel
import net.m3mobile.core.source.DeviceSupportMapSource
import java.util.ServiceLoader

/**
 * Method를 실행한 디바이스 모델이 해당 Method를 지원하는지 검사합니다.
 *
 * 지원하지 않는 디바이스 모델일 경우 호출부로 [UnsupportedDeviceModelException]를 던집니다.
 */
@InternalM3Api
public class InspectDeviceSupport: MethodInspector<DeviceSupportMapSource, Set<String>>() {

    override val serviceLoader: ServiceLoader<DeviceSupportMapSource> =
        ServiceLoader.load(DeviceSupportMapSource::class.java)

    /**
     * 현재 디바이스 모델이 해당 메서드를 지원하는지 검사합니다.
     *
     * [SupportedModels]와 [UnsupportedModels] 어노테이션을 기반으로 검사가 이루어지며,
     * 하나의 메서드에 두 어노테이션이 모두 존재할 경우 [UnsupportedModels]는 무시됩니다.
     *
     * @throws UnsupportedDeviceModelException 지원하지 않는 모델일 경우
     */
    override fun assert(methodKey: String, methodName: String) {
        val supportedModels = methodMap[methodKey] ?: DeviceModel.setOfEntriesName

        if (currentDeviceModel.name !in supportedModels) {
            throw UnsupportedDeviceModelException(
                methodName,
                supportedModels.toTypedArray()
            )
        }
    }
}

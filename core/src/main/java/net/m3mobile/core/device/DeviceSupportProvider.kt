package net.m3mobile.core.device

/**
 * 지원하는 디바이스 모델을 특정하는 데 사용됩니다.
 *
 * Kotlin Symbol Processing에 의해 자동으로 구현되는 인터페이스입니다.
 * @see net.m3mobile.processor.DeviceSupportProcessor
 */
interface DeviceSupportProvider {
    fun getSupportMap(): Map<String, Set<String>>
}
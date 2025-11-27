package net.m3mobile.sdk.startup.params

/**
 * Represents a specific Wi-Fi Access Point configuration.
 *
 * Instances of this class cannot be created directly via the constructor.
 * Instead, use [AccessPoint.builder] to construct an instance.
 */
@ConsistentCopyVisibility
data class AccessPoint private constructor(
    val ssid: String,
    val security: String,
    val password: String?,
    val enableStatic: Boolean?,
    val ipAddress: String?,
    val mask: String?,
    val gateway: String?,
    val dns: String?,
    val macRandom: Boolean?,
    val hiddenSsid: Boolean?
) {

    interface SsidBuilder {
        fun setSsid(ssid: String): SecurityBuilder
    }

    interface SecurityBuilder {
        fun setSecurity(security: String): Builder
    }

    /**
     * Example:
     * ```kotlin
     * AccessPoint.builder()
     *      .setSsid("ssid")
     *      .setSecurity("security")
     *      .build()
     * ```
     */
    class Builder internal constructor(): SsidBuilder, SecurityBuilder {
        private lateinit var ssid: String
        private lateinit var security: String
        private var password: String? = null
        private var enableStatic: Boolean? = null
        private var ipAddress: String? = null
        private var mask: String? = null
        private var gateway: String? = null
        private var dns: String? = null
        private var macRandom: Boolean? = null
        private var hiddenSsid: Boolean? = null

        override fun setSsid(ssid: String) = apply { this.ssid = ssid } as SecurityBuilder
        override fun setSecurity(security: String) = apply { this.security = security }
        fun setPassword(password: String) = apply { this.password = password }
        fun setEnableStatic(enable: Boolean) = apply { this.enableStatic = enable }
        fun setIpAddress(ipAddress: String) = apply { this.ipAddress = ipAddress }
        fun setMask(mask: String) = apply { this.mask = mask }
        fun setGateway(gateway: String) = apply { this.gateway = gateway }
        fun setDns(dns: String) = apply { this.dns = dns }
        fun setMacRandom(macRandom: Boolean) = apply { this.macRandom = macRandom }
        fun setHiddenSsid(hiddenSsid: Boolean) = apply { this.hiddenSsid = hiddenSsid }

        fun build() = AccessPoint(
            ssid = ssid,
            security = security,
            password = password,
            enableStatic = enableStatic,
            ipAddress = ipAddress,
            mask = mask,
            gateway = gateway,
            dns = dns,
            macRandom = macRandom,
            hiddenSsid = hiddenSsid
        )
    }

    companion object {

        @JvmStatic
        fun builder() = Builder()
    }
}
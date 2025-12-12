package net.m3mobile.sdk.startup.params

/**
 * Represents a specific Wi-Fi Access Point configuration.
 *
 * Instances of this class cannot be created directly via the constructor.
 * Instead, use [AccessPoint.builder] to construct an instance.
 */
public data class AccessPoint private constructor(
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

    public interface SsidBuilder {
        public fun setSsid(ssid: String): SecurityBuilder
    }

    public interface SecurityBuilder {
        public fun setSecurity(security: String): Builder
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
    public class Builder internal constructor(): SsidBuilder, SecurityBuilder {
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

        public override fun setSsid(ssid: String): SecurityBuilder = apply { this.ssid = ssid } as SecurityBuilder
        public override fun setSecurity(security: String): Builder = apply { this.security = security }
        public fun setPassword(password: String): Builder = apply { this.password = password }
        public fun setEnableStatic(enable: Boolean): Builder = apply { this.enableStatic = enable }
        public fun setIpAddress(ipAddress: String): Builder = apply { this.ipAddress = ipAddress }
        public fun setMask(mask: String): Builder = apply { this.mask = mask }
        public fun setGateway(gateway: String): Builder = apply { this.gateway = gateway }
        public fun setDns(dns: String): Builder = apply { this.dns = dns }
        public fun setMacRandom(macRandom: Boolean): Builder = apply { this.macRandom = macRandom }
        public fun setHiddenSsid(hiddenSsid: Boolean): Builder = apply { this.hiddenSsid = hiddenSsid }

        public fun build(): AccessPoint = AccessPoint(
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

    public companion object {

        @JvmStatic
        public fun builder(): SsidBuilder = Builder()
    }
}
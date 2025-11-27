package net.m3mobile.sdk.startup.params

/**
 * Represents an Access Point Name (APN) configuration used for setting up mobile network connections.
 *
 * Instances of this class cannot be created directly via the constructor.
 * Instead, use [Apn.builder] to construct an instance.
 */
@ConsistentCopyVisibility
data class Apn private constructor(
    val name: String,
    val url: String,
    val mcc: String,
    val mnc: String,
    val type: String,
    val proxy: String?,
    val port: String?,
    val user: String?,
    val password: String?,
    val server: String?,
    val mmsc: String?,
    val mmsProxy: String?,
    val mmsPort: String?,
    val authType: Int?,
    val protocol: Int?,
    val roaming: Int?,
    val mvno: Int?,
    val mvnoValue: String?
) {

    interface NameBuilder {
        fun setName(name: String): UrlBuilder
    }

    interface UrlBuilder {
        fun setUrl(url: String): MccBuilder
    }

    interface MccBuilder {
        fun setMcc(mcc: String): MncBuilder
    }

    interface MncBuilder {
        fun setMnc(mnc: String): TypeBuilder
    }

    interface TypeBuilder {
        fun setType(type: String): Builder
    }

    /**
     * Example:
     * ```kotlin
     * Apn.builder()
     *      .setName("name")
     *      .setUrl("url")
     *      .setMcc("mcc")
     *      .setMnc("mnc")
     *      .setType("type")
     *      .build()
     * ```
     */
    class Builder internal constructor() : NameBuilder, UrlBuilder, MccBuilder, MncBuilder, TypeBuilder {
        private lateinit var name: String
        private lateinit var url: String
        private lateinit var mcc: String
        private lateinit var mnc: String
        private lateinit var type: String
        private var proxy: String? = null
        private var port: String? = null
        private var user: String? = null
        private var password: String? = null
        private var server: String? = null
        private var mmsc: String? = null
        private var mmsProxy: String? = null
        private var mmsPort: String? = null
        private var authType: Int? = null
        private var protocol: Int? = null
        private var roaming: Int? = null
        private var mvno: Int? = null
        private var mvnoValue: String? = null

        override fun setName(name: String) = apply { this.name = name } as UrlBuilder
        override fun setUrl(url: String) = apply { this.url = url } as MccBuilder
        override fun setMcc(mcc: String) = apply { this.mcc = mcc } as MncBuilder
        override fun setMnc(mnc: String) = apply { this.mnc = mnc } as TypeBuilder
        override fun setType(type: String) = apply { this.type = type }
        fun setProxy(proxy: String) = apply { this.proxy = proxy }
        fun setPort(port: String) = apply { this.port = port }
        fun setUser(user: String) = apply { this.user = user }
        fun setPassword(password: String) = apply { this.password = password }
        fun setServer(server: String) = apply { this.server = server }
        fun setMmsc(mmsc: String) = apply { this.mmsc = mmsc }
        fun setMmsProxy(mmsProxy: String) = apply { this.mmsProxy = mmsProxy }
        fun setMmsPort(mmsPort: String) = apply { this.mmsPort = mmsPort }
        fun setAuthType(authType: Int) = apply { this.authType = authType }
        fun setProtocol(protocol: Int) = apply { this.protocol = protocol }
        fun setRoaming(roaming: Int) = apply { this.roaming = roaming }
        fun setMvno(mvno: Int) = apply { this.mvno = mvno }
        fun setMvnoValue(mvnoValue: String) = apply { this.mvnoValue = mvnoValue }

        fun build() = Apn(
            name = name,
            url = url,
            mcc = mcc,
            mnc = mnc,
            type = type,
            proxy = proxy,
            port = port,
            user = user,
            password = password,
            server = server,
            mmsc = mmsc,
            mmsProxy = mmsProxy,
            mmsPort = mmsPort,
            authType = authType,
            protocol = protocol,
            roaming = roaming,
            mvno = mvno,
            mvnoValue = mvnoValue
        )
    }

    companion object {
        @JvmStatic
        fun builder() = Builder()
    }
}
package net.m3mobile.feature.startup.params

/**
 * Represents an Access Point Name (APN) configuration used for setting up mobile network connections.
 *
 * Instances of this class cannot be created directly via the constructor.
 * Instead, use [Apn.builder] to construct an instance.
 */
public data class Apn private constructor(
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

    public interface NameBuilder {
        public fun setName(name: String): UrlBuilder
    }

    public interface UrlBuilder {
        public fun setUrl(url: String): MccBuilder
    }

    public interface MccBuilder {
        public fun setMcc(mcc: String): MncBuilder
    }

    public interface MncBuilder {
        public fun setMnc(mnc: String): TypeBuilder
    }

    public interface TypeBuilder {
        public fun setType(type: String): Builder
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
    public class Builder internal constructor() : NameBuilder, UrlBuilder, MccBuilder, MncBuilder, TypeBuilder {
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

        public override fun setName(name: String): UrlBuilder = apply { this.name = name }
        public override fun setUrl(url: String): MccBuilder = apply { this.url = url }
        public override fun setMcc(mcc: String): MncBuilder = apply { this.mcc = mcc }
        public override fun setMnc(mnc: String): TypeBuilder = apply { this.mnc = mnc }
        public override fun setType(type: String): Builder = apply { this.type = type }
        public fun setProxy(proxy: String): Builder = apply { this.proxy = proxy }
        public fun setPort(port: String): Builder = apply { this.port = port }
        public fun setUser(user: String): Builder = apply { this.user = user }
        public fun setPassword(password: String): Builder = apply { this.password = password }
        public fun setServer(server: String): Builder = apply { this.server = server }
        public fun setMmsc(mmsc: String): Builder = apply { this.mmsc = mmsc }
        public fun setMmsProxy(mmsProxy: String): Builder = apply { this.mmsProxy = mmsProxy }
        public fun setMmsPort(mmsPort: String): Builder = apply { this.mmsPort = mmsPort }
        public fun setAuthType(authType: Int): Builder = apply { this.authType = authType }
        public fun setProtocol(protocol: Int): Builder = apply { this.protocol = protocol }
        public fun setRoaming(roaming: Int): Builder = apply { this.roaming = roaming }
        public fun setMvno(mvno: Int): Builder = apply { this.mvno = mvno }
        public fun setMvnoValue(mvnoValue: String): Builder = apply { this.mvnoValue = mvnoValue }

        public fun build(): Apn = Apn(
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

    public companion object {

        @JvmStatic
        public fun builder(): NameBuilder = Builder()
    }
}
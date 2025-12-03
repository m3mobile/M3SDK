package net.m3mobile.core.requester

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import net.m3mobile.core.InternalM3Api

/**
 * 브로드캐스트 요청을 보내기 위한 기본 클래스입니다.
 *
 * [참고](https://github.com/m3mobile/M3SDK/blob/main/docs/How_to_add_new_api.md)
 */
@InternalM3Api
abstract class BroadcastRequester {

    protected abstract val requestAction: String
    protected open val typeKey: String = ""
    protected open val typeValue: String = ""
    protected open val extras: Bundle = bundleOf()
    protected abstract val context: Context

    protected fun runBroadcast() {
        val intent = Intent(requestAction)

        if (!extras.isEmpty)
            intent.putExtras(extras)

        if(typeKey.isNotEmpty()) {
            intent.putExtra(typeKey, typeValue)
        }
        context.sendBroadcast(intent)
    }

    open fun request() {
        runBroadcast()
    }
}
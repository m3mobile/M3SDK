package net.m3mobile.core.requester

import android.content.Intent
import net.m3mobile.core.InternalM3Api

/**
 * 본래 원하는 요청을 보낸 후, 설정 완료 신호를 추가로 보내야 하는 브로드캐스트 전용 클래스입니다.
 */
@InternalM3Api
public abstract class FinishRequiredBroadcastRequester: BroadcastRequester() {

    override fun request() {
        super.request()

        val finishIntent = Intent("com.android.server.startupservice.config.fin")
        context.sendBroadcast(finishIntent)
    }
}
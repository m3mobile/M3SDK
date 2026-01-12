package net.m3mobile.core.requester

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import net.m3mobile.core.InternalM3Api

@InternalM3Api
public abstract class MessageRequester {

    protected abstract val context: Context
    protected abstract val intent: Intent

    private val handler: Handler = object: Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            onMessage(msg)
        }
    }
    protected val messenger: Messenger by lazy {
        Messenger(handler)
    }
    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?
        ) {
            val msg = Message.obtain(null, 0).apply {
                replyTo = messenger
            }
            val server = Messenger(service);
            server.send(msg)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("MessageRequester", "Service disconnected")
        }
    }

    public fun connect() {
        context.bindService(
            intent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    protected abstract fun onMessage(msg: Message)
}
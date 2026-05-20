using System;
using Android.Content;

namespace M3Sdk.Xamarin.Internal
{
    internal sealed class ActionBroadcastReceiver : BroadcastReceiver
    {
        private readonly Action<Context, Intent> _onReceive;

        internal ActionBroadcastReceiver(Action<Context, Intent> onReceive)
        {
            _onReceive = onReceive ?? throw new ArgumentNullException(nameof(onReceive));
        }

        /// <summary>
        /// Dispatches the received Android broadcast to the supplied callback.
        /// </summary>
        /// <param name="context">The Android context that received the broadcast.</param>
        /// <param name="intent">The received broadcast intent.</param>
        public override void OnReceive(Context context, Intent intent)
        {
            _onReceive(context, intent);
        }
    }
}

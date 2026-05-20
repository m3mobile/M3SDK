using System;
using Android.OS;

namespace M3Sdk.Xamarin.Internal
{
    internal static class CallbackDispatcher
    {
        private static readonly Handler MainHandler = new Handler(Looper.MainLooper);

        /// <summary>
        /// Runs the supplied callback on the Android main thread.
        /// </summary>
        /// <param name="action">The callback to dispatch.</param>
        internal static void PostToMain(Action action)
        {
            if (action == null)
                return;

            if (Looper.MyLooper() == Looper.MainLooper)
            {
                action();
                return;
            }

            MainHandler.Post(new ActionRunnable(action));
        }

        private sealed class ActionRunnable : Java.Lang.Object, Java.Lang.IRunnable
        {
            private readonly Action _action;

            internal ActionRunnable(Action action)
            {
                _action = action;
            }

            /// <summary>
            /// Invokes the callback passed to this runnable.
            /// </summary>
            public void Run()
            {
                _action();
            }
        }
    }
}

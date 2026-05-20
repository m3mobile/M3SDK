using System;
using System.Threading;
using System.Threading.Tasks;
using Android.Content;
using Android.OS;

namespace M3Sdk.Xamarin.Internal
{
    internal abstract class AwaitableBroadcastRequester<T> : BroadcastRequester
    {
        // StartUp and ScanEmul responses are sent by other apps, so API 33+ receivers must be exported.
        private const int ReceiverExported = 2;

        protected AwaitableBroadcastRequester(Context context)
            : base(context)
        {
        }

        protected abstract string ResponseAction { get; }

        protected virtual int TimeoutMillis
        {
            get { return 3000; }
        }

        /// <summary>
        /// Sends the request broadcast and waits for the matching response broadcast.
        /// </summary>
        /// <param name="cancellationToken">A token used to cancel the pending response wait.</param>
        /// <returns>A task that completes with the parsed response payload.</returns>
        internal Task<T> FetchAsync(CancellationToken cancellationToken)
        {
            var taskSource = new TaskCompletionSource<T>();
            var receiver = default(ActionBroadcastReceiver);
            var timeoutSource = new CancellationTokenSource();
            var cancellationRegistration = default(CancellationTokenRegistration);
            var finished = false;
            var gate = new object();

            Action<Action> finish = complete =>
            {
                lock (gate)
                {
                    if (finished)
                        return;

                    finished = true;
                }

                timeoutSource.Cancel();

                if (receiver != null)
                {
                    try
                    {
                        Context.UnregisterReceiver(receiver);
                    }
                    catch (Exception)
                    {
                    }
                }

                complete();
            };

            receiver = new ActionBroadcastReceiver((context, intent) =>
            {
                if (intent == null || intent.Action != ResponseAction)
                    return;

                try
                {
                    var data = GetExtra(intent);
                    finish(() => taskSource.TrySetResult(data));
                }
                catch (Exception ex)
                {
                    finish(() => taskSource.TrySetException(ex));
                }
            });

            try
            {
                RegisterReceiver(receiver);
            }
            catch (Exception ex)
            {
                taskSource.TrySetException(ex);
                timeoutSource.Dispose();
                return taskSource.Task;
            }

            if (cancellationToken.CanBeCanceled)
            {
                cancellationRegistration = cancellationToken.Register(() =>
                {
                    finish(() => taskSource.TrySetCanceled());
                });
            }

            Task.Delay(TimeoutMillis, timeoutSource.Token).ContinueWith(task =>
            {
                if (task.IsCanceled)
                    return;

                finish(() => taskSource.TrySetException(
                    new TimeoutException("Timed out waiting for broadcast response '" + ResponseAction + "'.")));
            });

            taskSource.Task.ContinueWith(task =>
            {
                cancellationRegistration.Dispose();
                timeoutSource.Dispose();
            });

            try
            {
                RunBroadcast();
            }
            catch (Exception ex)
            {
                finish(() => taskSource.TrySetException(ex));
            }

            return taskSource.Task;
        }

        protected abstract T GetExtra(Intent intent);

        private void RegisterReceiver(ActionBroadcastReceiver receiver)
        {
            var filter = new IntentFilter(ResponseAction);

            if ((int)Build.VERSION.SdkInt >= 33)
            {
                var method = Context.Class.GetMethod(
                    "registerReceiver",
                    Java.Lang.Class.FromType(typeof(BroadcastReceiver)),
                    Java.Lang.Class.FromType(typeof(IntentFilter)),
                    Java.Lang.Integer.Type);

                method.Invoke(Context, receiver, filter, Java.Lang.Integer.ValueOf(ReceiverExported));
                return;
            }

            Context.RegisterReceiver(receiver, filter);
        }
    }
}

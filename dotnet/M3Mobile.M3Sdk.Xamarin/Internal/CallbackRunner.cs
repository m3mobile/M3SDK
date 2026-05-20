using System;
using System.Threading;
using System.Threading.Tasks;

namespace M3Sdk.Xamarin.Internal
{
    internal static class CallbackRunner
    {
        internal static IM3Cancelable Run<T>(Func<CancellationToken, Task<T>> operation, M3RequestCallback<T> callback)
        {
            if (operation == null)
                throw new ArgumentNullException(nameof(operation));
            if (callback == null)
                throw new ArgumentNullException(nameof(callback));

            var cancelable = new M3Cancelable();

            try
            {
                operation(cancelable.Token).ContinueWith(task =>
                {
                    if (task.IsCanceled || cancelable.IsCancellationRequested)
                        return;

                    if (task.IsFaulted)
                    {
                        var error = task.Exception == null
                            ? new InvalidOperationException("The request failed.")
                            : task.Exception.GetBaseException();
                        CallbackDispatcher.PostToMain(() => callback(default(T), error));
                        return;
                    }

                    CallbackDispatcher.PostToMain(() => callback(task.Result, null));
                });
            }
            catch (Exception ex)
            {
                CallbackDispatcher.PostToMain(() => callback(default(T), ex));
            }

            return cancelable;
        }
    }
}

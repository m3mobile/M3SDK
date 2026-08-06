using System;
using System.Threading;
using System.Threading.Tasks;
using Android.Content;
using Android.OS;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.Startup
{
    internal sealed class ProjectMediaRequest
    {
        private const int TimeoutMillis = 3000;
        private readonly Context _context;
        private readonly string _packageName;

        internal ProjectMediaRequest(Context context, string packageName)
        {
            _context = context;
            _packageName = packageName;
        }

        internal async Task<ProjectMediaResult> ResultAsync(CancellationToken cancellationToken)
        {
            var resultSource = new TaskCompletionSource<ProjectMediaResult>();
            var handler = new IncomingHandler(Looper.MainLooper, message =>
            {
                var errorMessage = message.Data == null
                    ? string.Empty
                    : message.Data.GetString(Constants.StartUp.ExtraProjectMediaErrorMessage);
                resultSource.TrySetResult(ProjectMediaContract.Result(message.What, errorMessage));
            });
            var messenger = new Messenger(handler);
            var timeoutSource = CancellationTokenSource.CreateLinkedTokenSource(cancellationToken);
            timeoutSource.CancelAfter(TimeoutMillis);
            var registration = timeoutSource.Token.Register(() =>
            {
                if (cancellationToken.IsCancellationRequested)
                {
                    resultSource.TrySetCanceled();
                    return;
                }

                resultSource.TrySetException(
                    new TimeoutException("Timed out waiting for the StartUp PROJECT_MEDIA response."));
            });

            try
            {
                var intent = new Intent(Constants.StartUp.RequestSystem)
                    .SetPackage(Constants.StartUp.PackageName)
                    .PutExtra(Constants.StartUp.TypeSetting, Constants.StartUp.TypeProjectMedia)
                    .PutExtra(Constants.StartUp.ExtraProjectMediaPackage, _packageName)
                    .PutExtra(Constants.StartUp.ExtraProjectMediaMessenger, messenger);
                _context.SendBroadcast(intent);
                return await resultSource.Task.ConfigureAwait(false);
            }
            finally
            {
                registration.Dispose();
                timeoutSource.Dispose();
                messenger.Dispose();
                handler.Dispose();
            }
        }

        private sealed class IncomingHandler : Handler
        {
            private readonly Action<Message> _message;

            internal IncomingHandler(Looper looper, Action<Message> message)
                : base(looper)
            {
                _message = message;
            }

            public override void HandleMessage(Message message)
            {
                _message(message);
            }
        }
    }
}

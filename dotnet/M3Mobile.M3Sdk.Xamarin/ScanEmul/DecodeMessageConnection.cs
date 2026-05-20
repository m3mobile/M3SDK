using System;
using System.Collections.Generic;
using Android.Content;
using Android.OS;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.ScanEmul
{
    internal sealed class DecodeMessageConnection : IDisposable
    {
        private readonly Context _context;
        private readonly Intent _intent;
        private readonly IncomingHandler _handler;
        private readonly Messenger _clientMessenger;
        private readonly DecodeServiceConnection _connection;
        private bool _isBound;
        private bool _disposed;

        /// <summary>
        /// Creates a Messenger connection to the ScanEmul decode service.
        /// </summary>
        /// <param name="context">The Android context used to bind the ScanEmul service.</param>
        internal DecodeMessageConnection(Context context)
        {
            _context = context;
            _intent = new Intent(Constants.ScanEmul.ConnectionAction)
                .SetPackage(Constants.ScanEmul.PackageName);
            _handler = new IncomingHandler(Looper.MainLooper, OnMessage);
            _clientMessenger = new Messenger(_handler);
            _connection = new DecodeServiceConnection(_clientMessenger);
        }

        /// <summary>
        /// Raised when the ScanEmul service reports a decoded barcode result.
        /// </summary>
        internal event EventHandler<ScanResultEventArgs> ScanResultReceived;

        /// <summary>
        /// Raised when the ScanEmul service reports GS1 parsed data.
        /// </summary>
        internal event EventHandler<GS1ParsedEventArgs> GS1ParsedReceived;

        /// <summary>
        /// Raised when the ScanEmul service reports Digital Link parsed data.
        /// </summary>
        internal event EventHandler<DigitalLinkParsedEventArgs> DigitalLinkParsedReceived;

        /// <summary>
        /// Binds to the ScanEmul decode service if it is not already bound.
        /// </summary>
        internal void Connect()
        {
            if (_isBound)
                return;

            _isBound = _context.BindService(_intent, _connection, Bind.AutoCreate);
        }

        /// <summary>
        /// Unbinds the ScanEmul decode service connection.
        /// </summary>
        public void Dispose()
        {
            if (_disposed)
                return;

            _disposed = true;

            if (_isBound)
            {
                try
                {
                    _context.UnbindService(_connection);
                }
                catch (Exception)
                {
                }

                _isBound = false;
            }
        }

        private void OnMessage(Message message)
        {
            if (message == null)
                return;

            var bundle = message.Obj as Bundle;
            if (bundle == null)
                return;

            switch (message.What)
            {
                case Constants.ScanEmul.MessageDecodeComplete:
                    EmitScanResult(bundle);
                    break;
                case Constants.ScanEmul.MessageGs1ParsingComplete:
                    EmitGs1Parsed(bundle);
                    break;
                case Constants.ScanEmul.MessageDigitalLinkParsingComplete:
                    EmitDigitalLinkParsed(bundle);
                    break;
            }
        }

        private void EmitScanResult(Bundle bundle)
        {
            var barcode = bundle.GetString(Constants.ScanEmul.MessageBarcode);
            var codeType = bundle.GetString(Constants.ScanEmul.MessageCodeType);
            var rawData = bundle.GetByteArray(Constants.ScanEmul.MessageRawData);
            if (barcode == null || codeType == null || rawData == null)
                return;

            var handler = ScanResultReceived;
            if (handler != null)
                handler(this, new ScanResultEventArgs(new ScanResult(barcode, codeType, rawData)));
        }

        private void EmitGs1Parsed(Bundle bundle)
        {
            var handler = GS1ParsedReceived;
            if (handler != null)
                handler(this, new GS1ParsedEventArgs(ParseGs1ParsedList(bundle)));
        }

        private void EmitDigitalLinkParsed(Bundle bundle)
        {
            var handler = DigitalLinkParsedReceived;
            if (handler != null)
                handler(this, new DigitalLinkParsedEventArgs(ParseDigitalLinkParsedList(bundle)));
        }

        private static IList<GS1ParsedData> ParseGs1ParsedList(Bundle bundle)
        {
            var result = new List<GS1ParsedData>();
            foreach (var item in GetParsedBundleItems(bundle))
            {
                var ai = item.GetString(Constants.ScanEmul.MessageParsedAi);
                var data = item.GetString(Constants.ScanEmul.MessageParsedData);
                var description = item.GetString(Constants.ScanEmul.MessageParsedDescription);
                if (ai != null && data != null && description != null)
                    result.Add(new GS1ParsedData(ai, data, description));
            }

            return result;
        }

        private static IList<DigitalLinkParsedData> ParseDigitalLinkParsedList(Bundle bundle)
        {
            var result = new List<DigitalLinkParsedData>();
            foreach (var item in GetParsedBundleItems(bundle))
            {
                var ai = item.GetString(Constants.ScanEmul.MessageParsedAi);
                var data = item.GetString(Constants.ScanEmul.MessageParsedData);
                var description = item.GetString(Constants.ScanEmul.MessageParsedDescription);
                if (ai != null && data != null && description != null)
                    result.Add(new DigitalLinkParsedData(ai, data, description));
            }

            return result;
        }

        private static IEnumerable<Bundle> GetParsedBundleItems(Bundle bundle)
        {
            var list = bundle.GetParcelableArrayList(Constants.ScanEmul.MessageParsedList);
            if (list == null)
                yield break;

            foreach (var item in list)
            {
                var child = item as Bundle;
                if (child != null)
                    yield return child;
            }
        }

        private sealed class IncomingHandler : Handler
        {
            private readonly Action<Message> _onMessage;

            /// <summary>
            /// Creates a handler for ScanEmul Messenger callbacks.
            /// </summary>
            /// <param name="looper">The looper used to receive Messenger messages.</param>
            /// <param name="onMessage">The callback that parses incoming messages.</param>
            internal IncomingHandler(Looper looper, Action<Message> onMessage)
                : base(looper)
            {
                _onMessage = onMessage;
            }

            /// <summary>
            /// Dispatches incoming Messenger messages to the connection parser.
            /// </summary>
            /// <param name="msg">The incoming ScanEmul Messenger message.</param>
            public override void HandleMessage(Message msg)
            {
                _onMessage(msg);
            }
        }

        private sealed class DecodeServiceConnection : Java.Lang.Object, IServiceConnection
        {
            private readonly Messenger _clientMessenger;

            /// <summary>
            /// Creates a service connection that registers the client Messenger with ScanEmul.
            /// </summary>
            /// <param name="clientMessenger">The Messenger that receives decode result callbacks.</param>
            internal DecodeServiceConnection(Messenger clientMessenger)
            {
                _clientMessenger = clientMessenger;
            }

            /// <summary>
            /// Sends the client Messenger to the ScanEmul service after binding succeeds.
            /// </summary>
            /// <param name="name">The connected service component.</param>
            /// <param name="service">The binder returned by the connected service.</param>
            public void OnServiceConnected(ComponentName name, IBinder service)
            {
                try
                {
                    var message = Message.Obtain(null, 0);
                    message.ReplyTo = _clientMessenger;
                    var server = new Messenger(service);
                    server.Send(message);
                }
                catch (Exception)
                {
                }
            }

            /// <summary>
            /// Handles ScanEmul service disconnection.
            /// </summary>
            /// <param name="name">The disconnected service component.</param>
            public void OnServiceDisconnected(ComponentName name)
            {
            }

            /// <summary>
            /// Handles ScanEmul service binding death.
            /// </summary>
            /// <param name="name">The affected service component.</param>
            public void OnBindingDied(ComponentName name)
            {
            }

            /// <summary>
            /// Handles a null ScanEmul service binding.
            /// </summary>
            /// <param name="name">The affected service component.</param>
            public void OnNullBinding(ComponentName name)
            {
            }
        }
    }
}

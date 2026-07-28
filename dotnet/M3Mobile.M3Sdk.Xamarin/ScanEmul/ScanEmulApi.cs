using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Android.App;
using Android.Content;
using Android.Content.PM;
using Android.OS;
using M3Sdk.Xamarin.Internal;

namespace M3Sdk.Xamarin.ScanEmul
{
    /// <summary>
    /// ScanEmul app API surface implemented by native Android broadcasts and Messenger.
    /// </summary>
    public sealed class ScanEmulApi : IScanEmulApi
    {
        private readonly Context _context;
        private readonly M3SdkGuard _guard;
        private readonly Dictionary<IOnScanResultListener, EventHandler<ScanResultEventArgs>> _scanResultListeners =
            new Dictionary<IOnScanResultListener, EventHandler<ScanResultEventArgs>>();
        private readonly Dictionary<IOnGS1ParsedListener, EventHandler<GS1ParsedEventArgs>> _gs1ParsedListeners =
            new Dictionary<IOnGS1ParsedListener, EventHandler<GS1ParsedEventArgs>>();
        private readonly Dictionary<IOnDigitalLinkParsedListener, EventHandler<DigitalLinkParsedEventArgs>> _digitalLinkParsedListeners =
            new Dictionary<IOnDigitalLinkParsedListener, EventHandler<DigitalLinkParsedEventArgs>>();
        private readonly object _gate = new object();
        private DecodeMessageConnection _decodeConnection;
        private bool _disposed;

        internal ScanEmulApi(Context context, M3SdkGuard guard)
        {
            _context = context;
            _guard = guard;
        }

        /// <inheritdoc />
        public event EventHandler<ScanResultEventArgs> ScanResultReceived
        {
            add
            {
                if (value == null)
                    return;

                GuardScanEmul("ScanResultReceived", "4.11.0");
                EnsureDecodeConnection().ScanResultReceived += value;
            }
            remove
            {
                if (value == null || _decodeConnection == null)
                    return;

                _decodeConnection.ScanResultReceived -= value;
            }
        }

        /// <inheritdoc />
        public event EventHandler<GS1ParsedEventArgs> GS1ParsedReceived
        {
            add
            {
                if (value == null)
                    return;

                GuardScanEmul("GS1ParsedReceived", "4.11.0");
                EnsureDecodeConnection().GS1ParsedReceived += value;
            }
            remove
            {
                if (value == null || _decodeConnection == null)
                    return;

                _decodeConnection.GS1ParsedReceived -= value;
            }
        }

        /// <inheritdoc />
        public event EventHandler<DigitalLinkParsedEventArgs> DigitalLinkParsedReceived
        {
            add
            {
                if (value == null)
                    return;

                GuardScanEmul("DigitalLinkParsedReceived", "4.11.0");
                EnsureDecodeConnection().DigitalLinkParsedReceived += value;
            }
            remove
            {
                if (value == null || _decodeConnection == null)
                    return;

                _decodeConnection.DigitalLinkParsedReceived -= value;
            }
        }

        /// <inheritdoc />
        public void StartScan()
        {
            ThrowIfDisposed();
            GuardScanEmul("StartScan", "2.13.0");
            new SimpleBroadcastRequester(_context, Constants.ScanEmul.StartScan).Request();
        }

        /// <inheritdoc />
        public void StopScan()
        {
            ThrowIfDisposed();
            GuardScanEmul("StopScan", "2.13.0");
            new SimpleBroadcastRequester(_context, Constants.ScanEmul.StopScan).Request();
        }

        /// <inheritdoc />
        public Task<string> GetScannerTypeAsync()
        {
            return GetScannerTypeAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetScannerTypeAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            GuardScanEmul("GetScannerType", "2.13.0");
            return new GetScannerTypeRequester(_context).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScannerType(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(GetScannerTypeAsync, callback);
        }

        /// <inheritdoc />
        public Task<int> GetScannerStatusAsync()
        {
            return GetScannerStatusAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<int> GetScannerStatusAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            GuardScanEmul("GetScannerStatus", "2.13.0");
            return new GetScannerStatusRequester(_context).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScannerStatus(M3RequestCallback<int> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(GetScannerStatusAsync, callback);
        }

        /// <inheritdoc />
        public void RegisterOnScanResultListener(IOnScanResultListener listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                throw new ArgumentNullException(nameof(listener));

            GuardScanEmul("RegisterOnScanResultListener", "4.11.0");
            lock (_gate)
            {
                EventHandler<ScanResultEventArgs> existing;
                if (_scanResultListeners.TryGetValue(listener, out existing))
                    EnsureDecodeConnection().ScanResultReceived -= existing;

                EventHandler<ScanResultEventArgs> handler = (sender, args) => listener.OnScanResult(args.Result);
                _scanResultListeners[listener] = handler;
                EnsureDecodeConnection().ScanResultReceived += handler;
            }
        }

        /// <inheritdoc />
        public IDisposable RegisterOnScanResultListener(Action<ScanResult> listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                throw new ArgumentNullException(nameof(listener));

            GuardScanEmul("RegisterOnScanResultListener", "4.11.0");
            EventHandler<ScanResultEventArgs> handler = (sender, args) => listener(args.Result);
            EnsureDecodeConnection().ScanResultReceived += handler;
            return new Registration(() =>
            {
                if (_decodeConnection != null)
                    _decodeConnection.ScanResultReceived -= handler;
            });
        }

        /// <inheritdoc />
        public void UnregisterOnScanResultListener(IOnScanResultListener listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                return;

            lock (_gate)
            {
                EventHandler<ScanResultEventArgs> handler;
                if (_scanResultListeners.TryGetValue(listener, out handler))
                {
                    _scanResultListeners.Remove(listener);
                    if (_decodeConnection != null)
                        _decodeConnection.ScanResultReceived -= handler;
                }
            }
        }

        /// <inheritdoc />
        public void RegisterOnGS1ParsedListener(IOnGS1ParsedListener listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                throw new ArgumentNullException(nameof(listener));

            GuardScanEmul("RegisterOnGS1ParsedListener", "4.11.0");
            lock (_gate)
            {
                EventHandler<GS1ParsedEventArgs> existing;
                if (_gs1ParsedListeners.TryGetValue(listener, out existing))
                    EnsureDecodeConnection().GS1ParsedReceived -= existing;

                EventHandler<GS1ParsedEventArgs> handler = (sender, args) => listener.OnGS1Parsed(args.Result);
                _gs1ParsedListeners[listener] = handler;
                EnsureDecodeConnection().GS1ParsedReceived += handler;
            }
        }

        /// <inheritdoc />
        public IDisposable RegisterOnGS1ParsedListener(Action<IList<GS1ParsedData>> listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                throw new ArgumentNullException(nameof(listener));

            GuardScanEmul("RegisterOnGS1ParsedListener", "4.11.0");
            EventHandler<GS1ParsedEventArgs> handler = (sender, args) => listener(args.Result);
            EnsureDecodeConnection().GS1ParsedReceived += handler;
            return new Registration(() =>
            {
                if (_decodeConnection != null)
                    _decodeConnection.GS1ParsedReceived -= handler;
            });
        }

        /// <inheritdoc />
        public void UnregisterOnGS1ParsedListener(IOnGS1ParsedListener listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                return;

            lock (_gate)
            {
                EventHandler<GS1ParsedEventArgs> handler;
                if (_gs1ParsedListeners.TryGetValue(listener, out handler))
                {
                    _gs1ParsedListeners.Remove(listener);
                    if (_decodeConnection != null)
                        _decodeConnection.GS1ParsedReceived -= handler;
                }
            }
        }

        /// <inheritdoc />
        public void RegisterOnDigitalLinkParsedListener(IOnDigitalLinkParsedListener listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                throw new ArgumentNullException(nameof(listener));

            GuardScanEmul("RegisterOnDigitalLinkParsedListener", "4.11.0");
            lock (_gate)
            {
                EventHandler<DigitalLinkParsedEventArgs> existing;
                if (_digitalLinkParsedListeners.TryGetValue(listener, out existing))
                    EnsureDecodeConnection().DigitalLinkParsedReceived -= existing;

                EventHandler<DigitalLinkParsedEventArgs> handler = (sender, args) => listener.OnDigitalLinkParsed(args.Result);
                _digitalLinkParsedListeners[listener] = handler;
                EnsureDecodeConnection().DigitalLinkParsedReceived += handler;
            }
        }

        /// <inheritdoc />
        public IDisposable RegisterOnDigitalLinkParsedListener(Action<IList<DigitalLinkParsedData>> listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                throw new ArgumentNullException(nameof(listener));

            GuardScanEmul("RegisterOnDigitalLinkParsedListener", "4.11.0");
            EventHandler<DigitalLinkParsedEventArgs> handler = (sender, args) => listener(args.Result);
            EnsureDecodeConnection().DigitalLinkParsedReceived += handler;
            return new Registration(() =>
            {
                if (_decodeConnection != null)
                    _decodeConnection.DigitalLinkParsedReceived -= handler;
            });
        }

        /// <inheritdoc />
        public void UnregisterOnDigitalLinkParsedListener(IOnDigitalLinkParsedListener listener)
        {
            ThrowIfDisposed();
            if (listener == null)
                return;

            lock (_gate)
            {
                EventHandler<DigitalLinkParsedEventArgs> handler;
                if (_digitalLinkParsedListeners.TryGetValue(listener, out handler))
                {
                    _digitalLinkParsedListeners.Remove(listener);
                    if (_decodeConnection != null)
                        _decodeConnection.DigitalLinkParsedReceived -= handler;
                }
            }
        }

        /// <inheritdoc />
        public void SetScanSound(ScanSound sound)
        {
            ThrowIfDisposed();
            GuardScanEmul("SetScanSound", "2.11.0");
            SendSetting(Constants.ScanEmul.TypeSound, IntExtra(Constants.ScanEmul.ExtraSound, (int)sound));
        }

        /// <inheritdoc />
        public void EnableScanVibration()
        {
            SetScanVibration("EnableScanVibration", true);
        }

        /// <inheritdoc />
        public void DisableScanVibration()
        {
            SetScanVibration("DisableScanVibration", false);
        }

        /// <inheritdoc />
        public void EnableScanLed()
        {
            SetScanLed("EnableScanLed", true);
        }

        /// <inheritdoc />
        public void DisableScanLed()
        {
            SetScanLed("DisableScanLed", false);
        }

        /// <inheritdoc />
        public void SetScanLedTime(int timeMillis)
        {
            ThrowIfDisposed();
            GuardScanEmul("SetScanLedTime", "2.11.0");
            SendSetting(Constants.ScanEmul.TypeLedTime, IntExtra(Constants.ScanEmul.ExtraLedTime, timeMillis));
        }

        /// <inheritdoc />
        public void SetScannerReadMode(ReadMode mode)
        {
            ThrowIfDisposed();
            GuardScanEmul("SetScannerReadMode", "2.11.0");
            SendSetting(Constants.ScanEmul.TypeReadMode, IntExtra(Constants.ScanEmul.ExtraReadMode, (int)mode));
        }

        /// <inheritdoc />
        public void SetScanResultOutputMode(OutputMode mode)
        {
            ThrowIfDisposed();
            GuardScanEmul("SetScanResultOutputMode", "2.11.0");
            SendSetting(Constants.ScanEmul.TypeOutputMode, IntExtra(Constants.ScanEmul.ExtraOutputMode, (int)mode));
        }

        /// <inheritdoc />
        public void SetScanResultEndCharacter(EndCharacter endCharacter)
        {
            ThrowIfDisposed();
            GuardScanEmul("SetScanResultEndCharacter", "2.11.0");
            SendSetting(Constants.ScanEmul.TypeEndCharacter, IntExtra(Constants.ScanEmul.ExtraEndCharacter, (int)endCharacter));
        }

        /// <inheritdoc />
        public void SetScanResultPrefix(string prefix)
        {
            ThrowIfDisposed();
            if (prefix == null)
                throw new ArgumentNullException(nameof(prefix));

            GuardScanEmul("SetScanResultPrefix", "2.11.0");
            SendSetting(Constants.ScanEmul.TypePrefix, StringExtra(Constants.ScanEmul.ExtraPrefix, prefix));
        }

        /// <inheritdoc />
        public void SetScanResultPostfix(string postfix)
        {
            ThrowIfDisposed();
            if (postfix == null)
                throw new ArgumentNullException(nameof(postfix));

            GuardScanEmul("SetScanResultPostfix", "2.11.0");
            SendSetting(Constants.ScanEmul.TypePostfix, StringExtra(Constants.ScanEmul.ExtraPostfix, postfix));
        }

        /// <inheritdoc />
        public Task<string> GetScanResultPrefixAsync()
        {
            return GetScanResultPrefixAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetScanResultPrefixAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            GuardScanEmul("GetScanResultPrefix", "2.11.0");
            return new ScannerSettingStringRequester(_context, Constants.ScanEmul.ResponsePrefix).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScanResultPrefix(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(GetScanResultPrefixAsync, callback);
        }

        /// <inheritdoc />
        public Task<string> GetScanResultPostfixAsync()
        {
            return GetScanResultPostfixAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<string> GetScanResultPostfixAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            GuardScanEmul("GetScanResultPostfix", "2.11.0");
            return new ScannerSettingStringRequester(_context, Constants.ScanEmul.ResponsePostfix).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScanResultPostfix(M3RequestCallback<string> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(GetScanResultPostfixAsync, callback);
        }

        /// <inheritdoc />
        public Task<EndCharacter> GetScanResultEndCharacterAsync()
        {
            return GetScanResultEndCharacterAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<EndCharacter> GetScanResultEndCharacterAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            GuardScanEmul("GetScanResultEndCharacter", "2.11.0");
            return new ScannerSettingEndCharacterRequester(_context).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScanResultEndCharacter(M3RequestCallback<EndCharacter> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(GetScanResultEndCharacterAsync, callback);
        }

        /// <inheritdoc />
        public Task<OutputMode> GetScanResultOutputModeAsync()
        {
            return GetScanResultOutputModeAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<OutputMode> GetScanResultOutputModeAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            GuardScanEmul("GetScanResultOutputMode", "2.11.0");
            return new ScannerSettingOutputModeRequester(_context).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScanResultOutputMode(M3RequestCallback<OutputMode> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(GetScanResultOutputModeAsync, callback);
        }

        /// <inheritdoc />
        public Task<bool> IsScannerProfileEnabledAsync()
        {
            return IsScannerProfileEnabledAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<bool> IsScannerProfileEnabledAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            GuardScanEmul("IsScannerProfileEnabled", "2.11.0");
            return new ScannerProfileEnabledRequester(_context).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable IsScannerProfileEnabled(M3RequestCallback<bool> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(IsScannerProfileEnabledAsync, callback);
        }

        /// <inheritdoc />
        public Task<ReadMode> GetScannerReadModeAsync()
        {
            return GetScannerReadModeAsync(CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<ReadMode> GetScannerReadModeAsync(CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            GuardScanEmul("GetScannerReadMode", "2.11.0");
            return new ScannerReadModeRequester(_context).FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScannerReadMode(M3RequestCallback<ReadMode> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(GetScannerReadModeAsync, callback);
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiResult> SetScannerButtonUiAsync(ScannerButtonUiOptions options)
        {
            return SetScannerButtonUiAsync(options, ScannerButtonUiRequester.CreateRequestId());
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiResult> SetScannerButtonUiAsync(
            ScannerButtonUiOptions options,
            CancellationToken cancellationToken)
        {
            return SetScannerButtonUiAsync(
                options,
                ScannerButtonUiRequester.CreateRequestId(),
                cancellationToken);
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiResult> SetScannerButtonUiAsync(
            ScannerButtonUiOptions options,
            string requestId)
        {
            return SetScannerButtonUiAsync(options, requestId, CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiResult> SetScannerButtonUiAsync(
            ScannerButtonUiOptions options,
            string requestId,
            CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            if (options == null)
                throw new ArgumentNullException(nameof(options));

            return new ScannerButtonUiRequester(_context, requestId, options)
                .FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable SetScannerButtonUi(
            ScannerButtonUiOptions options,
            M3RequestCallback<ScannerButtonUiResult> callback)
        {
            ThrowIfDisposed();
            return SetScannerButtonUi(options, ScannerButtonUiRequester.CreateRequestId(), callback);
        }

        /// <inheritdoc />
        public IM3Cancelable SetScannerButtonUi(
            ScannerButtonUiOptions options,
            string requestId,
            M3RequestCallback<ScannerButtonUiResult> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(
                token => SetScannerButtonUiAsync(options, requestId, token),
                callback);
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiResult> GetScannerButtonUiAsync()
        {
            return GetScannerButtonUiAsync(ScannerButtonUiRequester.CreateRequestId());
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiResult> GetScannerButtonUiAsync(CancellationToken cancellationToken)
        {
            return GetScannerButtonUiAsync(
                ScannerButtonUiRequester.CreateRequestId(),
                cancellationToken);
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiResult> GetScannerButtonUiAsync(string requestId)
        {
            return GetScannerButtonUiAsync(requestId, CancellationToken.None);
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiResult> GetScannerButtonUiAsync(
            string requestId,
            CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            return new ScannerButtonUiRequester(_context, requestId, null)
                .FetchAsync(cancellationToken);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScannerButtonUi(M3RequestCallback<ScannerButtonUiResult> callback)
        {
            ThrowIfDisposed();
            return GetScannerButtonUi(ScannerButtonUiRequester.CreateRequestId(), callback);
        }

        /// <inheritdoc />
        public IM3Cancelable GetScannerButtonUi(
            string requestId,
            M3RequestCallback<ScannerButtonUiResult> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(token => GetScannerButtonUiAsync(requestId, token), callback);
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiVerificationResult> SetAndVerifyScannerButtonUiAsync(
            ScannerButtonUiOptions options)
        {
            return SetAndVerifyScannerButtonUiAsync(options, ScannerButtonUiRequester.CreateRequestId());
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiVerificationResult> SetAndVerifyScannerButtonUiAsync(
            ScannerButtonUiOptions options,
            CancellationToken cancellationToken)
        {
            return SetAndVerifyScannerButtonUiAsync(
                options,
                ScannerButtonUiRequester.CreateRequestId(),
                cancellationToken);
        }

        /// <inheritdoc />
        public Task<ScannerButtonUiVerificationResult> SetAndVerifyScannerButtonUiAsync(
            ScannerButtonUiOptions options,
            string requestId)
        {
            return SetAndVerifyScannerButtonUiAsync(options, requestId, CancellationToken.None);
        }

        /// <inheritdoc />
        public async Task<ScannerButtonUiVerificationResult> SetAndVerifyScannerButtonUiAsync(
            ScannerButtonUiOptions options,
            string requestId,
            CancellationToken cancellationToken)
        {
            ThrowIfDisposed();
            var setResult = await SetScannerButtonUiAsync(options, requestId, cancellationToken);
            var getResult = setResult.IsSaved
                ? await GetScannerButtonUiAsync(ScannerButtonUiRequester.CreateRequestId(), cancellationToken)
                : null;
            return new ScannerButtonUiVerificationResult(setResult, getResult);
        }

        /// <inheritdoc />
        public IM3Cancelable SetAndVerifyScannerButtonUi(
            ScannerButtonUiOptions options,
            M3RequestCallback<ScannerButtonUiVerificationResult> callback)
        {
            ThrowIfDisposed();
            return SetAndVerifyScannerButtonUi(options, ScannerButtonUiRequester.CreateRequestId(), callback);
        }

        /// <inheritdoc />
        public IM3Cancelable SetAndVerifyScannerButtonUi(
            ScannerButtonUiOptions options,
            string requestId,
            M3RequestCallback<ScannerButtonUiVerificationResult> callback)
        {
            ThrowIfDisposed();
            return CallbackRunner.Run(
                token => SetAndVerifyScannerButtonUiAsync(options, requestId, token),
                callback);
        }

        /// <inheritdoc />
        public void Dispose()
        {
            if (_disposed)
                return;

            _disposed = true;
            if (_decodeConnection != null)
                _decodeConnection.Dispose();
        }

        private void SetScanVibration(string methodName, bool enabled)
        {
            ThrowIfDisposed();
            GuardScanEmul(methodName, "2.11.0");
            SendSetting(Constants.ScanEmul.TypeVibration, IntExtra(Constants.ScanEmul.ExtraVibration, enabled ? 1 : 0));
        }

        private void SetScanLed(string methodName, bool enabled)
        {
            ThrowIfDisposed();
            GuardScanEmul(methodName, "2.11.0");
            SendSetting(Constants.ScanEmul.TypeLed, IntExtra(Constants.ScanEmul.ExtraLed, enabled ? 1 : 0));
        }

        private void SendSetting(string typeValue, Bundle extras)
        {
            new ScannerSettingRequester(_context, typeValue, extras).Request();
        }

        private void GuardScanEmul(string methodName, string version)
        {
            _guard.AssertScanEmulVersion(methodName, version);
        }

        private DecodeMessageConnection EnsureDecodeConnection()
        {
            ThrowIfDisposed();

            if (_decodeConnection != null)
                return _decodeConnection;

            lock (_gate)
            {
                if (_decodeConnection == null)
                {
                    _decodeConnection = new DecodeMessageConnection(_context);
                    _decodeConnection.Connect();
                }
            }

            return _decodeConnection;
        }

        private void ThrowIfDisposed()
        {
            if (_disposed)
                throw new ObjectDisposedException(GetType().FullName);
        }

        private static Bundle IntExtra(string key, int value)
        {
            var extras = new Bundle();
            extras.PutInt(key, value);
            return extras;
        }

        private static Bundle StringExtra(string key, string value)
        {
            var extras = new Bundle();
            extras.PutString(key, value);
            return extras;
        }

        private sealed class Registration : IDisposable
        {
            private readonly Action _dispose;
            private bool _disposed;

            internal Registration(Action dispose)
            {
                _dispose = dispose;
            }

            /// <summary>
            /// Removes the event handler registered by the callback overload.
            /// </summary>
            public void Dispose()
            {
                if (_disposed)
                    return;

                _disposed = true;
                _dispose();
            }
        }

        private sealed class SimpleBroadcastRequester : BroadcastRequester
        {
            private readonly string _requestAction;

            internal SimpleBroadcastRequester(Context context, string requestAction)
                : base(context)
            {
                _requestAction = requestAction;
            }

            protected override string RequestAction
            {
                get { return _requestAction; }
            }

        }

        private sealed class ScannerSettingRequester : BroadcastRequester
        {
            private readonly string _typeValue;
            private readonly Bundle _extras;

            internal ScannerSettingRequester(Context context, string typeValue, Bundle extras)
                : base(context)
            {
                _typeValue = typeValue;
                _extras = extras;
            }

            protected override string RequestAction
            {
                get { return Constants.ScanEmul.SetScannerSetting; }
            }

            protected override string TypeKey
            {
                get { return Constants.ScanEmul.TypeSetting; }
            }

            protected override string TypeValue
            {
                get { return _typeValue; }
            }

            protected override Bundle CreateExtras()
            {
                return _extras;
            }
        }

        private abstract class ScannerModuleRequester<T> : AwaitableBroadcastRequester<T>
        {
            protected ScannerModuleRequester(Context context)
                : base(context)
            {
            }

            protected override string RequestAction
            {
                get { return Constants.ScanEmul.GetScannerModule; }
            }

            protected override string ResponseAction
            {
                get { return Constants.ScanEmul.ResponseScannerModule; }
            }
        }

        private sealed class GetScannerTypeRequester : ScannerModuleRequester<string>
        {
            internal GetScannerTypeRequester(Context context)
                : base(context)
            {
            }

            protected override string GetExtra(Intent intent)
            {
                return intent.GetStringExtra(Constants.ScanEmul.ResponseScannerModuleType) ?? string.Empty;
            }
        }

        private sealed class GetScannerStatusRequester : ScannerModuleRequester<int>
        {
            internal GetScannerStatusRequester(Context context)
                : base(context)
            {
            }

            protected override int GetExtra(Intent intent)
            {
                return intent.GetIntExtra(Constants.ScanEmul.ResponseScannerStatus, -1);
            }
        }

        private abstract class ScannerSettingFetchRequester<T> : AwaitableBroadcastRequester<T>
        {
            protected ScannerSettingFetchRequester(Context context)
                : base(context)
            {
            }

            protected override string RequestAction
            {
                get { return Constants.ScanEmul.GetScannerSetting; }
            }

            protected override string ResponseAction
            {
                get { return Constants.ScanEmul.ResponseScannerSetting; }
            }
        }

        private sealed class ScannerSettingStringRequester : ScannerSettingFetchRequester<string>
        {
            private readonly string _extraKey;

            internal ScannerSettingStringRequester(Context context, string extraKey)
                : base(context)
            {
                _extraKey = extraKey;
            }

            protected override string GetExtra(Intent intent)
            {
                var value = intent.GetStringExtra(_extraKey);
                if (value == null)
                    throw new InvalidOperationException("Failed to get extra '" + _extraKey + "' or type mismatch.");
                return value;
            }
        }

        private sealed class ScannerSettingEndCharacterRequester : ScannerSettingFetchRequester<EndCharacter>
        {
            internal ScannerSettingEndCharacterRequester(Context context)
                : base(context)
            {
            }

            protected override EndCharacter GetExtra(Intent intent)
            {
                var value = intent.GetIntExtra(Constants.ScanEmul.ResponseEndCharacter, 0);
                if (!Enum.IsDefined(typeof(EndCharacter), value))
                    throw new InvalidOperationException("Failed to map end character value '" + value + "'.");
                return (EndCharacter)value;
            }
        }

        private sealed class ScannerSettingOutputModeRequester : ScannerSettingFetchRequester<OutputMode>
        {
            internal ScannerSettingOutputModeRequester(Context context)
                : base(context)
            {
            }

            protected override OutputMode GetExtra(Intent intent)
            {
                var value = intent.GetIntExtra(Constants.ScanEmul.ResponseOutputMode, 0);
                if (!Enum.IsDefined(typeof(OutputMode), value))
                    throw new InvalidOperationException("Failed to map output mode value '" + value + "'.");
                return (OutputMode)value;
            }
        }

        private sealed class ScannerProfileEnabledRequester : ScannerSettingFetchRequester<bool>
        {
            internal ScannerProfileEnabledRequester(Context context)
                : base(context)
            {
            }

            protected override bool GetExtra(Intent intent)
            {
                return intent.GetBooleanExtra(Constants.ScanEmul.ResponseProfileEnabled, false);
            }
        }

        private sealed class ScannerReadModeRequester : ScannerSettingFetchRequester<ReadMode>
        {
            internal ScannerReadModeRequester(Context context)
                : base(context)
            {
            }

            protected override ReadMode GetExtra(Intent intent)
            {
                var value = intent.GetIntExtra(Constants.ScanEmul.ResponseReadMode, 0);
                if (!Enum.IsDefined(typeof(ReadMode), value))
                    throw new InvalidOperationException("Failed to map read mode value '" + value + "'.");
                return (ReadMode)value;
            }
        }

        private sealed class ScannerButtonUiRequester
        {
            private const int ReceiverExported = 2;
            private const int TimeoutMillis = 3000;
            private readonly Context _context;
            private readonly string _requestId;
            private readonly ScannerButtonUiOptions _options;
            private ScannerButtonUiResult _dynamicResult;

            internal ScannerButtonUiRequester(
                Context context,
                string requestId,
                ScannerButtonUiOptions options)
            {
                _context = context.ApplicationContext ?? context;
                _requestId = string.IsNullOrEmpty(requestId) ? CreateRequestId() : requestId;
                _options = options;
            }

            internal static string CreateRequestId()
            {
                return Guid.NewGuid().ToString();
            }

            internal Task<ScannerButtonUiResult> FetchAsync(CancellationToken cancellationToken)
            {
                if (cancellationToken.IsCancellationRequested)
                    return Task.FromCanceled<ScannerButtonUiResult>(cancellationToken);
                if (_options != null && !_options.IsValid)
                    return Task.FromResult(TransportResult(ScannerButtonUiTransportStatus.InvalidSdkRequest));
                if (!IsScanEmulInstalled())
                    return Task.FromResult(TransportResult(ScannerButtonUiTransportStatus.ScanEmulNotInstalled));

                var taskSource = new TaskCompletionSource<ScannerButtonUiResult>();
                var handler = new Handler(Looper.MainLooper);
                var finished = false;
                var gate = new object();
                var dynamicRegistered = false;
                var cancellationRegistration = default(CancellationTokenRegistration);
                ActionBroadcastReceiver dynamicReceiver = null;
                ActionBroadcastReceiver finalReceiver = null;
                RunnableAction timeout = null;

                Action cleanup = () =>
                {
                    if (timeout != null)
                        handler.RemoveCallbacks(timeout);
                    if (dynamicRegistered && dynamicReceiver != null)
                    {
                        try
                        {
                            _context.UnregisterReceiver(dynamicReceiver);
                        }
                        catch (Exception)
                        {
                        }
                        dynamicRegistered = false;
                    }
                    cancellationRegistration.Dispose();
                };

                Action<ScannerButtonUiResult> complete = result =>
                {
                    lock (gate)
                    {
                        if (finished)
                            return;
                        finished = true;
                    }
                    cleanup();
                    taskSource.TrySetResult(result);
                };

                dynamicReceiver = new ActionBroadcastReceiver((context, intent) =>
                {
                    var result = Result(intent);
                    if (result != null)
                        _dynamicResult = result;
                });

                finalReceiver = new ActionBroadcastReceiver((context, intent) =>
                {
                    ScannerButtonUiResult result = null;
                    try
                    {
                        result = Result(finalReceiver.GetResultExtras(false));
                    }
                    catch (Exception)
                    {
                    }

                    complete(result ??
                             _dynamicResult ??
                             TransportResult(ScannerButtonUiTransportStatus.FeatureNotAvailable));
                });

                timeout = new RunnableAction(() =>
                {
                    complete(_dynamicResult ??
                             TransportResult(ScannerButtonUiTransportStatus.Timeout));
                });

                if (cancellationToken.CanBeCanceled)
                {
                    cancellationRegistration = cancellationToken.Register(() =>
                    {
                        lock (gate)
                        {
                            if (finished)
                                return;
                            finished = true;
                        }
                        cleanup();
                        taskSource.TrySetCanceled();
                    });
                }

                try
                {
                    lock (gate)
                    {
                        if (finished)
                            return taskSource.Task;

                        RegisterDynamicReceiver(dynamicReceiver);
                        dynamicRegistered = true;
                        handler.PostDelayed(timeout, TimeoutMillis);
                    }
                    _context.SendOrderedBroadcast(
                        RequestIntent(),
                        null,
                        finalReceiver,
                        handler,
                        Android.App.Result.Canceled,
                        null,
                        null);
                }
                catch (Exception)
                {
                    complete(TransportResult(ScannerButtonUiTransportStatus.SendFailed));
                }

                return taskSource.Task;
            }

            private Intent RequestIntent()
            {
                var action = _options == null
                    ? Constants.ScanEmul.GetScannerSetting
                    : Constants.ScanEmul.SetScannerSetting;
                var intent = new Intent(action)
                    .SetPackage(Constants.ScanEmul.PackageName)
                    .PutExtra(Constants.ScanEmul.TypeSetting, Constants.ScanEmul.TypeScannerButtonUi)
                    .PutExtra(Constants.ScanEmul.ExtraRequestId, _requestId);

                if (_options != null)
                {
                    if (_options.ImagePath != null)
                        intent.PutExtra(Constants.ScanEmul.ExtraScannerButtonImagePath, _options.ImagePath);
                    if (_options.OpacityPercent.HasValue)
                        intent.PutExtra(Constants.ScanEmul.ExtraScannerButtonOpacityPercent, _options.OpacityPercent.Value);
                    if (_options.Size.HasValue)
                        intent.PutExtra(Constants.ScanEmul.ExtraScannerButtonSize, SizeValue(_options.Size.Value));
                }

                return intent;
            }

            private void RegisterDynamicReceiver(BroadcastReceiver receiver)
            {
                var filter = new IntentFilter(Constants.ScanEmul.ResponseScannerSetting);
                if ((int)Build.VERSION.SdkInt >= 33)
                {
                    var method = _context.Class.GetMethod(
                        "registerReceiver",
                        Java.Lang.Class.FromType(typeof(BroadcastReceiver)),
                        Java.Lang.Class.FromType(typeof(IntentFilter)),
                        Java.Lang.Integer.Type);

                    method.Invoke(_context, receiver, filter, Java.Lang.Integer.ValueOf(ReceiverExported));
                    return;
                }

                _context.RegisterReceiver(receiver, filter);
            }

            private bool IsScanEmulInstalled()
            {
                try
                {
                    _context.PackageManager.GetPackageInfo(
                        Constants.ScanEmul.PackageName,
                        PackageInfoFlags.Activities);
                    return true;
                }
                catch (Exception)
                {
                    return false;
                }
            }

            private ScannerButtonUiResult Result(Intent intent)
            {
                if (intent == null || intent.Action != Constants.ScanEmul.ResponseScannerSetting)
                    return null;
                return Result(intent.Extras);
            }

            private ScannerButtonUiResult Result(Bundle extras)
            {
                if (extras == null)
                    return null;
                if (extras.GetString(Constants.ScanEmul.TypeSetting) != Constants.ScanEmul.TypeScannerButtonUi)
                    return null;
                if (extras.GetString(Constants.ScanEmul.ExtraRequestId) != _requestId)
                    return null;

                var rawStatus = extras.GetString(Constants.ScanEmul.ExtraStatus);
                var imagePath = extras.GetString(Constants.ScanEmul.ExtraScannerButtonImagePath);
                if (imagePath == null ||
                    !extras.ContainsKey(Constants.ScanEmul.ExtraScannerButtonOpacityPercent))
                    return null;

                var settings = new ScannerButtonUiSettings(
                    imagePath,
                    extras.GetInt(Constants.ScanEmul.ExtraScannerButtonOpacityPercent),
                    SizeByValue(extras.GetString(Constants.ScanEmul.ExtraScannerButtonSize)));

                return new ScannerButtonUiResult(
                    _requestId,
                    ScannerButtonUiTransportStatus.Ok,
                    extras.GetBoolean(Constants.ScanEmul.ExtraSuccess, false),
                    StatusByValue(rawStatus),
                    rawStatus,
                    extras.GetBoolean(Constants.ScanEmul.ExtraRuntimeApplied, false),
                    settings);
            }

            private ScannerButtonUiResult TransportResult(ScannerButtonUiTransportStatus status)
            {
                return new ScannerButtonUiResult(
                    _requestId,
                    status,
                    false,
                    ScannerButtonUiStatus.Unknown,
                    null,
                    false,
                    null);
            }

            private static string SizeValue(ScannerButtonUiSize size)
            {
                switch (size)
                {
                    case ScannerButtonUiSize.ExtraSmall: return "extra_small";
                    case ScannerButtonUiSize.Small: return "small";
                    case ScannerButtonUiSize.Medium: return "medium";
                    case ScannerButtonUiSize.Large: return "large";
                    case ScannerButtonUiSize.ExtraLarge: return "extra_large";
                    default: return "unknown";
                }
            }

            private static ScannerButtonUiSize SizeByValue(string value)
            {
                switch (value)
                {
                    case "extra_small": return ScannerButtonUiSize.ExtraSmall;
                    case "small": return ScannerButtonUiSize.Small;
                    case "medium": return ScannerButtonUiSize.Medium;
                    case "large": return ScannerButtonUiSize.Large;
                    case "extra_large": return ScannerButtonUiSize.ExtraLarge;
                    default: return ScannerButtonUiSize.Unknown;
                }
            }

            private static ScannerButtonUiStatus StatusByValue(string value)
            {
                switch (value)
                {
                    case "APPLIED": return ScannerButtonUiStatus.Applied;
                    case "SAVED_SERVICE_NOT_READY": return ScannerButtonUiStatus.SavedServiceNotReady;
                    case "SAVED_BUTTON_NOT_VISIBLE": return ScannerButtonUiStatus.SavedButtonNotVisible;
                    case "UNSUPPORTED_DEVICE": return ScannerButtonUiStatus.UnsupportedDevice;
                    case "INVALID_REQUEST": return ScannerButtonUiStatus.InvalidRequest;
                    case "INVALID_IMAGE": return ScannerButtonUiStatus.InvalidImage;
                    case "SAVE_FAILED": return ScannerButtonUiStatus.SaveFailed;
                    case "BUSY": return ScannerButtonUiStatus.Busy;
                    default: return ScannerButtonUiStatus.Unknown;
                }
            }
        }

        private sealed class RunnableAction : Java.Lang.Object, Java.Lang.IRunnable
        {
            private readonly Action _action;

            internal RunnableAction(Action action)
            {
                _action = action;
            }

            public void Run()
            {
                _action();
            }
        }
    }
}

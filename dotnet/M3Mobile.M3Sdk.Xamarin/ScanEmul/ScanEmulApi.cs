using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using Android.Content;
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
    }
}

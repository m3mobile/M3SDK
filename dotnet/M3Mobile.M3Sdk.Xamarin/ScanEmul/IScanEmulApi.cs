using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;

namespace M3Sdk.Xamarin.ScanEmul
{
    /// <summary>
    /// ScanEmul app API contract implemented through native Android broadcasts and Messenger.
    /// </summary>
    public interface IScanEmulApi : IDisposable
    {
        /// <summary>
        /// Occurs when a scan operation completes and returns decoded scanner data.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        event EventHandler<ScanResultEventArgs> ScanResultReceived;

        /// <summary>
        /// Occurs when a scanned GS1 barcode is parsed.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        event EventHandler<GS1ParsedEventArgs> GS1ParsedReceived;

        /// <summary>
        /// Occurs when a Digital Link barcode is parsed.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        event EventHandler<DigitalLinkParsedEventArgs> DigitalLinkParsedReceived;

        /// <summary>
        /// Starts a scan.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>2.13.0</c> or later.</remarks>
        void StartScan();

        /// <summary>
        /// Cancels the current scan.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>2.13.0</c> or later.</remarks>
        void StopScan();

        /// <summary>
        /// Asynchronously gets the device scanner type.
        /// </summary>
        /// <returns>A task that resolves to the scanner type.</returns>
        /// <remarks>Requires ScanEmul version <c>2.13.0</c> or later.</remarks>
        Task<string> GetScannerTypeAsync();

        /// <summary>
        /// Asynchronously gets the device scanner type.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the scanner type.</returns>
        /// <remarks>Requires ScanEmul version <c>2.13.0</c> or later.</remarks>
        Task<string> GetScannerTypeAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the device scanner type and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the scanner type or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires ScanEmul version <c>2.13.0</c> or later.</remarks>
        IM3Cancelable GetScannerType(M3RequestCallback<string> callback);

        /// <summary>
        /// Asynchronously gets the current scanner status.
        /// </summary>
        /// <returns>A task that resolves to the scanner status code: <c>1</c> failed to open, <c>2</c> failed to close, <c>4</c> opened successfully, or <c>8</c> closed successfully.</returns>
        /// <remarks>Requires ScanEmul version <c>2.13.0</c> or later.</remarks>
        Task<int> GetScannerStatusAsync();

        /// <summary>
        /// Asynchronously gets the current scanner status.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the scanner status code: <c>1</c> failed to open, <c>2</c> failed to close, <c>4</c> opened successfully, or <c>8</c> closed successfully.</returns>
        /// <remarks>Requires ScanEmul version <c>2.13.0</c> or later.</remarks>
        Task<int> GetScannerStatusAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the current scanner status and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the status code or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires ScanEmul version <c>2.13.0</c> or later.</remarks>
        IM3Cancelable GetScannerStatus(M3RequestCallback<int> callback);

        /// <summary>
        /// Registers a listener to receive scan results.
        /// </summary>
        /// <param name="listener">The listener that receives scan results.</param>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        void RegisterOnScanResultListener(IOnScanResultListener listener);

        /// <summary>
        /// Registers an action to receive scan results.
        /// </summary>
        /// <param name="listener">The action that receives scan results.</param>
        /// <returns>A disposable registration that unregisters the action when disposed.</returns>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        IDisposable RegisterOnScanResultListener(Action<ScanResult> listener);

        /// <summary>
        /// Unregisters a scan result listener.
        /// </summary>
        /// <param name="listener">The listener to remove.</param>
        void UnregisterOnScanResultListener(IOnScanResultListener listener);

        /// <summary>
        /// Registers a listener to receive parsed GS1 scan results.
        /// </summary>
        /// <param name="listener">The listener that receives parsed GS1 data.</param>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        void RegisterOnGS1ParsedListener(IOnGS1ParsedListener listener);

        /// <summary>
        /// Registers an action to receive parsed GS1 scan results.
        /// </summary>
        /// <param name="listener">The action that receives parsed GS1 data.</param>
        /// <returns>A disposable registration that unregisters the action when disposed.</returns>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        IDisposable RegisterOnGS1ParsedListener(Action<IList<GS1ParsedData>> listener);

        /// <summary>
        /// Unregisters a parsed GS1 listener.
        /// </summary>
        /// <param name="listener">The listener to remove.</param>
        void UnregisterOnGS1ParsedListener(IOnGS1ParsedListener listener);

        /// <summary>
        /// Registers a listener to receive parsed Digital Link scan results.
        /// </summary>
        /// <param name="listener">The listener that receives parsed Digital Link data.</param>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        void RegisterOnDigitalLinkParsedListener(IOnDigitalLinkParsedListener listener);

        /// <summary>
        /// Registers an action to receive parsed Digital Link scan results.
        /// </summary>
        /// <param name="listener">The action that receives parsed Digital Link data.</param>
        /// <returns>A disposable registration that unregisters the action when disposed.</returns>
        /// <remarks>Requires ScanEmul version <c>4.11.0</c> or later.</remarks>
        IDisposable RegisterOnDigitalLinkParsedListener(Action<IList<DigitalLinkParsedData>> listener);

        /// <summary>
        /// Unregisters a parsed Digital Link listener.
        /// </summary>
        /// <param name="listener">The listener to remove.</param>
        void UnregisterOnDigitalLinkParsedListener(IOnDigitalLinkParsedListener listener);

        /// <summary>
        /// Sets the scan feedback sound for the current scanner profile.
        /// </summary>
        /// <param name="sound">The scan feedback sound.</param>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void SetScanSound(ScanSound sound);

        /// <summary>
        /// Enables vibration feedback for scans in the current scanner profile.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void EnableScanVibration();

        /// <summary>
        /// Disables vibration feedback for scans in the current scanner profile.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void DisableScanVibration();

        /// <summary>
        /// Enables LED feedback for scans in the current scanner profile.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void EnableScanLed();

        /// <summary>
        /// Disables LED feedback for scans in the current scanner profile.
        /// </summary>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void DisableScanLed();

        /// <summary>
        /// Sets the scan LED feedback duration for the current scanner profile.
        /// </summary>
        /// <param name="timeMillis">The duration in milliseconds. Kotlin KDoc defines the supported range as <c>1</c> to <c>1000</c>.</param>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void SetScanLedTime(int timeMillis);

        /// <summary>
        /// Sets the scanner read mode for the current scanner profile.
        /// </summary>
        /// <param name="mode">The read mode to apply.</param>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void SetScannerReadMode(ReadMode mode);

        /// <summary>
        /// Sets the scan result output mode for the current scanner profile.
        /// </summary>
        /// <param name="mode">The output mode to apply.</param>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void SetScanResultOutputMode(OutputMode mode);

        /// <summary>
        /// Sets the end character appended to scan results in the current scanner profile.
        /// </summary>
        /// <param name="endCharacter">The end character to append.</param>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void SetScanResultEndCharacter(EndCharacter endCharacter);

        /// <summary>
        /// Sets the prefix prepended to scan results in the current scanner profile.
        /// </summary>
        /// <param name="prefix">The prefix string.</param>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void SetScanResultPrefix(string prefix);

        /// <summary>
        /// Sets the postfix appended to scan results in the current scanner profile.
        /// </summary>
        /// <param name="postfix">The postfix string.</param>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        void SetScanResultPostfix(string postfix);

        /// <summary>
        /// Asynchronously gets the scan result prefix for the current scanner profile.
        /// </summary>
        /// <returns>A task that resolves to the scan result prefix.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<string> GetScanResultPrefixAsync();

        /// <summary>
        /// Asynchronously gets the scan result prefix for the current scanner profile.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the scan result prefix.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<string> GetScanResultPrefixAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the scan result prefix and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the prefix or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        IM3Cancelable GetScanResultPrefix(M3RequestCallback<string> callback);

        /// <summary>
        /// Asynchronously gets the scan result postfix for the current scanner profile.
        /// </summary>
        /// <returns>A task that resolves to the scan result postfix.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<string> GetScanResultPostfixAsync();

        /// <summary>
        /// Asynchronously gets the scan result postfix for the current scanner profile.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the scan result postfix.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<string> GetScanResultPostfixAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the scan result postfix and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the postfix or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        IM3Cancelable GetScanResultPostfix(M3RequestCallback<string> callback);

        /// <summary>
        /// Asynchronously gets the scan result end character for the current scanner profile.
        /// </summary>
        /// <returns>A task that resolves to the configured end character.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<EndCharacter> GetScanResultEndCharacterAsync();

        /// <summary>
        /// Asynchronously gets the scan result end character for the current scanner profile.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the configured end character.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<EndCharacter> GetScanResultEndCharacterAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the scan result end character and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the configured end character or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        IM3Cancelable GetScanResultEndCharacter(M3RequestCallback<EndCharacter> callback);

        /// <summary>
        /// Asynchronously gets the scan result output mode for the current scanner profile.
        /// </summary>
        /// <returns>A task that resolves to the configured output mode.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<OutputMode> GetScanResultOutputModeAsync();

        /// <summary>
        /// Asynchronously gets the scan result output mode for the current scanner profile.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the configured output mode.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<OutputMode> GetScanResultOutputModeAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the scan result output mode and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the configured output mode or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        IM3Cancelable GetScanResultOutputMode(M3RequestCallback<OutputMode> callback);

        /// <summary>
        /// Asynchronously gets whether the current scanner profile is enabled.
        /// </summary>
        /// <returns>A task that resolves to <c>true</c> when the current scanner profile is enabled; otherwise, <c>false</c>.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<bool> IsScannerProfileEnabledAsync();

        /// <summary>
        /// Asynchronously gets whether the current scanner profile is enabled.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to <c>true</c> when the current scanner profile is enabled; otherwise, <c>false</c>.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<bool> IsScannerProfileEnabledAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests whether the current scanner profile is enabled and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the profile enabled state or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        IM3Cancelable IsScannerProfileEnabled(M3RequestCallback<bool> callback);

        /// <summary>
        /// Asynchronously gets the scanner read mode for the current scanner profile.
        /// </summary>
        /// <returns>A task that resolves to the configured read mode.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<ReadMode> GetScannerReadModeAsync();

        /// <summary>
        /// Asynchronously gets the scanner read mode for the current scanner profile.
        /// </summary>
        /// <param name="cancellationToken">A token that cancels the pending broadcast request.</param>
        /// <returns>A task that resolves to the configured read mode.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        Task<ReadMode> GetScannerReadModeAsync(CancellationToken cancellationToken);

        /// <summary>
        /// Requests the scanner read mode and returns the result through a main-thread callback.
        /// </summary>
        /// <param name="callback">The callback that receives either the configured read mode or an exception.</param>
        /// <returns>A cancellable request handle.</returns>
        /// <remarks>Requires ScanEmul version <c>2.11.0</c> or later.</remarks>
        IM3Cancelable GetScannerReadMode(M3RequestCallback<ReadMode> callback);
    }
}

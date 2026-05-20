using System;
using System.Collections.Generic;

namespace M3Sdk.Xamarin.ScanEmul
{
    /// <summary>
    /// Defines scan feedback sounds for the current scanner profile.
    /// </summary>
    public enum ScanSound
    {
        /// <summary>
        /// No scan feedback sound.
        /// </summary>
        None = 0,

        /// <summary>
        /// Beep scan feedback sound.
        /// </summary>
        Beep = 1,

        /// <summary>
        /// Ding-dong scan feedback sound.
        /// </summary>
        DingDong = 2
    }

    /// <summary>
    /// Defines scanner read modes.
    /// </summary>
    public enum ReadMode
    {
        /// <summary>
        /// Asynchronous read mode.
        /// </summary>
        Async = 0,

        /// <summary>
        /// Synchronous read mode.
        /// </summary>
        Sync = 1,

        /// <summary>
        /// Continuous read mode.
        /// </summary>
        Continue = 2,

        /// <summary>
        /// Multiple read mode.
        /// </summary>
        Multiple = 3,

        /// <summary>
        /// Presentation read mode.
        /// </summary>
        Presentation = 4,

        /// <summary>
        /// Aiming-and-release read mode.
        /// </summary>
        AimingAndRelease = 5
    }

    /// <summary>
    /// Defines output modes for scanned data.
    /// </summary>
    public enum OutputMode
    {
        /// <summary>
        /// Copy and paste output mode.
        /// </summary>
        CopyAndPaste = 0,

        /// <summary>
        /// Key emulation output mode.
        /// </summary>
        KeyEmulation = 1,

        /// <summary>
        /// Copy to clipboard output mode.
        /// </summary>
        CopyToClipboard = 2,

        /// <summary>
        /// Commit text output mode.
        /// </summary>
        CommitText = 3
    }

    /// <summary>
    /// Defines the end character appended to scanned data.
    /// </summary>
    public enum EndCharacter
    {
        /// <summary>
        /// Appends an Enter character.
        /// </summary>
        Enter = 0,

        /// <summary>
        /// Appends a Space character.
        /// </summary>
        Space = 1,

        /// <summary>
        /// Appends a Tab character.
        /// </summary>
        Tab = 2,

        /// <summary>
        /// Appends a keyboard Enter event.
        /// </summary>
        KeyboardEnter = 3,

        /// <summary>
        /// Appends a keyboard Space event.
        /// </summary>
        KeyboardSpace = 4,

        /// <summary>
        /// Appends a keyboard Tab event.
        /// </summary>
        KeyboardTab = 5,

        /// <summary>
        /// Appends no end character.
        /// </summary>
        None = 6
    }

    /// <summary>
    /// Decoded scanner result delivered by ScanEmul.
    /// </summary>
    public sealed class ScanResult
    {
        private readonly byte[] _rawData;

        /// <summary>
        /// Creates a decoded scanner result.
        /// </summary>
        /// <param name="barcode">The decoded barcode data.</param>
        /// <param name="type">The scanner code type.</param>
        /// <param name="rawData">The raw scanner data bytes.</param>
        /// <exception cref="ArgumentNullException"><paramref name="barcode" /> or <paramref name="type" /> is <c>null</c>.</exception>
        public ScanResult(string barcode, string type, byte[] rawData)
        {
            Barcode = barcode ?? throw new ArgumentNullException(nameof(barcode));
            Type = type ?? throw new ArgumentNullException(nameof(type));
            _rawData = rawData == null ? new byte[0] : (byte[])rawData.Clone();
        }

        /// <summary>
        /// Gets the decoded barcode data.
        /// </summary>
        public string Barcode { get; private set; }

        /// <summary>
        /// Gets the scanner code type.
        /// </summary>
        public string Type { get; private set; }

        /// <summary>
        /// Gets a copy of the raw scanner data bytes.
        /// </summary>
        public byte[] RawData
        {
            get { return (byte[])_rawData.Clone(); }
        }
    }

    /// <summary>
    /// Represents a parsed GS1 Application Identifier value.
    /// </summary>
    public sealed class GS1ParsedData
    {
        /// <summary>
        /// Creates a parsed GS1 Application Identifier value.
        /// </summary>
        /// <param name="ai">The Application Identifier.</param>
        /// <param name="data">The parsed data value.</param>
        /// <param name="description">The Application Identifier description.</param>
        /// <exception cref="ArgumentNullException">A parsed GS1 value is <c>null</c>.</exception>
        public GS1ParsedData(string ai, string data, string description)
        {
            Ai = ai ?? throw new ArgumentNullException(nameof(ai));
            Data = data ?? throw new ArgumentNullException(nameof(data));
            Description = description ?? throw new ArgumentNullException(nameof(description));
        }

        /// <summary>
        /// Gets the Application Identifier.
        /// </summary>
        public string Ai { get; private set; }

        /// <summary>
        /// Gets the parsed data value.
        /// </summary>
        public string Data { get; private set; }

        /// <summary>
        /// Gets the Application Identifier description.
        /// </summary>
        public string Description { get; private set; }
    }

    /// <summary>
    /// Represents a parsed Digital Link Application Identifier value.
    /// </summary>
    public sealed class DigitalLinkParsedData
    {
        /// <summary>
        /// Creates a parsed Digital Link Application Identifier value.
        /// </summary>
        /// <param name="ai">The Application Identifier.</param>
        /// <param name="data">The parsed data value.</param>
        /// <param name="description">The Application Identifier description.</param>
        /// <exception cref="ArgumentNullException">A parsed Digital Link value is <c>null</c>.</exception>
        public DigitalLinkParsedData(string ai, string data, string description)
        {
            Ai = ai ?? throw new ArgumentNullException(nameof(ai));
            Data = data ?? throw new ArgumentNullException(nameof(data));
            Description = description ?? throw new ArgumentNullException(nameof(description));
        }

        /// <summary>
        /// Gets the Application Identifier.
        /// </summary>
        public string Ai { get; private set; }

        /// <summary>
        /// Gets the parsed data value.
        /// </summary>
        public string Data { get; private set; }

        /// <summary>
        /// Gets the Application Identifier description.
        /// </summary>
        public string Description { get; private set; }
    }

    /// <summary>
    /// Event data for a decoded scan result.
    /// </summary>
    public sealed class ScanResultEventArgs : EventArgs
    {
        /// <summary>
        /// Creates event data for a decoded scan result.
        /// </summary>
        /// <param name="result">The decoded scan result.</param>
        /// <exception cref="ArgumentNullException"><paramref name="result" /> is <c>null</c>.</exception>
        public ScanResultEventArgs(ScanResult result)
        {
            Result = result ?? throw new ArgumentNullException(nameof(result));
        }

        /// <summary>
        /// Gets the decoded scan result.
        /// </summary>
        public ScanResult Result { get; private set; }
    }

    /// <summary>
    /// Event data for parsed GS1 scan results.
    /// </summary>
    public sealed class GS1ParsedEventArgs : EventArgs
    {
        /// <summary>
        /// Creates event data for parsed GS1 scan results.
        /// </summary>
        /// <param name="result">The parsed GS1 data values.</param>
        /// <exception cref="ArgumentNullException"><paramref name="result" /> is <c>null</c>.</exception>
        public GS1ParsedEventArgs(IList<GS1ParsedData> result)
        {
            Result = result ?? throw new ArgumentNullException(nameof(result));
        }

        /// <summary>
        /// Gets the parsed GS1 data values.
        /// </summary>
        public IList<GS1ParsedData> Result { get; private set; }
    }

    /// <summary>
    /// Event data for parsed Digital Link scan results.
    /// </summary>
    public sealed class DigitalLinkParsedEventArgs : EventArgs
    {
        /// <summary>
        /// Creates event data for parsed Digital Link scan results.
        /// </summary>
        /// <param name="result">The parsed Digital Link data values.</param>
        /// <exception cref="ArgumentNullException"><paramref name="result" /> is <c>null</c>.</exception>
        public DigitalLinkParsedEventArgs(IList<DigitalLinkParsedData> result)
        {
            Result = result ?? throw new ArgumentNullException(nameof(result));
        }

        /// <summary>
        /// Gets the parsed Digital Link data values.
        /// </summary>
        public IList<DigitalLinkParsedData> Result { get; private set; }
    }

    /// <summary>
    /// Callback interface invoked when a scan operation returns a decoded result.
    /// </summary>
    public interface IOnScanResultListener
    {
        /// <summary>
        /// Called when a scan operation successfully completes or produces a result.
        /// </summary>
        /// <param name="result">The decoded scan result.</param>
        void OnScanResult(ScanResult result);
    }

    /// <summary>
    /// Callback interface invoked when a GS1 barcode scan result is received.
    /// </summary>
    public interface IOnGS1ParsedListener
    {
        /// <summary>
        /// Called when a GS1 barcode has been successfully parsed.
        /// </summary>
        /// <param name="result">The parsed GS1 data values.</param>
        void OnGS1Parsed(IList<GS1ParsedData> result);
    }

    /// <summary>
    /// Callback interface invoked when a Digital Link barcode scan result is received.
    /// </summary>
    public interface IOnDigitalLinkParsedListener
    {
        /// <summary>
        /// Called when a Digital Link barcode has been successfully parsed.
        /// </summary>
        /// <param name="result">The parsed Digital Link data values.</param>
        void OnDigitalLinkParsed(IList<DigitalLinkParsedData> result);
    }
}

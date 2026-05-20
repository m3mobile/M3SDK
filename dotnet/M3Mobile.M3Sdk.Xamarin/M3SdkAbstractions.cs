using System;
using System.Threading;

namespace M3Sdk.Xamarin
{
    /// <summary>
    /// Callback shape used by async requester helpers. On success, <paramref name="error" /> is null.
    /// On failure, <paramref name="result" /> is default and <paramref name="error" /> contains the exception.
    /// </summary>
    /// <typeparam name="T">The successful result type.</typeparam>
    /// <param name="result">The successful result, or the default value of <typeparamref name="T" /> when an error occurred.</param>
    /// <param name="error">The exception raised by the request, or <c>null</c> when the request succeeded.</param>
    public delegate void M3RequestCallback<T>(T result, Exception error);

    /// <summary>
    /// Represents a cancellable callback request.
    /// </summary>
    public interface IM3Cancelable : IDisposable
    {
        /// <summary>
        /// Gets whether cancellation has been requested.
        /// </summary>
        bool IsCancellationRequested { get; }

        /// <summary>
        /// Cancels the pending request. Callback requests do not invoke the callback after cancellation.
        /// </summary>
        void Cancel();
    }

    /// <summary>
    /// Exception thrown when strict mode requires an app version that is missing or too old.
    /// </summary>
    public sealed class UnsatisfiedVersionException : Exception
    {
        /// <summary>
        /// Creates an exception with a strict-mode version guard message.
        /// </summary>
        /// <param name="message">The exception message.</param>
        public UnsatisfiedVersionException(string message)
            : base(message)
        {
        }
    }

    /// <summary>
    /// Exception thrown when strict mode rejects an API on the current M3 Mobile model.
    /// </summary>
    public sealed class UnsupportedDeviceModelException : Exception
    {
        /// <summary>
        /// Creates an exception with a strict-mode model guard message.
        /// </summary>
        /// <param name="message">The exception message.</param>
        public UnsupportedDeviceModelException(string message)
            : base(message)
        {
        }
    }

    internal sealed class M3Cancelable : IM3Cancelable
    {
        private readonly CancellationTokenSource _source = new CancellationTokenSource();
        private bool _disposed;

        internal CancellationToken Token
        {
            get { return _source.Token; }
        }

        /// <summary>
        /// Gets whether cancellation has been requested for the callback operation.
        /// </summary>
        public bool IsCancellationRequested
        {
            get { return _source.IsCancellationRequested; }
        }

        /// <summary>
        /// Requests cancellation of the callback operation.
        /// </summary>
        public void Cancel()
        {
            if (!_source.IsCancellationRequested)
                _source.Cancel();
        }

        /// <summary>
        /// Cancels the operation and releases the cancellation token source.
        /// </summary>
        public void Dispose()
        {
            if (_disposed)
                return;

            _disposed = true;
            Cancel();
            _source.Dispose();
        }
    }
}

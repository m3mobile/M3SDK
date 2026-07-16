using System.Globalization;
using System.Threading;

namespace M3SdkPublishedSample;

internal sealed class ExecutionTrace
{
    private int _attempt;

    internal string Message(string operation, string body)
    {
        var attempt = Interlocked.Increment(ref _attempt);
        return
            "operation=" + operation +
            "\nattempt=" + attempt.ToString(CultureInfo.InvariantCulture) +
            "\nat=" + DateTimeOffset.Now.ToString("HH:mm:ss.fff", CultureInfo.InvariantCulture) +
            "\n" + body;
    }
}

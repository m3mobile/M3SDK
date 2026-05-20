using Android.Content;
using Android.OS;

namespace M3Sdk.Xamarin.Internal
{
    internal abstract class BroadcastRequester
    {
        protected BroadcastRequester(Context context)
        {
            Context = context;
        }

        protected Context Context { get; private set; }

        protected abstract string RequestAction { get; }

        protected virtual string TypeKey
        {
            get { return string.Empty; }
        }

        protected virtual string TypeValue
        {
            get { return string.Empty; }
        }

        protected virtual Bundle CreateExtras()
        {
            return null;
        }

        /// <summary>
        /// Sends the configured Android broadcast request.
        /// </summary>
        internal virtual void Request()
        {
            RunBroadcast();
        }

        protected void RunBroadcast()
        {
            var intent = new Intent(RequestAction);

            var extras = CreateExtras();

            if (extras != null)
                intent.PutExtras(extras);

            if (!string.IsNullOrEmpty(TypeKey))
                intent.PutExtra(TypeKey, TypeValue);

            Context.SendBroadcast(intent);
        }
    }
}

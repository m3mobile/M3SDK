using Android.Content;

namespace M3Sdk.Xamarin.Internal
{
    internal abstract class FinishRequiredBroadcastRequester : BroadcastRequester
    {
        protected FinishRequiredBroadcastRequester(Context context)
            : base(context)
        {
        }

        /// <summary>
        /// Sends the configured request broadcast and then the StartUp finish notification broadcast.
        /// </summary>
        internal override void Request()
        {
            base.Request();

            var finishIntent = new Intent(Constants.StartUp.RequestConfigFinish);
            Context.SendBroadcast(finishIntent);
        }
    }
}

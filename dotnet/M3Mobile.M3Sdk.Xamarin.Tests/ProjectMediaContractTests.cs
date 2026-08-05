using System.Linq;
using M3Sdk.Xamarin.Startup;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class ProjectMediaContractTests
    {
        [Fact]
        public void StatusesMatchStartUpContract()
        {
            Assert.Equal(
                new[] { 0, 1, 2, 3, 4, 5, 6 },
                System.Enum.GetValues<ProjectMediaStatus>().Select(status => (int)status));
        }

        [Theory]
        [InlineData(0, ProjectMediaStatus.Success, true)]
        [InlineData(1, ProjectMediaStatus.UnsupportedDevice, false)]
        [InlineData(2, ProjectMediaStatus.TargetNotInstalled, false)]
        [InlineData(3, ProjectMediaStatus.InvalidTarget, false)]
        [InlineData(4, ProjectMediaStatus.PermissionDenied, false)]
        [InlineData(5, ProjectMediaStatus.AppOpUnavailable, false)]
        [InlineData(6, ProjectMediaStatus.ApplyFailed, false)]
        public void ResultPreservesEveryKnownStatus(
            int code,
            ProjectMediaStatus expectedStatus,
            bool expectedSuccess)
        {
            var result = ProjectMediaContract.Result(code, "detail");

            Assert.Equal(expectedStatus, result.Status);
            Assert.Equal(expectedSuccess, result.IsSuccess);
            Assert.Equal("detail", result.ErrorMessage);
        }

        [Fact]
        public void UnknownCodeBecomesApplyFailedWithDiagnosticMessage()
        {
            var result = ProjectMediaContract.Result(99, null);

            Assert.Equal(ProjectMediaStatus.ApplyFailed, result.Status);
            Assert.False(result.IsSuccess);
            Assert.Equal("Unknown result code: 99", result.ErrorMessage);
        }
    }
}

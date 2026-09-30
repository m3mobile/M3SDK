using System;
using System.IO;
using System.Linq;
using System.Xml.Linq;

namespace M3Sdk.Xamarin.Tests
{
    public sealed class QueriesManifestTargetsTests
    {
        [Fact]
        public void AddsQueriesManifestForLegacyAndModernAndroidConsumers()
        {
            var target = Document(
                    "dotnet/M3Mobile.M3Sdk.Xamarin/buildTransitive/M3Mobile.M3Sdk.Xamarin.targets")
                .Descendants(Namespace + "PropertyGroup")
                .Single(group =>
                    group.Elements(Namespace + "_M3MobileM3SdkXamarinAddQueriesManifest").Any());
            var condition = target.Attribute("Condition").Value;

            Assert.Contains("'$(TargetFrameworkIdentifier)' == 'MonoAndroid'", condition);
            Assert.Contains("'$(TargetPlatformIdentifier)' == 'android'", condition);
            Assert.DoesNotContain("TargetFrameworkVersion", condition);
        }

        [Fact]
        public void MergesQueriesManifestWithEverySdkTargetPackage()
        {
            var targets = Document(
                "dotnet/M3Mobile.M3Sdk.Xamarin/buildTransitive/M3Mobile.M3Sdk.Xamarin.targets");
            var merge = targets.Descendants(Namespace + "ExtractedManifestDocuments").Single();
            var packages = Document(
                    "dotnet/M3Mobile.M3Sdk.Xamarin/manifest/AndroidManifest.xml")
                .Descendants("package")
                .Select(package => package.Attribute(Android + "name").Value)
                .ToArray();

            Assert.Contains(
                "'$(_M3MobileM3SdkXamarinAddQueriesManifest)' == 'true'",
                merge.Parent.Attribute("Condition").Value);
            Assert.Equal(
                "$(_M3MobileM3SdkXamarinQueriesManifest)",
                merge.Attribute("Include").Value);
            Assert.Equal(
                new[]
                {
                    "com.m3.startup",
                    "net.m3mobile.app.scanemul",
                    "com.m3.keytoolsl20",
                    "net.m3.keytool",
                    "com.m3.appcenter",
                },
                packages);
        }

        [Fact]
        public void BuildAndBuildTransitiveImportsRemainDeduplicated()
        {
            var buildImport = Document(
                    "dotnet/M3Mobile.M3Sdk.Xamarin/build/M3Mobile.M3Sdk.Xamarin.targets")
                .Descendants(Namespace + "Import")
                .Single();
            var transitiveProperties = Document(
                    "dotnet/M3Mobile.M3Sdk.Xamarin/buildTransitive/M3Mobile.M3Sdk.Xamarin.targets")
                .Descendants(Namespace + "PropertyGroup")
                .Single(group =>
                    group.Elements(Namespace + "M3MobileM3SdkXamarinTargetsImported").Any());

            Assert.Contains(
                "'$(M3MobileM3SdkXamarinTargetsImported)' != 'true'",
                buildImport.Attribute("Condition").Value);
            Assert.Equal(
                "true",
                transitiveProperties
                    .Element(Namespace + "M3MobileM3SdkXamarinTargetsImported")
                    .Value);
        }

        private static XDocument Document(string relativePath)
        {
            var repository = new DirectoryInfo(AppContext.BaseDirectory);
            while (repository != null &&
                   !Directory.Exists(Path.Combine(repository.FullName, ".git")))
            {
                repository = repository.Parent;
            }

            if (repository == null)
            {
                throw new DirectoryNotFoundException("M3SDK repository root was not found.");
            }

            return XDocument.Load(Path.Combine(repository.FullName, relativePath));
        }

        private static readonly XNamespace Namespace =
            "http://schemas.microsoft.com/developer/msbuild/2003";

        private static readonly XNamespace Android =
            "http://schemas.android.com/apk/res/android";
    }
}

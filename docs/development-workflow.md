# M3SDK 개발·검증·배포 절차

## 작업 순서와 통과 기준

1. **앱 구현·코드 리뷰:** 대응 앱의 UI, Broadcast, 저장·복원 구현을 먼저 확인한다. 변경 경로, 지원 모델, 최소 앱 버전과 오류 동작을 리뷰한다.
2. **앱 검증:** 대상 기기에서 직접 설정·Broadcast·연속 변경·재시작·재부팅을 확인한다. 관련 없는 기능이 유지되는지도 확인한다.
3. **회귀 판단:** 모델/실행 경로별로 검사한다. 생략 시 변경 파일, 해당 모델에 도달하지 않는 호출 경로, 이를 뒷받침하는 테스트, 판단자를 기록한다. 모델 조건 하나만으로 생략하지 않는다. 공통 Receiver/설정/복원 경로를 공유하는 모델도 영향 검토에 포함한다.
4. **SDK 구현·리뷰:** Kotlin·C# API, 모델·최소 버전 검사, 양쪽 패키지 소비 샘플과 국·영문 매뉴얼을 갱신한다. 새 기능을 샘플에서 실제로 호출할 수 있어야 한다.
5. **배포 전 자동 검증:** `Verify SDK Candidate`를 배포할 버전으로 수동 실행한다. Kotlin 전체 JVM 테스트·API 호환성·Release 빌드·격리 Maven 저장소 게시, C# 계약 테스트·클래식 Xamarin DLL/NuGet 생성, 양쪽 **패키지 의존성** 샘플 Release 빌드가 모두 통과해야 한다. PDF도 후보 파일에 포함한다. 실패한 도구 설치나 샘플 빌드를 성공으로 간주하지 않는다.
6. **배포 전 실기 검증:** 위 실행이 생성한 샘플 APK와 대응 앱으로 실제 기능을 확인한다. SDK 요청 전송 로그만으로 통과시키지 않는다. 테스트한 후보 manifest의 SHA-256을 기록한다.
7. **배포:** 증거를 제출해 `Deployment`를 먼저 dry-run으로 확인한다. 이후 같은 후보 실행을 실제 배포한다. NuGet은 검증한 nupkg를 다시 빌드하지 않고 그대로 게시한다. 태그는 후보 코드에 문서 버전 갱신만 반영한 커밋이다. JitPack은 그 태그를 빌드하므로 공개 경로 검증을 반드시 별도로 수행한다.
8. **배포 후 자동 검증:** 배포 workflow가 `Verify Published Samples`를 동기 호출한다. 깨끗한 hosted runner에서 로컬 Maven 없이 JitPack 버전과 NuGet Gallery 버전을 받아 양쪽 샘플을 빌드한다. 실패하면 배포 workflow도 실패한다. 게시된 패키지가 자동 취소되는 것은 아니다.
9. **배포 후 실기·완료:** 공개 패키지로 빌드한 양쪽 샘플 APK로 실제 동작을 확인하고 변경한 단말 설정을 복원한다. `Complete SDK Release`에 배포 실행 ID와 최종 증거를 제출한다. 자동 검증과 수동 증거가 모두 통과해야 완료다. Jira 상태는 자동으로 바꾸지 않는다.

## 증거 작성과 실행

`docs/release-evidence-template.json`을 작업 폴더로 복사한다. 실제 수행한 항목만 `passed`로 바꾸고 판단자·로그/영상/리뷰 링크를 넣는다. 기본 템플릿은 전부 pending이므로 배포할 수 없다.

- `source_sha`: 후보 실행의 40자리 커밋 SHA.
- `candidate_run_id`: 성공한 **수동 Verify SDK Candidate 실행** ID. PR의 합성 머지 커밋 빌드는 정식 배포 후보로 쓸 수 없다.
- `candidate_manifests`: `candidate-android.json`, `candidate-dotnet.json` 각각의 SHA-256. 두 manifest는 같은 코드·버전·실행을 가리켜야 한다.
- `app_verification`: 대응 앱의 검증 결과 링크. `devices`: 모델·OS·앱 버전.
- `pre_release`: UI·Broadcast·설정 저장·재시작/재부팅·Kotlin 패키지·C# 패키지 실기 결과.
- `regression`: 영향 검토에서 정한 모델을 모두 나열한다. `skipped`에는 `reason`, `affected_paths`, `test_coverage`와 판단자·근거 링크가 필수다. 프로그램은 근거 존재만 검사하므로 근거 타당성은 리뷰에서 확인해야 한다.
- `deployment_run_id`와 `post_release`: 실제 배포 실행 및 공개 패키지로 빌드한 샘플의 실기 결과. 배포 전 결과를 복사하지 않는다.

```powershell
# GitHub에 workflow를 반영한 뒤 실행. 이 명령들은 배포/실기 검증을 자동 수행하지 않음.
gh workflow run --repo m3mobile/M3SDK verify-candidate.yml --ref develop -f version=VERSION
# 완료된 후보의 artifacts를 내려받아 테스트/증거 작성 후:
gh workflow run --repo m3mobile/M3SDK deployment.yml --ref develop -f tag_name=VERSION -f candidate_run_id=RUN_ID -f evidence_json=@evidence.json -F dry_run=true
# 실제 배포는 같은 입력으로 dry_run=false. 성공 후 공개 샘플의 실기 검증을 마친 뒤:
gh workflow run --repo m3mobile/M3SDK complete-release.yml --ref develop -f deployment_run_id=DEPLOY_RUN_ID -f evidence_json=@evidence.json
```

배포 전 로컬 Kotlin 패키지 소비 확인:

```powershell
.\gradlew.bat --no-daemon testDebugUnitTest apiCheck assembleRelease publishToMavenLocal "-Pm3sdk.publish.version=VERSION" "-Dmaven.repo.local=ABSOLUTE_MAVEN_DIRECTORY"
.\gradlew.bat --no-daemon -p samples/android-compose-jitpack :app:testDebugUnitTest :app:assembleRelease "-Pm3SdkVersion=VERSION" "-Pm3SdkRepository=ABSOLUTE_MAVEN_DIRECTORY"
# VS MSBuild/Xamarin.Android, NuGet CLI, .NET 9/10, Android workload 필요.
pwsh -File tools/workflow/build-nuget-candidate.ps1 -Version VERSION
```

Kotlin 후보 모드는 지정한 파일 저장소에서 `sdk` 모듈을 소비한다. 공개 모드는 `com.github.m3mobile:M3SDK:VERSION`을 JitPack에서 소비한다. 저장소의 소스 모듈을 직접 참조하지 않는다. C# 후보는 동일한 클래식 Xamarin nupkg를 .NET Android 샘플에서 PackageReference로 소비한다. 샘플 APK의 debug 서명은 검증용이며 제품 앱의 서명 정책이 아니다.

## 최초 원격 설정

- workflow/스크립트를 저장소에 반영한 뒤 실행 가능하다. 로컬 파일 추가만으로 GitHub Actions가 활성화되지는 않는다.
- 브랜치 보호/ruleset에 리뷰 승인과 `candidate-packages / android`, `candidate-packages / dotnet`, API/C# 검사를 필수로 설정한다. 실제 check 이름은 첫 실행 결과에서 확인한다.
- `sdk-release` environment의 승인자·배포 허용 브랜치를 설정하고 `NUGET_API_KEY`를 등록한다. environment 이름만 YAML에 써도 승인 보호가 자동 설정되는 것은 아니다.
- Windows runner에는 클래식 Xamarin.Android MSBuild targets, Android API 24, .NET 9/10과 Android workload가 필요하다. 없으면 명확히 실패한다. 자체 runner를 쓸 때는 PR 코드와 배포 자격 증명을 같은 신뢰 경계에 두지 않는다.
- Actions artifact 기본 보존기간 안에 후보를 검증·배포·완료한다. 장기 보관이 필요하면 증거와 후보 파일을 별도로 보존한다.
- 태그/NuGet 일부만 게시된 실패는 로그로 게시 범위를 확인한다. 태그 재지정·기존 버전 덮어쓰기는 하지 않는다. 수정이 필요하면 새 버전 후보부터 다시 검증한다.
- 게시 후 일시적인 다운로드 장애만 발생한 경우에는 원래 배포 실행의 실패한 검증 job만 재실행할 수 있다. 태그/패키지를 이미 게시한 publish job 전체를 다시 실행하지 않는다. 완료 workflow는 최종 성공한 원래 배포 실행 ID를 사용한다.
- GitHub Packages alpha는 개발용이며 정식 배포 완료의 근거가 아니다. 정식 JitPack/NuGet 배포는 위 절차를 따른다.

`impact_review.regression_models`에 지원 모델 전체의 영향 검토에서 정한 회귀 대상을 기록한다. 결과 목록은 이 계획과 정확히 일치해야 한다. passed인 회귀 모델은 devices에도 OS·앱 버전이 있어야 한다. 생략 근거가 불충분한 모델은 skipped로 승인하지 않는다.

GitHub Release는 배포 단계에서 draft로 생성한다. 공개 패키지 검증 및 배포 후 실기 증거를 통과한 Complete workflow만 draft를 정식 공개/latest로 전환한다. SDK의 태그·NuGet은 이보다 앞서 외부에 존재할 수 있으므로 draft가 패키지 비공개를 뜻하지는 않는다.

## 매뉴얼과 릴리즈 노트 완료 조건

배포 전 manuals의 앱 매뉴얼과 해당 SDK 국·영문 4종/PDF 화면 검수, release_notes.draft를 확인한다. 실제 기능과 무관한 항목은 impact_review.not_applicable_checks 및 항목별 판단자·근거·사유로 명시적으로 제외한다. SDK 패키지 소비 테스트는 제외할 수 없다. Excel은 수동 입력 경로를 지원하며 최종 완료 전 release_notes.excel의 파일·시트·범위·동기화/담당자 확인 근거가 필요하다. 자세한 절차는 release-notes-manual.md를 따른다.

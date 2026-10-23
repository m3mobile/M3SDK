# M3SDK 개발·검증·배포 절차

## 작업 순서와 통과 기준

1. **앱 구현·코드 리뷰:** 대응 앱의 UI, Broadcast, 저장·복원 구현을 먼저 확인한다. 변경 경로, 지원 모델, 최소 앱 버전과 오류 동작을 리뷰한다.
2. **앱 검증:** 대상 기기에서 직접 설정·Broadcast·연속 변경·재시작·재부팅을 확인한다. 관련 없는 기능이 유지되는지도 확인한다.
3. **회귀 판단:** 모델/실행 경로별로 검사한다. 생략 시 변경 파일, 해당 모델에 도달하지 않는 호출 경로, 이를 뒷받침하는 테스트, 판단자를 기록한다. 모델 조건 하나만으로 생략하지 않는다. 공통 Receiver/설정/복원 경로를 공유하는 모델도 영향 검토에 포함한다.
4. **SDK 구현·리뷰:** Kotlin·C# API, 모델·최소 버전 검사, 양쪽 패키지 소비 샘플과 국·영문 매뉴얼을 갱신한다. 새 기능을 샘플에서 실제로 호출할 수 있어야 한다.
5. **배포 전 자동 검증:** `Verify SDK Candidate`를 배포할 버전으로 수동 실행한다. Kotlin 전체 JVM 테스트·API 호환성·Release 빌드·격리 Maven 저장소 게시, C# 계약 테스트·클래식 Xamarin DLL/NuGet 생성, 양쪽 **패키지 의존성** 샘플 Release 빌드가 모두 통과해야 한다. PDF도 후보 파일에 포함한다. 실패한 도구 설치나 샘플 빌드를 성공으로 간주하지 않는다.
6. **배포 전 실기 검증:** 위 실행이 생성한 샘플 APK와 대응 앱으로 실제 기능을 확인한다. SDK 요청 전송 로그만으로 통과시키지 않는다. 테스트한 후보 manifest의 SHA-256을 기록한다.
7. **배포:** CI 결과를 담당자가 확인하고 `Deployment`를 수동 시작한다. 먼저 dry-run으로 후보 출처·버전·SHA·파일 해시를 자동 확인하고, 실제 배포가 허용되면 같은 후보 실행을 게시한다. 사람이 작성하는 증거 JSON은 받지 않는다. NuGet은 검증한 nupkg를 다시 빌드하지 않고 그대로 게시한다. 태그는 후보 코드에 문서 버전 갱신만 반영한 커밋이다. JitPack은 그 태그를 빌드하므로 공개 경로 검증을 반드시 별도로 수행한다.
8. **배포 후 자동 검증:** 배포 workflow가 `Verify Published Samples`를 동기 호출한다. 깨끗한 hosted runner에서 로컬 Maven 없이 JitPack 버전과 NuGet Gallery 버전을 받아 양쪽 샘플을 빌드한다. 실패하면 배포 workflow도 실패한다. 게시된 패키지가 자동 취소되는 것은 아니다.
9. **공개·작업 기록:** `Deployment`의 두 공개 샘플 검사가 성공하면 같은 run의 `finalize`가 자동으로 Release를 공개한다. 완료 버튼은 따로 누르지 않는다. 현재 원격 `sdk-release` reviewer 설정은 별도 적용 전까지 게시 job에서 승인 대기를 만들 수 있다. 실기·설정 복원·매뉴얼 화면 검수·릴리즈 기록은 허용 범위에서 별도로 수행하며 미수행은 pending으로 남긴다. Actions의 패키지 검증 결과를 이 작업들의 수행 기록으로 대체하지 않는다. Jira 상태는 자동으로 바꾸지 않는다.

## 자동 기록과 실행

SDK에서는 수동 `evidence_json` 입력이나 증거 템플릿 제출을 사용하지 않는다. 후보 manifest와 다음 기록을 Actions가 생성·검사한다.

- 후보는 같은 저장소의 `develop`에서 수동 실행한 성공한 **Verify SDK Candidate** run이어야 한다. PR 합성 커밋, 다른 브랜치, fork 후보는 허용하지 않는다.
- `candidate-verification.json`: 저장소·workflow·실행 ID/시도·브랜치·SHA, 버전, 두 manifest의 SHA-256과 모든 파일 해시. manifest의 SHA/버전/run과 실제 파일을 대조하고 누락·추가·중복 파일 및 경로 이탈을 거부한다.
- 배포 직전에는 후보를 다시 내려받아 검사하고 gate의 자동 기록과 정확히 비교한다. 패키지와 manifest를 함께 다시 작성해도 gate 이후 바뀌었다면 실패한다. 후보 run을 재실행해 시도가 바뀌면 새 gate 검증부터 진행한다.
- `release-plan.json`과 `release.bundle`: 쓰기 없는 prepare가 문서만 바꾼 정확한 커밋·후보 기록·게시 파일 해시를 묶는다. publish는 bundle의 커밋을 그대로 사용하며 새로 빌드하거나 문서 커밋을 다시 만들지 않는다.
- `release-record.json`: 실제 게시 job이 새로 만든 Release ID와 배포 context·계획 digest를 묶는다. dry-run/rehearsal에는 생성하지 않는다.
- finalize는 실제 needs 결과, 수동 시작·develop·publish 모드, 현재 run ID/SHA, 새 Release ID·tag·source SHA·소유 run 표식과 후보 시도/파일을 재검사한다. 현재 배포 run은 아직 in_progress여도 정상이며 completed/success로 위조하지 않는다. 기존 draft는 자동으로 인수하지 않는다.
- prepare/gate artifact는 upload가 반환한 정확한 artifact ID로 전달한다. 재실행 시 새 이름에 run_attempt를 붙이며 무조건 overwrite하지 않는다. 기존 publish가 만든 release-record는 소비/완료 job만 재실행할 때 재사용한다. 시험 보고서와 공개 샘플 APK도 run_attempt별 이름을 사용한다. 이미 업로드한 후보 패키지를 다시 만들려면 새 후보 run을 시작한다.
- 자동 기록에는 실기·리뷰·Excel 확인을 수행했다는 상태를 만들지 않는다. 앱 템플릿의 기존 수동 증거 양식은 `templates/android-app`에 분리돼 있으며 앱 정책은 이번 SDK 변경으로 바꾸지 않는다.

```powershell
# GitHub에 workflow를 반영한 뒤 실행. 이 명령들은 배포/실기 검증을 자동 수행하지 않음.
gh workflow run --repo m3mobile/M3SDK verify-candidate.yml --ref develop -f version=VERSION
# 후보 CI 결과를 담당자가 확인한 뒤 게시 없는 사전 검사:
gh workflow run --repo m3mobile/M3SDK deployment.yml --ref develop -f tag_name=VERSION -f candidate_run_id=RUN_ID -F dry_run=true
# 실제 배포는 별도 허용 후 같은 후보에 dry_run=false. 공개 검사와 최종 공개는 같은 run에서 자동 연결된다.
# 게시 없는 리허설: 같은 브랜치·정확한 SHA에서 만든 수동 후보만 사용한다.
gh workflow run --repo m3mobile/M3SDK deployment.yml --ref BRANCH -f tag_name=VERSION -f candidate_run_id=RUN_ID -F rehearsal=true -F dry_run=true -f rehearsal_case=normal
```

배포 전 로컬 Kotlin 패키지 소비 확인:

```powershell
.\gradlew.bat --no-daemon testDebugUnitTest apiCheck assembleRelease publishToMavenLocal "-Pm3sdk.publish.version=VERSION" "-Dmaven.repo.local=ABSOLUTE_MAVEN_DIRECTORY"
.\gradlew.bat --no-daemon -p samples/android-compose-jitpack :app:testDebugUnitTest :app:assembleRelease "-Pm3SdkVersion=VERSION" "-Pm3SdkRepository=ABSOLUTE_MAVEN_DIRECTORY"
# VS MSBuild/Xamarin.Android, NuGet CLI, .NET 9/10, Android workload 필요.
pwsh -File tools/workflow/build-nuget-candidate.ps1 -Version VERSION
```

Kotlin 후보 모드는 지정한 파일 저장소에서 `sdk` 모듈을 소비한다. 공개 모드는 `com.github.m3mobile:M3SDK:VERSION`을 JitPack에서 소비한다. 저장소의 소스 모듈을 직접 참조하지 않는다. C#은 정확한 NuGet 버전 범위로 지정해 다른 버전 대체 복원을 거부한다. 공개 샘플은 재시도 가능한 격리 restore가 성공한 뒤 추가 restore 없이 SDK 의존성 설치와 빌드를 진행한다. C# 후보는 동일한 클래식 Xamarin nupkg를 .NET Android 샘플에서 PackageReference로 소비한다. 샘플 APK의 debug 서명은 검증용이며 제품 앱의 서명 정책이 아니다.

## 최초 원격 설정

- PR의 API 호환성 검사는 후보 Android job에서, C# 계약 테스트와 클래식 Xamarin 소스 포함 검사는 후보 .NET job에서 실행한다. 별도 중복 검사 job은 두지 않는다. Alpha도 같은 후보 검증이 성공한 뒤 진행하며 API 검사를 반복하지 않는다.
- M3SDK-15 원격 적용 시 새 CI 실행 결과를 먼저 확인한 뒤 기존 필수 check `check-binary-compatibility`, `check-dotnet-contracts`를 후보 검사로 통합한다. 로컬 workflow 변경만으로 원격 필수 check 설정이 바뀌지는 않는다.
- workflow/스크립트를 저장소에 반영한 뒤 실행 가능하다. 로컬 파일 추가만으로 GitHub Actions가 활성화되지는 않는다.
- 원격 정책 변경 단계에서 동료 Approve 필수는 제거하고 PR·대화 해결과 `candidate-packages / android`, `candidate-packages / dotnet`, `actionlint`를 필수로 설정한다. 실제 check 이름은 첫 실행 결과에서 확인한다.
- `sdk-release` environment의 승인자·배포 허용 브랜치를 설정한다. NuGet 게시 job이 이 environment를 사용한다. 같은 run의 최종 공개는 게시 성공에 의존하며 추가 environment 승인 job을 만들지 않는다. reviewer 제거는 원격 정책 변경 단계에서 별도로 검증한다. 그리고 `NUGET_API_KEY`를 등록한다. environment 이름만 YAML에 써도 승인 보호가 자동 설정되는 것은 아니다.
- Windows runner에는 클래식 Xamarin.Android MSBuild targets, Android API 24, .NET 9/10과 Android workload가 필요하다. 없으면 명확히 실패한다. 자체 runner를 쓸 때는 PR 코드와 배포 자격 증명을 같은 신뢰 경계에 두지 않는다.
- Actions artifact 기본 보존기간 안에 후보를 검증·배포·완료한다. 장기 보관이 필요하면 증거와 후보 파일을 별도로 보존한다.
- 태그/NuGet 일부만 게시된 실패는 로그로 게시 범위를 확인한다. 태그 재지정·기존 버전 덮어쓰기는 하지 않는다. 수정이 필요하면 새 버전 후보부터 다시 검증한다.
- 게시 후 일시적인 다운로드 장애만 발생한 경우에는 원래 배포 실행의 실패한 검증 job만 재실행할 수 있다. 태그/패키지를 이미 게시한 publish job 전체를 다시 실행하지 않는다. finalize도 같은 원래 배포 실행에서 재실행한다. publish를 재실행하면 기존 tag 충돌로 중단한다.
- GitHub Packages alpha의 Xamarin 경로는 같은 실행에서 검증한 후보 nupkg를 재사용한다. GitHub Packages alpha는 개발용이며 정식 배포 완료의 근거가 아니다. 정식 JitPack/NuGet 배포는 위 절차를 따른다.

지원 모델 전체의 영향 검토에서 정한 회귀 대상·실행 결과·생략 근거는 작업 기록에 남긴다. 실기 결과에는 모델·OS·앱 버전을 함께 기록한다. 수행하지 않은 검증을 passed로 작성하지 않는다.

GitHub Release는 배포 단계에서 draft로 생성한다. 같은 Deployment의 finalize는 두 공개 패키지 검사가 성공하고 해당 실행이 만든 Release ID임을 확인해 정식 공개/latest로 전환한다. SDK의 태그·NuGet은 이보다 앞서 외부에 존재할 수 있으므로 draft가 패키지 비공개를 뜻하지는 않는다.

## 매뉴얼과 릴리즈 노트 완료 조건

변경 기능과 앱 매뉴얼·SDK 국·영문 4종을 대조하고 생성 PDF를 화면 검수한다. 적용 제외 판단에는 사유와 근거를 남긴다. 릴리즈 노트 초안과 Excel 반영 여부도 별도 작업 기록으로 관리한다. SDK Actions는 이 기록을 JSON으로 제출받지 않으며 자동 패키지 검사 성공이 화면 검수·Excel 반영 완료를 뜻하지 않는다. 자세한 수동 인계 절차는 release-notes-manual.md를 따른다.

## Broadcast 문서 자동 검사

`npm run validate:manuals --prefix tools/MdToPdf`는 API 말단 항목마다 직접 Broadcast 설명이 있는지, SDK Kotlin/C# Action과 문서가 일치하는지, 4종 매뉴얼의 계약이 일치하는지 검사한다. 목차 링크·공통 예제·제거한 구형 내용 검사도 유지한다. Broadcast 블록 수는 실행 결과로 출력하며 고정 개수나 공개 API 개수 추정값을 통과 조건으로 사용하지 않는다.

계약 비교에는 Action·대상 패키지·전송 방식과 Extra 표의 코드 식별자·타입·필수 여부·리터럴 값을 사용한다. 일반 설명의 인라인 코드·버전 표현은 비교하지 않는다. 본문에만 명시된 후속/GET Action 식별자는 계속 비교하며, 직접 명령을 지원하지 않는 항목에는 기존 지원 불가 선언을 유지한다. 계약 표의 코드 값과 설명 문구를 구분해 작성한다.

정상·오류 회귀 시험은 `npm run test:manuals --prefix tools/MdToPdf`로 실행한다. 같은 시험을 후보 CI에서도 실행한다. PDF 생성·화면 검수는 별도 검증이다.

## 게시 없는 GitHub 리허설

`Deployment(rehearsal=true)`는 `Rehearse SDK Release`를 호출한다. 운영 후보 정책은 develop으로 유지한다. 리허설 후보는 수동 실행·동일 저장소·현재 브랜치와 정확한 SHA를 요구하며 `rehearsal_only` 기록은 운영 승격에 사용할 수 없다.

실제 GitHub run API와 artifact 서비스를 통과한 후보를 공통 prepare/check 코드로 검증한다. 임시 bare Git에만 태그를 보내고, 새 Ubuntu/Windows runner는 전달된 Maven/NuGet feed에서 패키지를 소비한다. `contents: read`만 사용하며 sdk-release와 NUGET_API_KEY를 참조하지 않는다. 최종 collector는 실제 needs로 would_publish/blocked를 기록한다. 이것은 공개 완료 기록이 아니다.

`rehearsal_case`는 normal, android-failure, dotnet-failure, consumer-skip, tamper를 지원한다. 실패 주입은 리허설에만 존재하며 실패 job은 실제 failure로 남는다. collector의 성공으로 workflow 전체 실패를 숨기지 않는다. 취소와 실패 job 재실행도 실제 Actions에서 확인하고 run URL/attempt/artifact ID를 기록한다.

리허설은 실제 게시 토큰 권한·운영 environment·NuGet 신규 버전 전파·JitPack 신규 태그 빌드를 검증하지 않는다. 이미 공개된 버전의 Verify Published Samples는 공개 다운로드 경로만 추가 확인한다. develop 반영 후 게시 없는 검사, 원격 정책 적용, 최초 실게시·실기 검증은 각각 별도 단계다.

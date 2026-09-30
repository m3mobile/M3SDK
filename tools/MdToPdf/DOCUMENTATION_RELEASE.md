# 매뉴얼만 개정할 때

매뉴얼 내용이 바뀌면 다운로드 파일명과 문서 내부의 매뉴얼 버전을 함께 올린다. 같은 이름의 PDF를 다른 내용으로 덮어쓰지 않는다.

현재 개정은 매뉴얼 **2.3.17**, 대상 SDK **2.3.15**다. SDK API·바이너리·NuGet 버전은 변경하지 않는다. Java/Kotlin 및 Xamarin의 한·영 Markdown 4종을 같은 개정으로 관리한다.

## 생성 및 검토

1. 4개 Markdown 첫머리의 매뉴얼 버전과 대상 SDK 버전을 확인한다. PDF 링크의 매뉴얼 버전도 일치시킨다.
2. `node tools/MdToPdf/validate-manuals.js`로 목차와 Broadcast 명세를 확인한다.
3. 다음 명령으로 같은 소스에서 PDF 4개를 생성한다.

```shell
node tools/MdToPdf/convert-md-to-pdf.js --input docs/M3SDK_Manual_kr.md --input docs/M3SDK_Manual_en.md --input docs/M3SDK_Xamarin_Manual_kr.md --input docs/M3SDK_Xamarin_Manual_en.md --output output/pdf/manual-2.3.17 --temp build/manual-2.3.17 --version 2.3.17
```

4. 표지 버전, 추가·변경 페이지, 목차 링크, 한글 글꼴, 코드와 표의 잘림을 확인한다.
5. 검토한 소스 커밋과 PDF별 SHA-256을 함께 기록한다.

## 배포

- 문서 전용 GitHub Release는 `docs-2.3.17`처럼 `docs-` 태그를 사용한다. 매뉴얼의 표시 버전과 PDF 파일명은 `2.3.17`이다. SDK 릴리즈 태그와 충돌시키지 않는다.
- 기존 `2.3.15` SDK 릴리즈와 `docs-2.3.16` 문서 릴리즈의 PDF는 그대로 보존한다. 새로운 내용은 `M3SDK_Manual_kr_v2.3.17.pdf`처럼 새 이름으로 배포한다.
- 먼저 Draft Release에 PDF 4개와 해시를 준비해 검토한다. 이 PR을 작성하는 단계에서는 Release를 공개하지 않는다.
- 공개 배포 시 새 Release의 4개 다운로드 URL이 동작하는지 확인한 후 저장소의 새 링크를 공개한다. 새 링크를 포함한 PR을 먼저 병합해 깨진 다운로드 링크를 만들지 않는다.
- 이번 문서 릴리즈 `docs-2.3.17`은 PDF 검증 후 GitHub Releases의 Latest로 지정한다. 문서의 대상 SDK 버전 `2.3.15`와 SDK 패키지 배포는 그대로 유지한다.
- 기존 `Deployment` workflow는 SDK 빌드·태그·NuGet 배포까지 수행한다. 문서만 개정할 때 실행하지 않는다. `prepare-release.js --version`도 SDK 의존성과 릴리즈 링크를 함께 바꾸므로 이 절차에 사용하지 않는다.
- **외부 사이트의 SDK Release Note 다운로드 URL은 사용자가 직접 수정한다. 작업자는 직접 변경하지 않고, 매뉴얼 PDF가 새 버전으로 배포될 때마다 “외부 사이트 SDK Release Note의 다운로드 URL을 수정해야 함”을 사용자에게 알린다.**

다음 SDK 정식 릴리즈에서도 매뉴얼 버전을 이미 배포한 PDF와 재사용하지 않는다. SDK 버전과 매뉴얼 버전이 달라지면 문서 첫머리의 두 값을 각각 유지한다.

# 매뉴얼만 개정할 때

매뉴얼 내용이 바뀌면 다운로드 파일명과 문서 내부의 매뉴얼 버전을 함께 올린다. 같은 이름의 PDF를 다른 내용으로 덮어쓰지 않는다.

현재 공개 매뉴얼은 **2.3.15**이며, 검토 중인 다음 개정은 **2.3.18 초안**이다. 이전에 공개했던 2.3.16·2.3.17 Release와 태그는 철회했다. 대상 SDK는 **2.3.15**다. SDK API·바이너리·NuGet 버전은 변경하지 않는다. Java/Kotlin 및 Xamarin의 한·영 Markdown 4종을 같은 개정으로 관리한다.

## 생성 및 검토

1. 4개 Markdown 첫머리의 매뉴얼 버전과 대상 SDK 버전을 확인한다. 초안에서는 현재 공개 PDF(2.3.15) 링크를 그대로 둔다. 2.3.18 PDF 공개가 승인되면 새 다운로드 링크로 교체한다.
2. `node tools/MdToPdf/validate-manuals.js`로 목차와 Broadcast 명세를 확인한다.
3. 다음 명령으로 같은 소스에서 PDF 4개를 생성한다.

```shell
node tools/MdToPdf/convert-md-to-pdf.js --input docs/M3SDK_Manual_kr.md --input docs/M3SDK_Manual_en.md --input docs/M3SDK_Xamarin_Manual_kr.md --input docs/M3SDK_Xamarin_Manual_en.md --output output/pdf/manual-2.3.18 --temp build/manual-2.3.18 --version 2.3.18
```

4. 표지 버전, 추가·변경 페이지, 목차 링크, 한글 글꼴, 코드와 표의 잘림을 확인한다. 외부 링크는 독자가 실제로 열 수 있는 대상인지 확인한다. 로컬 Git에 커밋이 있어도 링크한 GitHub 저장소에 그 커밋이 없으면 링크를 넣지 않는다.
5. 검토한 소스 커밋과 PDF별 SHA-256을 함께 기록한다.

## 배포

- 문서 전용 GitHub Release는 `docs-2.3.18`처럼 `docs-` 태그를 사용한다. 매뉴얼의 표시 버전과 PDF 파일명은 `2.3.18`이다. SDK 릴리즈 태그와 충돌시키지 않는다.
- 공개 중인 2.3.15 PDF는 그대로 보존한다. 철회한 2.3.16·2.3.17 파일명도 재사용하지 않는다. 새로운 내용은 `M3SDK_Manual_kr_v2.3.18.pdf`처럼 새 이름으로 배포한다.
- PDF 4개와 해시를 로컬에서 준비해 검토한다. **사용자의 명시적 승인 전에는 PR 병합, Release 생성·공개, Latest 변경을 하지 않는다.**
- 공개 배포가 승인되면 새 Release의 PDF 4개를 공개하고 다운로드 URL이 동작하는지 확인한 뒤 저장소의 링크를 변경한다. 파일이 존재하기 전에 새 다운로드 링크를 공개하지 않는다.
- 사용자 승인으로 `docs-2.3.18`을 공개하는 경우, 별도 승인을 받은 범위에서만 Latest를 변경한다. 문서의 대상 SDK 버전 `2.3.15`와 SDK 패키지 배포는 그대로 유지한다.
- 기존 `Deployment` workflow는 SDK 빌드·태그·NuGet 배포까지 수행한다. 문서만 개정할 때 실행하지 않는다. `prepare-release.js --version`도 SDK 의존성과 릴리즈 링크를 함께 바꾸므로 이 절차에 사용하지 않는다.
- **외부 사이트의 SDK Release Note 다운로드 URL은 사용자가 직접 수정한다. 작업자는 직접 변경하지 않고, 매뉴얼 PDF가 새 버전으로 배포될 때마다 “외부 사이트 SDK Release Note의 다운로드 URL을 수정해야 함”을 사용자에게 알린다.**

다음 SDK 정식 릴리즈에서도 매뉴얼 버전을 이미 배포한 PDF와 재사용하지 않는다. SDK 버전과 매뉴얼 버전이 달라지면 문서 첫머리의 두 값을 각각 유지한다.

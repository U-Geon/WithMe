# KMU-MobileProgramming
2023 - 2학기 모바일프로그래밍 성동구 우리 아이 안심 서비스 앱 (WithMe)

## 개발 주제
맞벌이 가정을 위해 병원에 가야하는 아이를 위한 아이 안심 동행 서비스

## 개발 기간
- 23.09.12 ~ 23.12.6

## 개발 환경
- Tool: Android Studio
- Language: Kotlin
- Real Device: 2개 Galaxy Z flip 3
- Android Virtual Device (AVD) : Pixel 2 API 29

## 설치 및 실행 방법
- Github Repository Clone 및 Android Studio Run 'app'

## 프로젝트 팀원
- 최선우 (팀장)
- 서준형
- 유건
- 곽우진
- 하은영

## 서버 및 데이터베이스
> ### Model


> ### Service

## 페이지별 화면 설명 및 주요 기능 설명
> ### 회원가입 페이지 (activity_signup.xml, SignupActivity)
  - ConstraintLayout 사용

> ### 로그인 페이지
  - LinearLayout 사용
  - 로그인이 가능한 화면
  - 로그인 시
    - 미리 만들어진 관리자 ID로 로그인 시 관리자 액티비티로 넘어감
    - 사용자 ID로 로그인 시 사용자 메인 액티비티로 넘어감
      - 로그인 시 세션 기능을 위해 SharedPreference에 사용자 ID 저장.
     
> ### 홈 페이지
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 아이 인적사항 입력 페이지 (activity_kid_information.xml, KidInformationActivity)
  - LinearLayout 사용
  - 아이 인적사항을 입력 받는 페이지
  - 이름, 전화번호, 주민등록번호, 아이 상태 입력할 수 있는 editText 사용 
  - 이후 입력한 정보를 다음 메인 서비스 액티비티에 putExtra로 값을 넘겨줌.

> ### 메인 서비스 페이지
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 아동상태 및 병원 동행 결과
  - 관리자가 입력한 병원 동행 결과를 볼 수 있는 페이지
  - 동행 완료 버튼 클릭 시 현재 액티비티로 이동됌.
  - 확인 버튼 누를 시 새로운 서비스를 위해 다시 메인 페이지로 이동.
  - 

> ### 마이 페이지
  - 사용자의 정보 (이름, 서비스 이용 횟수, 예치금 잔액)을 확인할 수 있는 페이지
  - 예치금 입금하기, 예치금 충전 내역, 아동 상태 및 병원 동행 결과 모아보기 페이지를 라우팅

> ### 예치금 결제하기
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 예치금 내역 페이지
  - 사용자가 사용한 예치금 내역을 볼 수 있는 페이지
  - RecyclerView를 사용하여 예치금 내역을 보여주는 item 여러 개 반복 생성
  - item에는 예치금 사용 날짜, 사용 금액, 남은 잔액 등을 나타냄.
    - 우측 중앙의 버튼을 누를 시 

> ### 서비스 사용 내역
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 메인 서비스 페이지
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

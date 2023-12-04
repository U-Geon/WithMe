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
> ### 회원가입 페이지 (activity_signup.xml, SignupActivity.kt)
  - ConstraintLayout 사용
  - ㅁㄴㅇㄹ

> ### 아이디 / 비밀번호 찾기 페이지 
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 로그인 페이지 (activity_login.xml, LoginActivity.kt)
  - LinearLayout 사용
  - 로그인이 가능한 화면
  - 앱을 껐다 키거나 홈버튼으로 앱을 내렸다가 다시 실행할 경우 로그인 화면으로 가는 불상사 발생.
    - sharedPreference로 사용자 정보가 있는 경우, 자동으로 메인 액티비티로 넘어가게끔 처리. 
  - 로그인 시
    - 미리 만들어진 관리자 ID로 로그인 시 관리자 액티비티로 넘어감
    - 사용자 ID로 로그인 시 사용자 메인 액티비티로 넘어감
      - 로그인 시 세션 기능을 위해 SharedPreference에 사용자 ID 저장.
     
> ### 메인 페이지 - 홈 프라그먼트 (fragment_home.xml, HomeFragment.kt)
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 아이 인적사항 입력 페이지 (activity_kid_information.xml, KidInformationActivity.kt)
  - LinearLayout 사용
  - 아이 인적사항을 입력 받는 페이지
  - 이름, 전화번호, 주민등록번호, 아이 상태 입력할 수 있는 editText 사용 
  - 이후 입력한 정보를 다음 메인 서비스 액티비티에 putExtra로 값을 넘겨줌.

> ### 서비스 페이지 (
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 아동상태 및 병원 동행 결과 (activity_service_result.xml, ServiceResultActivity.kt)
  - 관리자가 입력한 병원 동행 결과를 볼 수 있는 페이지
  - 동행 완료 버튼 클릭 시 현재 액티비티로 이동됌.
  - 확인 버튼 누를 시 새로운 서비스를 위해 다시 메인 페이지로 이동. 

> ### 메인 페이지 - 마이 프라그먼트 (fragment_my.xml, MyFragment.kt)
  - 사용자의 정보 (이름, 서비스 이용 횟수, 예치금 잔액)을 확인할 수 있는 페이지
  - 예치금 입금하기, 예치금 충전 내역, 아동 상태 및 병원 동행 결과 모아보기 페이지를 라우팅 

> ### 예치금 결제하기
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 예치금 내역 페이지
  - 사용자가 사용한 예치금 내역을 볼 수 있는 페이지
  - RecyclerView를 사용하여 예치금 내역을 보여주는 item 여러 개 반복 생성 
  - item에는 예치금 사용 날짜, 사용 금액, 남은 잔액 등을 나타냄. 

> ### 서비스 사용 내역 페이지
  - 사용자가 사용한 서비스 내역을 볼 수 있는 페이지
  - RecyclerView를 사용하여 서비스 사용 내역을 볼 수 있는 item을 여러 개 반복 생성
  - 각 item에는 서비스 사용 날짜, 서비스 사용 병원, 우측 버튼이 위치합니다.
    - 우측 버튼 클릭 시 괸리자가 입력한 세부 내역을 볼 수 있습니다.
   
> ### 설정 페이지
  - 앱의 설정 기능을 담당하는 페이지
  - 여기다 적어줘

## 관리자 앱
> ### 메인 액티비티 - 홈 프라그먼트 (fragment_admin_home.xml, AdminHomeFragment.kt)
  - 사용자가 신청한 서비스 리스트를 보여주는 액티비티
  - RecyclerView를 사용하여 신청 목록을 쭉 나열해줌.
  - 각 아이템은 클릭 시 모달창 이벤트를 발생시키는 layout, 그 layout 안에는 시작 장소, 병원 이름, 도착 장소에 대한 정보가 나열되어있다.
    - 모달창에는 동행 정보와 아이 인적사항 (이름, 전화번호, 주민등록번호), 당시 아이 상태를 확인할 수 있다.
    - 동행 버튼 클릭 시 관리자 서비스 페이지로 이동할 수 있다.
    - 취소 시 모달창을 닫을 수 있음.

> ### 서비스 페이지
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ
    
> ### 아동 상태 및 병원 동행 결과 작성 페이지 (activity_admin_service_result_write.xml, AdminServiceResultWriteActivity.kt)
  - 관리자가 서비스 동행 완료 시 넘어가지는 페이지
  - 이 곳에 서비스 결과 (병원 방문 이후 아이 상태, 검진 내역, 약제비 & 진료비 등) 를 입력할 수 있게 EditText 설정.
  - 확인 버튼 누를 시 서버에 동행 결과 데이터를 전송하며 사용자의 서비스 마지막 페이지에 동행 완료 버튼을 보이게 하고 클릭할 수 있게끔 설정.

> ### 예치금 관리 페이지
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

## 기능 위주 실행화면 예시
> 앱 초반 실행 후 메인 서비스 신청 후 인적사항 입력


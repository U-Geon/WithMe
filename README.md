# KMU-MobileProgramming
2023 - 2학기 모바일프로그래밍 성동구 우리 아이 안심 서비스 앱 (WithMe)

## 개발 주제
맞벌이 가정을 위해 병원에 가야 하는 아이를 위한 아이 안심 동행 서비스

## 개발 기간
- 23.09.12 ~ 23.12.5

## 개발 환경
- Tool: Android Studio
- Language: Kotlin
- Real Device: Galaxy Z flip 3 2개
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
> ### 회원가입 페이지 (activity_signup.xml, SignupActivity.kt) (하은영)
  - ConstraintLayout 사용.
  - 아이디, 비밀번호, 이름, 전화번호, 주소 입력할 수 있는 editText 사용.
  - 가족 관계 증명서 이미지를 올리기 위한 ImageView, READ_MEDIA_IMAGES 사용.
  - 회원가입 버튼 클릭 시, 서버로 데이터 전송.

> ### 아이디 / 비밀번호 찾기 페이지 (IdFindFragment.kt, PwFindFragment.kt) (곽우진)
  - 아이디 찾기, 비밀번호 찾기 프라그먼트 사용
  - 

> ### 로그인 페이지 (activity_login.xml, LoginActivity.kt) (유 건)
  - LinearLayout 사용.
  - 로그인이 가능한 화면.
  - 앱을 껐다 키거나 홈버튼으로 앱을 내렸다가 다시 실행할 경우 로그인 화면으로 가는 불상사 발생.
    - sharedPreference로 사용자 정보가 있는 경우, 자동으로 메인 액티비티로 넘어가게끔 처리. 
  - 로그인 시
    - 미리 만들어진 관리자 ID로 로그인 시 관리자 액티비티로 넘어감.
    - 사용자 ID로 로그인 시 사용자 메인 액티비티로 넘어감.
      - 로그인 시 세션 기능을 위해 SharedPreference에 사용자 ID 저장.
     
> ### 바텀 네비게이션 (activity_main.xml, MainActivity.kt) (하은영)
  - BottomNavigationView 및 화면이 표시되는 부분을 구현하기 위해 FrameLayout 사용.
  - home, my, setting의 총 세 가지의 옵션: 각각 프래그먼트로 개발.
     
> ### 메인 페이지 - 홈 프라그먼트 (fragment_home.xml, HomeFragment.kt) (하은영)
  - 안심동행 신청 버튼: 안심 동행을 신청하기 위한 아이 인적사항 입력 페이지를 라우팅.
  - 동행 상황 바로 가기 버튼: 동행 현황에 대해 볼 수 있는 페이지를 라우팅.
  - SharedPreferences 기능을 사용: 안심동행이 진행 중일 때 안심동행 신청을 누르거나 안심동행이 진행 중이지 않을 때, 안심동행 신청 혹은 동행 상황 바로가기를 누르지 못하게(Toast Message) 구현.
  - 예치금 바로가기: 예치금을 입금할 수 있는 안내 메시지가 있는 페이지를 라우팅.
  - 로그아웃: 로그인 시에 저장되어 있던 SharedPreferences의 아이디를 지워 로그아웃 기능을 구현.

> ### 아이 인적사항 입력 페이지 (activity_kid_information.xml, KidInformationActivity.kt) (유 건)
  - LinearLayout 사용
  - 아이 인적사항을 입력 받는 페이지
  - 이름, 전화번호, 주민등록번호, 아이 상태 입력할 수 있는 editText 사용 
  - 이후 입력한 정보를 다음 메인 서비스 액티비티에 putExtra로 값을 넘겨줌.

> ### 서비스 페이지 (곽우진)
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ

> ### 아동상태 및 병원 동행 결과 (activity_service_result.xml, ServiceResultActivity.kt) (유 건)
  - 관리자가 입력한 병원 동행 결과를 볼 수 있는 페이지
  - 동행 완료 버튼 클릭 시 현재 액티비티로 이동됌.
  - 확인 버튼 누를 시 새로운 서비스를 위해 다시 메인 페이지로 이동. 

> ### 메인 페이지 - 마이 프라그먼트 (fragment_my.xml, MyFragment.kt, MyList.kt, MyListAdapter.kt) (하은영)
  - 사용자의 정보(이름, 서비스 이용 횟수, 예치금 잔액)을 확인할 수 있는 페이지.
  - RecyclerView를 사용하여 예치금 입금하기, 예치금 충전 내역, 서비스 사용 내역 리스트 구현 -> 각각의 기능이 있는 페이지를 라우팅.

> ### 예치금 결제하기 (PaymentActivity.kt) (하은영)
  - 결제 api, 가상계좌의 방법 모색 -> 실제로 사용하려면 사업자 등록증 혹은 비용 문제 발생: 사용자가 관리자 계좌로 입금하게 되면, 관리자가 직접 예치금을 관리하는 방식으로 구현.
  - TextView를 사용하여 계좌번호 안내.
  - 자세한 방식은 이후 관리자앱에서 설명.

> ### 예치금 내역 페이지(DepositChargingHistoryActivity.kt) (유 건)
  - 사용자가 사용한 예치금 내역을 볼 수 있는 페이지
  - RecyclerView를 사용하여 예치금 내역을 보여주는 item 여러 개 반복 생성 
  - item에는 예치금 사용 날짜, 사용 금액, 남은 잔액 등을 나타냄. 

> ### 서비스 사용 내역 페이지(ServiceUsageHistoryActivity.kt) (유 건)
  - 사용자가 사용한 서비스 내역을 볼 수 있는 페이지
  - RecyclerView를 사용하여 서비스 사용 내역을 볼 수 있는 item을 여러 개 반복 생성
  - 각 item에는 서비스 사용 날짜, 서비스 사용 병원, 우측 버튼이 위치합니다.
    - 우측 버튼 클릭 시 괸리자가 입력한 세부 내역을 볼 수 있습니다.
   
> ### 메인 페이지 - 설정 프래그먼트(SettingFragment.kt) (곽우진)
  - 앱의 설정 기능을 담당하는 페이지
  - 여기다 적어줘

## 관리자 앱
> ### 메인 액티비티 - 홈 프라그먼트 (fragment_admin_home.xml, AdminHomeFragment.kt) (유 건, 하은영 도움)
  - 사용자가 신청한 서비스 리스트를 보여주는 액티비티.
  - RecyclerView를 사용하여 신청 목록을 쭉 나열해줌.
  - 각 아이템은 클릭 시 모달창 이벤트를 발생시키는 layout, 그 layout 안에는 시작 장소, 병원 이름, 도착 장소에 대한 정보가 나열되어 있음.
    - 모달창에는 동행 정보와 아이 인적사항 (이름, 전화번호, 주민등록번호), 당시 아이 상태를 확인할 수 있음.
    - 동행 버튼 클릭 시 관리자 서비스 페이지로 이동할 수 있음.
    - 취소 시 모달창을 닫을 수 있음.

> ### 서비스 페이지(AdminServiceActivity.kt) (곽우진)
  - 여기에 작성해줘
  - ㅁㄴㅇㄹ
    
> ### 아동 상태 및 병원 동행 결과 작성 페이지 (activity_admin_service_result_write.xml, AdminServiceResultWriteActivity.kt) (유 건)
  - 관리자가 서비스 동행 완료 시 넘어가지는 페이지
  - 이 곳에 서비스 결과 (병원 방문 이후 아이 상태, 검진 내역, 약제비 & 진료비 등) 를 입력할 수 있게 EditText 설정.
  - 확인 버튼 누를 시 서버에 동행 결과 데이터를 전송하며 사용자의 서비스 마지막 페이지에 동행 완료 버튼을 보이게 하고 클릭할 수 있게끔 설정.

> ### 예치금 관리 페이지1(AdminDepositFragment.kt, AdminDepositManagementActivity.kt) (하은영)
  - RecyclerView를 사용하여, 모든 사용자들의 정보(아이디, 이름)을 확인할 수 있는 리스트 구현.
  - 클릭된 리스트의 해당 사용자의 예치금을 관리할 수 있는 페이지를 라우팅.
  - SearchView를 사용하여, 아이디로 사용자를 검색할 수 있는 검색창 기능 존재.

> ### 예치금 관리 페이지2 (하은영)
  - 사용자의 정보(아이디, 이름, 전화번호, 현재 예치금) 확인 및 예치금을 관리할 수 있는 페이지.
  - +버튼 및 - 버튼: 예치금 입금을 받았을 때 더할 수 있는 기능과, 병원에서 사용한 만큼 뺄 수 있는 기능. -> 각각의 버튼 클릭 시, 페이지에서 수정된 예치금을 미리 확인 가능.
  - 수정 완료 버튼: 수정된 예치금 정보와 해당 사용자의 아이디 정보를 서버로 전송하여, 사용자 각각의 예치금을 관리.

## 기능 위주 실행화면 예시
> 앱 초반 실행 후 메인 서비스 신청 후 인적사항 입력


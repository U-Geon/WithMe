# KMU-MobileProgramming
2023 - 2학기 모바일프로그래밍 성동구 우리 아이 안심 서비스 앱 (WithMe)

## 개발 주제
맞벌이 가정을 위해 병원에 가야 하는 아이를 위한 아이 안심 동행 서비스

## 개발 기간
- 23.09.12 ~ 23.12.5

## 개발 환경
- Tool: Android Studio
- Language: Kotlin
- Physical Device: Galaxy Z flip 3 2개
- Android Virtual Device (AVD) : Pixel 2 API 29

## 설치 및 실행 방법
- 안드로이드 스튜디오

      1. 본 프로젝트를 로컬 저장소에 Clone 또는 다운로드합니다.
      2. 안드로이드 스튜디오를 실행 후, /frontend 에서 오픈합니다.
      3. 가상 또는 실제 디바이스를 준비하여 실행합니다.
  
- 실제 디바이스에 설치

[다운로드 링크](https://drive.google.com/file/d/1miBCEC8j2sG9Zc9NPZE1DhBv-DW4PJER/view?usp=sharing)

      1 - 1. 본 프로젝트를 로컬 저장소에 Clone 또는 다운로드 후, /frontend/build_apk 폴더의 압축 파일을 찾습니다.
      1 - 2. 상단 다운로드 링크에 접속해 압축 파일을 다운로드합니다.
      2. 파일 압축을 해제 후, apk 파일을 디바이스에 설치 후 실행합니다.

※ 본 애플리케이션은 사용자의 실시간 위치를 이용합니다. 실행 시 위치 사용 권한을 수락해야 합니다.
      
## 프로젝트 팀원
<table>
      <thead>
          <tr>
              <th colspan="5"> 팀원 목록 </th>
          </tr>
        <tr>
              <th colspan="2"> 백엔드 </th>
              <th colspan="3"> 프론트엔드 </th>
          </tr>
      </thead>
      <tbody>
          <tr>
            <tr>
              <td align='center'>최선우 | 팀장</td>
              <td align='center'>서준형</td>
              <td align='center'>하은영</td>
              <td align='center'>곽우진</td>
              <td align='center'>유 건</td>
            </tr>
            <tr>
              <td align='center'>소프트웨어학부</td>
              <td align='center'>소프트웨어학부</td>
              <td align='center'>소프트웨어학부</td>
              <td align='center'>소프트웨어학부</td>
              <td align='center'>소프트웨어학부</td>
            </tr>
          </tr>
      </tbody>
  </table>

## 서버 및 데이터베이스
> ### 프로그램 구조도
![image](https://github.com/U-Geon/WithMe/assets/105338988/1af5f202-c35c-4c4e-9410-0d5275e7b0db)
> ### DB ERD
![erd](https://github.com/U-Geon/WithMe/assets/105338988/c5269937-7fe3-4654-8c15-64d3159fa9ac)



## 페이지별 화면 설명 및 주요 기능 설명
> ### 회원가입 페이지 (activity_signup.xml, SignupActivity.kt) (하은영)
  - ConstraintLayout 사용.
  - 아이디, 비밀번호, 이름, 전화번호, 주소 입력할 수 있는 editText 사용.
  - 본인인증을 위한 iamport api 사용.
  - 가족 관계 증명서 이미지를 올리기 위한 ImageView, READ_MEDIA_IMAGES 사용.
  - 회원가입 버튼 클릭 시, 서버로 데이터 전송.
  - register API를 통해 전송받은 사용자 계정 데이터를 DB에 저장 (서준형)

> ### 아이디 / 비밀번호 찾기 페이지 (activity_account_find_kt, activity_account_find.xml, fragment_id_find.xml, IdFindFragment.kt, fragment_pw_find.xml, PwFindFragment.kt) (곽우진)
  - 아이디 찾기, 비밀번호 찾기 프라그먼트 사용
  - AccountFindActivity 에서 아이디 찾기 / 비밀번호 찾기 버튼을 누르면 해당하는 프래그먼트를 화면에 표시.
  - find_pw API를 통해 비밀번호 전달 (최선우)

> ### 로그인 페이지 (activity_login.xml, LoginActivity.kt) (유 건)
  - LinearLayout 사용.
  - 로그인이 가능한 화면.
  - 앱을 껐다 키거나 홈버튼으로 앱을 내렸다가 다시 실행할 경우 로그인 화면으로 가는 불상사 발생.
    - sharedPreference로 사용자 정보가 있는 경우, 자동으로 메인 액티비티로 넘어가게끔 처리. 
  - 로그인 시
    - 미리 만들어진 관리자 ID로 로그인 시 관리자 액티비티로 넘어감.
    - 사용자 ID로 로그인 시 사용자 메인 액티비티로 넘어감.
      - 로그인 시 세션 기능을 위해 SharedPreference에 사용자 ID 저장.
      - login API를 통해 로그인 성공 확인 (최선우)
     
> ### 바텀 네비게이션 (activity_main.xml, MainActivity.kt) (하은영)
  - BottomNavigationView 및 화면이 표시되는 부분을 구현하기 위해 FrameLayout 사용.
  - home, my, setting의 총 세 가지의 옵션: 각각 프래그먼트로 개발.
  - 기본 화면을 home으로 설정.
     
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

> ### 서비스 페이지 (activity_service.xml, ServiceActivity.kt) (곽우진)
  - 먼저 로그인 화면에서 위치 사용에 관한 권한 요청을 받아야 함.
  - 네이버 지도 API를 호출하여 현재 자신의 위치를 표시하고, 아이가 있는 장소, 아이가 가야 할 병원, 아이가 귀가할 장소를 입력 후 서비스를 신청함.
  - 주소 입력 창은 SlidingDrawer를 사용해 스와이프로 올리고 내릴 수 있게 구현.
  - 주소 입력 시 서버에 업로드해 둔 카카오 우편번호 페이지를 호출하여 해당 위치의 전체 주소와 이름을 반환함.
  - apply_service API를 통해 서비스 신청 (최선우)
  - 모든 장소를 입력 후 최종 신청이 제출되면 1초마다 현재 서비스의 진행 상황을 묻는 요청을 보내는 스레드가 시작됨.
  - 관리자가 동행을 수락한 순간부터 마커 (네이버 지도) 가 생성되고, 관리자의 현재 위치를 표시함.
  - 관리자가 동행을 완료 후 결과를 입력하면 해당 결과를 서버에서 받아볼 수 있음.
  - 진행 중엔 status라는 boolean 변수를 sharedPreferences에 저장해, 메인 페이지 (HomeFragment) 에서 사용.
  - send_laction API를 통해 관리자 위치 및 서비스 진행 상황 전달 (최선우)
  - 위치 api 사용을 위해 daum_address API를 이용해 페이지를 렌더링 함 (서준형)

> ### 아동상태 및 병원 동행 결과 (activity_service_result.xml, ServiceResultActivity.kt) (유 건)
  - 관리자가 입력한 병원 동행 결과를 볼 수 있는 페이지
  - 동행 완료 버튼 클릭 시 현재 액티비티로 이동됨.
  - 확인 버튼 누를 시 새로운 서비스를 위해 다시 메인 페이지로 이동.
  - main_result API를 통해 현재 서비스의 동행 결과를 DB에서 추출해 전달 (서준형)

> ### 메인 페이지 - 마이 프라그먼트 (fragment_my.xml, MyFragment.kt, MyList.kt, MyListAdapter.kt) (하은영)
  - 사용자의 정보(이름, 서비스 이용 횟수, 현재 예치금)을 확인할 수 있는 페이지.
  - RecyclerView를 사용하여 예치금 입금하기, 예치금 충전 내역, 서비스 사용 내역 리스트 구현 -> 각각의 기능이 있는 페이지를 라우팅.
  - select_name_money_history_count_phone_number API를 통해 사용자의 정보 전달 (최선우)

> ### 예치금 결제하기 (activity_payment.xml, PaymentActivity.kt) (하은영)
  - 결제 api, 가상계좌의 방법 모색 -> 실제로 사용하려면 사업자 등록증 혹은 비용 문제 발생: 사용자가 관리자 계좌로 입금하게 되면, 관리자가 직접 예치금을 관리하는 방식으로 구현.
  - TextView를 사용하여 계좌번호 안내.
  - 자세한 방식은 이후 관리자앱에서 설명.

> ### 예치금 내역 페이지 (activity_deposit_charging_history.xml DepositChargingHistoryActivity.kt) (유 건)
  - 사용자가 사용한 예치금 내역을 볼 수 있는 페이지
  - RecyclerView를 사용하여 예치금 내역을 보여주는 item 여러 개 반복 생성 
  - item에는 예치금 사용 날짜, 사용 금액, 남은 잔액 등을 나타냄.
  - select_deposit API를 통해 전송받은 요청 데이터(id)를 통해 사용자를 검색 후 예치금 내역을 전달 (서준형)

> ### 서비스 사용 내역 페이지 (activity_service_usage_history.xml, ServiceUsageHistoryActivity.kt) (유 건)
  - 사용자가 사용한 서비스 내역을 볼 수 있는 페이지
  - RecyclerView를 사용하여 서비스 사용 내역을 볼 수 있는 item을 여러 개 반복 생성
  - 각 item에는 서비스 사용 날짜, 서비스 사용 병원, 우측 버튼이 위치합니다.
    - 우측 버튼 클릭 시 괸리자가 입력한 세부 내역을 볼 수 있습니다.
  - service_history API를 통해 사용자의 서비스 내역 전달 (최선우)
   
> ### 메인 페이지 - 설정 프래그먼트 (fragment_setting.xml, SettingFragment.kt, FaqActivity.kt, activity_faq.xml) (곽우진)
  - 앱의 설정 기능을 담당하는 페이지
  - 푸시 알림은 토글을 사용. 토글 값이 바뀔 때마다 토글이 켜져있는지 / 꺼져있는지를 저장하는 bool 변수를 sharedPreference에 저장함.
  - FAQ 버튼 클릭 시 자주 묻는 질문 액티비티를 연결.
  - 회원 탈퇴 버튼 클릭 시 정말 탈퇴할 것인지를 묻는 다이얼로그가 출력되고, 이를 수락하면 데이터베이스에서 로그인된 계정을 삭제함.
  - 이때 sharedPreferences에 저장된 로그인 정보를 함께 삭제 후, 로그인 창으로 복귀함.
  - delete_account API를 통해 요청한 사용자의 데이터를 DB에서 제거 후 성공 여부를 전달함 (서준형) 

## 관리자 앱
> ### 메인 액티비티 - 홈 프라그먼트 (fragment_admin_home.xml, AdminHomeFragment.kt) (유 건, 하은영 도움)
  - 사용자가 신청한 서비스 리스트를 보여주는 액티비티.
  - RecyclerView를 사용하여 신청 목록을 쭉 나열해줌.
  - 각 아이템은 클릭 시 모달창 이벤트를 발생시키는 layout, 그 layout 안에는 시작 장소, 병원 이름, 도착 장소에 대한 정보가 나열되어 있음.
    - 모달창에는 동행 정보와 아이 인적사항 (이름, 전화번호, 주민등록번호), 당시 아이 상태를 확인할 수 있음.
    - 동행 버튼 클릭 시 관리자 서비스 페이지로 이동할 수 있음.
    - 취소 시 모달창을 닫을 수 있음.
    - get_apply_service_list API를 통해 신청한 서비스 리스트 전달 (최선우)

> ### 서비스 페이지(activity_admin_service.xml, AdminServiceActivity.kt, AdminKidInformationActivity.kt, activity_admin_kid_information.xml) (곽우진)
  - 관리자가 메인 페이지에서 요청된 서비스 리스트를 확인 후 특정 서비스를 수락하면 본 페이지로 이동됨.
  - 아이 정보 확인하기 버튼을 눌러 해당 서비스에서 동행할 아이의 인적사항을 확인할 수 있음.
        - select_kid_info API를 통해 아이 인적사항 전달 (최선우)
  - 아이 정보 확인하기 창은 DialogFragment 기능을 활용하여 아예 다른 페이지로 이동하는 것이 아닌 서비스 화면에서 간단하게 확인할 수 있도록 구현함.
  - 픽업 시작 버튼을 누르면 해당 서비스에 대한 관리자의 실시간 위치와 진행 상황을 서버에 전달함. 사용자는 이 정보를 서버에 요청하는 것.
  - 진행 상황은 Integer 변수로 구분. -1은 대기 중, 0은 픽업 중, 1은 병원 동행 중, 2는 귀가 중, 3은 귀가 완료를 의미함.
  - 이 변수는 사용자에게 전달될 때 ServiceActivity 에서 일치하는 텍스트로 변환되어 출력됨.
  - get_location API를 통해 관리자의 실시간 위치와 진행 상황을 데이터 베이스에 저장 (서준형)
  - 관리자가 서비스를 완료 후 마지막으로 귀가 완료 버튼을 누르면 서비스는 종료되고 병원 동행 결과 작성 페이지로 이동함.
    
> ### 아동 상태 및 병원 동행 결과 작성 페이지 (activity_admin_service_result_write.xml, AdminServiceResultWriteActivity.kt) (유 건)
  - 관리자가 서비스 동행 완료 시 넘어가지는 페이지
  - 이 곳에 서비스 결과 (병원 방문 이후 아이 상태, 검진 내역, 약제비 & 진료비 등) 를 입력할 수 있게 EditText 설정.
  - 확인 버튼 누를 시 서버에 동행 결과 데이터를 전송하며 사용자의 서비스 마지막 페이지에 동행 완료 버튼을 보이게 하고 클릭할 수 있게끔 설정.
  - hospital_result API를 통해 병원 동행 결과를 데이터 베이스에 저장함. (최선우)

> ### 예치금 관리 페이지1 (fragment_admin_deposit.xml, AdminDepositFragment.kt) (하은영)
  - RecyclerView를 사용하여, 모든 사용자들의 정보(아이디, 이름)을 확인할 수 있는 리스트 구현.
  - 리스트 클릭 시, 해당 사용자의 예치금을 관리할 수 있는 페이지를 라우팅.
  - SearchView를 사용하여, 아이디로 사용자를 검색할 수 있는 검색창 기능 구현.
  - find_all_account API를 통해 DB에서 모든 사용자 계정의 정보를 전달 (서준형)
  - search_users API를 통해 요청데이터(id 정보)로 찾고자 하는 계정의 데이터만 추출해 전달 (서준형)

> ### 예치금 관리 페이지2 (activity_admin_deposit_management.xml, AdminDepositManagementActivity.kt) (하은영)
  - 사용자의 정보(아이디, 이름, 전화번호, 현재 예치금) 확인 및 예치금을 관리할 수 있는 페이지.
  - +버튼 및 - 버튼: 예치금 입금을 받았을 때 더할 수 있는 기능과, 병원에서 사용한 만큼 뺄 수 있는 기능. -> 각각의 버튼 클릭 시, 페이지에서 수정된 예치금을 미리 확인 가능.
  - 수정 완료 버튼: 수정된 예치금 정보와 해당 사용자의 아이디 정보를 서버로 전송하여, 사용자 각각의 예치금을 관리.
  - modify_deposit API를 통해 예치금 수정 요청을 보낸 사용자의 예치금을 수정해 DB에 업데이트 하고, 성공여부를 전달 (서준형)

## 기능 위주 실행화면 예시
> 앱 초반 실행 후 메인 서비스 신청 후 인적사항 입력


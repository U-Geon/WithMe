"""server URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from kids_guard.views import register, login, apply_service, select_state, select_deposit, edit_account_info, delete_account, get_faq, send_location, modify_state, select_kid_info, hospital_result, select_name_money_count_phone_number, complete_service, get_location, modify_deposit, daum_address, find_password, main_result, find_all_account, get_apply_service_list, service_history

urlpatterns = [
    # 사용자 전용
    path('register/', register, name = 'register'), 
    path('login/', login, name = 'login'),
    path('find_password/', find_password, name = 'find_password'), #비밀번호 찾기
    path('apply_service/', apply_service, name='apply_service'), # 출발 장소, 도착 장소를 필수로 입력 받고, 병원 정보, 신청 시점의 아이 정보를 입력으로 받음 -> db에 잘 들어갔으면 response로 신청완료됐다 응답
    path('select_state/', select_state, name = 'select_state'), #
    path('select_deposit/', select_deposit, name ='select_dsposit'),
    path('edit_account_info/', edit_account_info, name = 'edit_account_info'),
    path('delete_account/', delete_account, name = 'delete_account'),
    path('get_faq/', get_faq, name = 'get_faq'), 
    path('main_result', main_result, name='main_result'),
    path('service_history', service_history, name='service_history'),

    # 관리자 전용
    path('send_location/', send_location, name = 'send_location'), 
    path('modify_state/', modify_state, name = 'modify_state'), 
    path('select_kid_info/', select_kid_info, name = 'select_kid_info'),
    path('hospital_result/', hospital_result, name='hospital_result'), 
    path('select_name_money_count_phone_number/', select_name_money_count_phone_number, name='select_name_money_count'),
    path('complete_service/', complete_service, name='complete_service'),
    path('get_location/', get_location, name= 'get_location'),
    path('modify_deposit/', modify_deposit, name='modify_deposit'),
    path('find_all_account/', find_all_account, name='find_all_account'),
    path('admin/', get_apply_service_list, name = 'get_apply_service_list'),
    
    # 주소 검색을 위한 api
    path('daum_address.html/', daum_address, name='daum_address'),

]

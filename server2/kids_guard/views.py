from django.shortcuts import render
from django.db import connection
from django.http import JsonResponse, HttpResponse
from django.contrib.auth import authenticate, login
from django.views.decorators.csrf import csrf_exempt
import json

# from .models import Account

# Create your views here.
@csrf_exempt
def register(request):
    # all_records = Account.objects.all()
    # for i in all_records:
    #     print(i.id)
    #     print(i.passward)
    #     print(i.name)
    # return HttpResponse("OK", status = 200)
    # data = json.loads(request.body)
    # print(data)
    # print(data['key1'])
    # print(data['key2'])
    # return HttpResponse("OK", status = 200)
    data = json.loads(request.body)
    id = data['id']
    password = data['password']
    name = data['name']
    zip_code = data['zip_code']
    phone_number = data['phone_number']
    # name = '최선우'
    # zip_code = '12345'
    # phone_number = '01052980568'

    with connection.cursor() as cursor:
        cursor.execute(f"""insert into account (id, password, name, zip_code, phone_number)
                            values ('{id}', '{password}', '{name}', '{zip_code}', '{phone_number}') ;""")
    return HttpResponse("OK", status = 200)

@csrf_exempt
def login(request):
    data = json.loads(request.body)
    id = data['id']
    password = data['password']
    print(id)
    print(password)
    with connection.cursor() as cursor:
        cursor.execute(f"""select count(*)
                            from account
                            where id = '{id}' and password = '{password}' ;""")
        login = (cursor.fetchone())[0]
        print(login)
    # return HttpResponse("OK", status = 200)
    if login == 1 :
        return JsonResponse({'success': True, 'message': '로그인이 성공했습니다.'}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')
    else :
        return JsonResponse({'success': False, 'message': '로그인에 실패했습니다.'}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

@csrf_exempt
def apply_service(request):
    requestdata = json.loads(request.body)
    start_location = requestdata['start_location']
    arrival_location = requestdata['arrival_location']
    # hospital = requestdata['hospital']
    # real_time_personal_data = requestdata['real_time_personal_data']
    child_account_id = requestdata['child_account_id']
    

    if (start_location == "") or (arrival_location == "") or (child_account_id == ""):
        return JsonResponse({"success": False, "message": "Invaild Data."}, status = 400)

    else:
        with connection.cursor() as cursor:
            cursor.execute(f"""insert into relax_service (start_location, arrival_location, child_name, child_account_id)
                            values ('{start_location}','{arrival_location}',(SELECT name FROM child WHERE account_id = '{child_account_id}'),'{child_account_id}');""")
            cursor.execute(f"""insert into status (status, time, relax_service_id)
                            values ('승인전', NOW() ,(SELECT MAX(id) FROM relax_service));""")
        return JsonResponse({"success": True, "message": "Service Applied."}, status = 200)

# 관리자가 만들기
# def modify_state(request):
#     return JsonResponse({'상태 변경 완료': 1}, content_type ='application/json; charest=utf-8')

@csrf_exempt
def select_state(request):
    data = json.loads(request.body)
    id = data['id']
    # id = 'abcde'
    with connection.cursor() as cursor:
        cursor.execute(f"""select status, time, start_location, arrival_location, hospital
                            from status
                            join relax_service on status.relax_service_id = relax_service.id
                            where relax_service_id = (select id
                                                        from relax_service
                                                        where finish = 0)
                                    and relax_service.child_account_id = '{id}'
                            order by 2 ;""")
        state = []
        for i, v in enumerate(cursor.fetchall()):
            if i in range(3):
                state.append({'state': v[0], 'time': v[1],'location': v[2]})
            if i == 3:
                state.append({'state': v[0], 'time': v[1],'location': v[4]})
            if i == 4:
                state.append({'state': v[0], 'time': v[1],'location': v[3]})
        print(state)

    return JsonResponse({'states': state}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

@csrf_exempt
def select_deposit(request):
    
    requestdata = json.loads(request.body)
    account_id = requestdata['account_id']

    # account_id = request.GET.get['account_id']

    with connection.cursor() as cursor:
        cursor.execute(f"""SELECT date, change_amount, money
                            FROM money_history
                            WHERE account_id = '{account_id}'
                            ORDER BY 1 ;""")
    
        data = cursor.fetchall()
        json_data = {"예치금목록": [{"날짜": date, "지출": expenditure, "잔액": balance} for date, expenditure, balance in data]}
        return JsonResponse(json_data, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)
    
@csrf_exempt
def select_name_money_count(request):
    data = json.loads(request.body)
    id = data['id']
    with connection.cursor() as cursor:
        cursor.execute(f"""select account.name, account.money, count(relax_service.id) count
                            from account
                            join relax_service on account.id = relax_service.child_account_id
                            where account.id = "{id}" ;""")
        data = cursor.fetchall()[0]
    print(data)
    return JsonResponse({'name':data[0], 'money':data[1], 'uses':data[2]}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)


@csrf_exempt
def edit_account_info(request):
    data = json.loads(request.body)
    id = data['id']
    password = data['password']
    zip_code = data['zip_code']
    phone_number = data['phone_number']

    with connection.cursor() as cursor:
        cursor.execute(f"""update account
                        set password = '{password}'
                        where id = '{id}' ;
                        update account
                        set zip_code = '{zip_code}'
                        where id = '{id}' ;
                        update account
                        set phone_number = '{phone_number}'
                        where id = '{id}' ;""")

    return JsonResponse({"success": True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

@csrf_exempt
def delete_account(request):

    requestdata = json.loads(request.body)

    if requestdata['id'] == "":
         return JsonResponse({"success": False, "message": "Invaild ID"}, status = 400)
    else:     
        id = requestdata['id']
        with connection.cursor() as cursor:
            cursor.execute(f"""DELETE FROM account
                            WHERE id = '{id}';""")
    return JsonResponse({"success": True, "message": "Account deleted"}, status = 200)

@csrf_exempt
def get_faq(request):
    
    with connection.cursor() as cursor:
        cursor.execute(f"""SELECT question, answer
        FROM faq;""")
    
        data = cursor.fetchall()

    json_data = {"Qlist": [{"question": question, "answer": answer} for question, answer in data]}
        
    return JsonResponse(json_data, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)

def send_location(request):
    return JsonResponse({}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)

def modify_state(request):
    return JsonResponse({}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)

@csrf_exempt
def select_kid_info(request):
    data = json.loads(request.body)
    account_id = data['account_id']

    with connection.cursor() as cursor:
        cursor.execute(f"""select name, age, phone_number, personal_data
                            from child
                            where account_id = '{account_id}' ;""")
        kid_info = cursor.fetchall()[0]
    print(kid_info)

    return JsonResponse({'name': kid_info[0], 'age': kid_info[1], 'phone_number': kid_info[2], 'personal_data': kid_info[3]}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)

@csrf_exempt
def hospital_result(request):

    data = json.loads(request.body)
    account_id = data['account_id']
    result = data['result']

    with connection.cursor() as cursor:
        cursor.execute(f"""update relax_service
                            set result = '{result}'
                            where id = (select id
                                        from (select max(id) id
                                                from relax_service
                                                where child_account_id = '{account_id}') tmp) ;""")
    

    return JsonResponse({'success': True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)



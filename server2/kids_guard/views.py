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
    family = data['family']
    print(family)
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
        cursor.execute(f"""select count(id), case when manager is NULL then 0
                            when manager is not NULL then 1 end
                            from account
                            where id = '{id}' and password = '{password}' ;""")
        result = cursor.fetchone()
        login = result[0]
        admin = result[1]
        # print(login)
        # print(admin)
        print(result)
    # return HttpResponse("OK", status = 200)
    if login == 1 :
        if admin == 1:
            return JsonResponse({'success': True, 'admin': True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')
        else:
            return JsonResponse({'success': True, 'admin': False}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')
    else :
        return JsonResponse({'success': False, 'admin': False}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

def find_password(request):
    #request
    #id, name, phone
    data = json.loads(request.body) 
    id = data['id']

    with connection.cursor() as cursor:
        cursor.execute(f"""select password
                            from account
                            where id = "{id}" ;""")
        password = (cursor.fetchone())[0]

    #response
    #password

    return JsonResponse({'password' : password}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

@csrf_exempt
def apply_service(request):

    # 이름, 아이 핸드폰 번호, 부모 id
    requestdata = json.loads(request.body)

    startAddress = requestdata['startAddress']
    hospitalAddress = requestdata['hospitalAddress']
    endAddress = requestdata['endAddress']
    kidName = requestdata['kidName']
    phoneNumber = requestdata['phoneNumber']
    # 주민등록번호
    rrn = requestdata['rrn']
    status = requestdata['status']
    userId = requestdata['userId']

    with connection.cursor() as cursor:
        cursor.execute(f"""select count(*)
                            from child
                            where account_id = '{userId}' ;""")
        if (cursor.fetchone())[0] == 0:
            cursor.execute(f"""insert into child (name, age, resident_registration_number, phone_number, approval, account_id)
                            values ('{kidName}', 0, '{rrn}', '{phoneNumber}', "{status}", '{userId}') ;""")
    

    if (startAddress == "") or (endAddress == "") or (userId == ""):
        return JsonResponse({"success": False, "message": "Invaild Data."}, status = 400)

    else:
        with connection.cursor() as cursor:
            cursor.execute(f"""insert into relax_service (start_location, arrival_location, hospital, child_name, child_account_id, real_time_personal_data)
                            values ('{startAddress}','{endAddress}', '{hospitalAddress}',(SELECT name FROM child WHERE account_id = '{userId}'),'{userId}', '{status}');""")
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
def select_name_money_count_phone_number(request):
    data = json.loads(request.body)
    id = data['id']
    with connection.cursor() as cursor:
        cursor.execute(f"""select account.name, account.money, count(relax_service.id) count, account.phone_number
                            from account
                            join relax_service on account.id = relax_service.child_account_id
                            where account.id = "{id}" ;""")
        data = cursor.fetchall()[0]
    print(data)
    return JsonResponse({'name':data[0], 'money':data[1], 'uses':data[2], 'phone_number':data[3]}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)


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

# test 아직 안함
@csrf_exempt
def send_location(request):

    requestdata = json.loads(request.body)

    id = requestdata['id']


    with connection.cursor() as cursor:
        cursor.execute(f"""SELECT latitude, longitude
                            FROM real_time_location
                            WHERE status_relax_service_id = (SELECT MAX(id) FROM relax_service WHERE id = (SELECT id FROM relax_service WHERaccount_id = '{id}'));""")

        data = cursor.fetchall()

        if data == ():
            json_data = {"success" : False}

        else:
            json_data = {"success" : True, "latitude": data[0]['latitude'], "longitude": data[0]['longitude']}

        return JsonResponse(json_data, status = 200, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

#test 아직 안함
@csrf_exempt
def modify_state(request):

    requestdata = json.loads(request.body)

    id = requestdata['id']
    status = requestdata['status']

    with connection.cursor() as cursor:
        # cursor.execute(f"""UPDATE status SET status = '{status}' WHERE relax_service_id = (SELECT MAX(id) FROM relax_service WHERE id = (SELECT id FROM relax_service WHERE account_id = '{account_id}'));""")
        cursor.execute(f"""insert into status (status, time, relax_service_id)
                            values ('{status}', NOW() ,(SELECT MAX(id) FROM relax_service WHERE child_account_id = '{id}'));""")
        json_data = {"success" : True}

    return JsonResponse(json_data, status = 200, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

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


def complete_service(request):

    data = json.loads(request.body)
    id = data['id']
    status = data['status']

    with connection.cursor() as cursor:
        # cursor.execute(f"""UPDATE status SET status = '{status}' WHERE relax_service_id = (SELECT MAX(id) FROM relax_service WHERE id = (SELECT id FROM relax_service WHERE account_id = '{account_id}'));""")
        cursor.execute(f"""insert into status (status, time, relax_service_id)
                            values ('{status}', NOW() ,(SELECT MAX(id) FROM relax_service WHERE child_account_id = '{id}'));""")
        cursor.execute(f"""update relax_service
                            set finish = 1
                            where id = (select id
                                        from (select max(id) id
                                                from relax_service
                                                where child_account_id = 'abcde') tmp) ;""")
    json_data = {"success" : True}

    return JsonResponse({'success': True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)

def modify_deposit(request):
    requestdata = json.loads(request.body)
    id = requestdata['id']
    amount = requestdata['amount']

    with connection.cursor() as cursor:
        cursor.execute(f"""insert into money_history (date, money, change_amount, account_id)
                            values (NOW() , ((SELECT money FROM account WHERE id = '{id}') + '{amount}'), '{amount}', '{id}');""")
        
        cursor.execute(f"""UPDATE account SET money = money + '{amount}' WHERE id = '{id}';""")

        return JsonResponse({"success": True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')
    
def get_location(request):
    
    requestdata = json.loads(request.body)
    id = requestdata['id']
    latitude = requestdata['latitude']
    longitude = requestdata['longitude']

        
    with connection.cursor() as cursor:
        cursor.execute(f"""insert into real_time_location (time, status_status, status_relax_service_id, latitude, longitude)
                            values (NOW() , (SELECT status FROM status WHERE time = (SELECT MAX(time) FROM status WHERE relax_service_id = (SELECT MAX(id) FROM relax_service WHERE child_account_id = '{id}'))), (SELECT MAX(id) FROM relax_service WHERE child_account_id = '{id}'), '{latitude}', '{longitude}');""")
        

        return JsonResponse({"success": True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')


def daum_address(request):
    return render(request, 'main/daum_address.html')
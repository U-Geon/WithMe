from django.shortcuts import render
from django.db import connection
from django.http import JsonResponse, HttpResponse
from django.contrib.auth import authenticate, login
from django.views.decorators.csrf import csrf_exempt
import json

# from .models import Account

# Create your views here.

# 회원가입 api (사용자 사용)
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
    # family = data['family']
    # print(family)
    # name = '최선우'
    # zip_code = '12345'
    # phone_number = '01052980568'

    with connection.cursor() as cursor:
        cursor.execute(f"""insert into account (id, password, name, zip_code, phone_number)
                            values ('{id}', '{password}', '{name}', '{zip_code}', '{phone_number}') ;""")
    return JsonResponse({"success":True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

# 로그인 api (사용자 사용)
@csrf_exempt
def login(request):
    data = json.loads(request.body)
    id = data['id']
    password = data['password']
    print(id)
    print(password)
    with connection.cursor() as cursor:
        cursor.execute(f"""select count(id), manager
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
            return JsonResponse({'success': True, 'isAdmin': True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')
        else:
            return JsonResponse({'success': True, 'isAdmin': False}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')
    else :
        return JsonResponse({'success': False, 'isAdmin': False}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

# 비밀번호 찾기 api (사용자 사용)
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

# 서비스 신청 api (사용자 사용)
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

# 삭제된 api(사용 xx)
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

# 예치금 사용 내역 api (사용자 사용)
@csrf_exempt
def select_deposit(request):
    
    account_id = request.GET.get('userId', '')
    print(account_id)

    # account_id = request.GET.get['account_id']

    with connection.cursor() as cursor:
        cursor.execute(f"""SELECT date(date), change_amount, money
                            FROM money_history
                            WHERE account_id = '{account_id}'
                            ORDER BY 1 ;""")
    
        data = cursor.fetchall()
        print(data)
        json_data = {"예치금목록": [{"날짜": str(date), "지출": expenditure, "잔액": balance} for date, expenditure, balance in data]}
        print(json_data)
        return JsonResponse(json_data, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)

# 이름과 예치금, 이용 횟수, 전화번호를 전송하는 api (사용자, 관리자 사용)
@csrf_exempt
def select_name_money_count_phone_number(request):
    data = json.loads(request.body)
    id = data['id']
    with connection.cursor() as cursor:
        cursor.execute(f"""select count(*)
                            from account
                            join relax_service on account.id = relax_service.child_account_id
                            where account.id = "{id}" ;""")
        if cursor.fetchone()[0] == 0:
            cursor.execute(f"""select name, money, 0, phone_number
						from account
						where account.id = "{id}" ;""")
        else:
            cursor.execute(f"""select account.name, account.money, count(relax_service.id) count, account.phone_number
                            from account
                            join relax_service on account.id = relax_service.child_account_id
                            where account.id = "{id}" ;""")
        data = cursor.fetchall()[0]
    print(data)
    return JsonResponse({'name':data[0], 'money':data[1], 'uses':data[2], 'phone_number':data[3]}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)

# 계정 수정을 위한 api (사용자 사용)
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

# 계정 삭제를 위한 api (사용자 사용)
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

# faq를 전송하는 api (사용자 사용)
@csrf_exempt
def get_faq(request):
    
    with connection.cursor() as cursor:
        cursor.execute(f"""SELECT question, answer
        FROM faq;""")
    
        data = cursor.fetchall()

    json_data = {"Qlist": [{"question": question, "answer": answer} for question, answer in data]}
        
    return JsonResponse(json_data, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8', status = 200)

# 사용자가 관리자의 위치를 받기 위한 api (사용자 사용)
@csrf_exempt
def send_location(request):

    requestdata = request.GET.get('id', '')
    ##id도 보내줘요 ㅠㅠ
    id = requestdata
    print(type(id))

    with connection.cursor() as cursor:

        cursor.execute(f"""SELECT latitude, longitude, status.status ,real_time_location.time
                            FROM real_time_location
                            join status on status.relax_service_id = real_time_location.status_relax_service_id
                            WHERE status_relax_service_id = (SELECT MAX(id) 
																FROM relax_service 
                                                                WHERE child_account_id = '{id}')
							order by 4 desc
                            limit 1;""")
        data = cursor.fetchall()
        print(data)
        print('-------------------------')
        if len(data) != 0:
            data = [i for i in data[0]]
        

        if data == ():
            json_data = {"success" : False}

        else:
            if(data[2] == "승인전"):
                data[2] = -1
            elif(data[2] == '픽업시작'):
                data[2] = 0
            elif(data[2] == '픽업완료'):
                data[2] = 1
            elif(data[2] == '픽업시작'):
                data[2] = 2
            elif(data[2] == '귀가완료'):
                data[2] = 3
            json_data = {"success" : True, "latitude": data[0], "longitude": data[1], 'status' : data[2]}

        return JsonResponse(json_data, status = 200, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

# 서비스 상태를 변경하는 api (관리자 사용)
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

# 아이 정보를 보기 위한 api (관리자 사용)
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

# 아이의 병원 결과를 업데이트하는 api (관리자 전용)
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

# 서비스를 완료했다고 알리기 위한 api (관리자 전용)
@csrf_exempt
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

# 서비스가 끝나고 예치금을 변경하기 위한 api (관리자 전용)
@csrf_exempt
def modify_deposit(request):
    requestdata = json.loads(request.body)
    print(requestdata)
    id = requestdata['id']
    amount = requestdata['amount']
    with connection.cursor() as cursor:
        cursor.execute(f"""insert into money_history (date, money, change_amount, account_id)
                            values (NOW() , ((SELECT money FROM account WHERE id = '{id}') + '{amount}'), '{amount}', '{id}');""")
        
        cursor.execute(f"""UPDATE account SET money = money + {amount} WHERE id = '{id}';""")

        return JsonResponse({"success": True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

# 관리자의 위치를 사용자에게 보내주는 api (관리자 전용)
@csrf_exempt
def get_location(request):
    
    requestdata = json.loads(request.body)
    print(requestdata)
    id = requestdata['id']
    latitude = requestdata['lat']
    longitude = requestdata['lon']
    status = requestdata['status']

        
    with connection.cursor() as cursor:
        cursor.execute(f"""select status
                                from status 
                                where relax_service_id = (SELECT MAX(id) FROM relax_service WHERE child_account_id = '{id}') 
                                order by time desc;""")
        s = [i[0] for i in cursor.fetchall()]
        if status == -1 and '승인전' not in s:
                cursor.execute(f"""insert into status (status, time, relax_service_id)
                                    values ('승인전', NOW() ,(SELECT MAX(id) 
                                                            FROM relax_service
                                                            where finish = 0 and child_account_id = '{id}'));""")
        elif status == 0 and '픽업시작'not in s:
                cursor.execute(f"""insert into status (status, time, relax_service_id)
                                    values ('픽업시작', NOW() ,(SELECT MAX(id) 
                                                            FROM relax_service
                                                            where finish = 0 and child_account_id = '{id}'));""")
        elif status == 1 and '픽업완료'not in s:
                cursor.execute(f"""insert into status (status, time, relax_service_id)
                                    values ('픽업완료', NOW() ,(SELECT MAX(id) 
                                                            FROM relax_service
                                                            where finish = 0 and child_account_id = '{id}'));""")
        elif status == 2 and '병원호송완료'not in s:
                cursor.execute(f"""insert into status (status, time, relax_service_id)
                                    values ('병원호송완료', NOW() ,(SELECT MAX(id) 
                                                            FROM relax_service
                                                            where finish = 0 and child_account_id = '{id}'));""")
        elif status == 3 and '귀가완료'not in s:
                cursor.execute(f"""insert into status (status, time, relax_service_id)
                                    values ('귀가완료', NOW() ,(SELECT MAX(id) 
                                                            FROM relax_service
                                                            where finish = 0 and child_account_id = '{id}'));""")
        
        print(s)
        cursor.execute(f"""insert into real_time_location (time, status_status, status_relax_service_id, latitude, longitude)
                            values (NOW() , 
								(SELECT status 
                                FROM status 
                                WHERE time = (SELECT MAX(time) 
											FROM status 
                                            WHERE relax_service_id = (SELECT MAX(id) 
																	FROM relax_service 
                                                                    WHERE child_account_id = '{id}'))), 
								(SELECT MAX(id) FROM relax_service WHERE child_account_id = '{id}'), 
                                '{latitude}', 
                                '{longitude}');""")
        

        return JsonResponse({"success": True}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')


# 위치 api를 사용하기 위한 api (둘다 사용)
def daum_address(request):
    return render(request, 'main/daum_address.html')\
    
# 사용자가 결과 보는 api (사용자 전용)
def main_result(request): #get
    requestdata = json.loads(request.body)
    id = requestdata['id']

    with connection.cursor() as cursor:
        cursor.execute(f"""SELECT result
                       from relax_service
                       WHERE id = (select MAX(id)
                            from relax_service
                            where child_account_id = '{id}');""")
        
        result = cursor.fetchall()[0]

        return JsonResponse({"result": result[0]}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

# 서비스 사용 내역 확인 (사용자 전용)
@csrf_exempt
def service_history(request):
    account_id = request.GET.get('userId', '')
    with connection.cursor() as cursor:
        cursor.execute(f"""select distinct date(time), relax_service.hospital, relax_service.result
                            from relax_service
                            join status on status.relax_service_id = relax_service.id
                            where child_account_id = '{account_id}'
                            order by 1 desc ;""")
        result = {'service': [ {'date':str(i[0]), 'hospital':i[1], 'detail':i[2]} for i in cursor.fetchall()]}
    print(result)
    return JsonResponse(result, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

# 모든 계정 정보를 보기 위한 api (관리자 전용)
@csrf_exempt
def find_all_account(request):
    requestdata = json.loads(request.body)

    with connection.cursor() as cursor:
        cursor.execute(f"""SELECT id, name
                            FROM account
                            WHERE id != 'admin'
                            ORDER BY 1 ;""")
        
        result = cursor.fetchall()

        json_data = {"data": [{"id": id, "name": name} for id, name in result]}

        return JsonResponse(json_data, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

# 관리자가 서비스 신청한 사용자들을 보기 위한 api (관리자 전용)
@csrf_exempt
def get_apply_service_list(request):
    with connection.cursor() as cursor:
        cursor.execute(f"""select start_location, hospital, arrival_location, child_name, child.phone_number, child.resident_registration_number, real_time_personal_data, relax_service.child_account_id
                            from relax_service
                            join child on relax_service.child_name = child.name
                            where finish != 1 ;""")
# *          "start" : 출발 장소 이름,
# *          "middle" : 중간 장소 이름,
# *          "final" : 도착 장소 이름,
# *          "kidName": 아이 이름,
# *          "phoneNumber": 전화번호,
# *          "rrn": 주민등록번호,
# *          "status": 아이 상태
        result = [{'start':i[0], 'middle':i[1], 'final':i[2], 'kidName':i[3], 'phoneNumber':i[4], 'rrn':i[5], 'status':i[6], 'id' : i[7]} for i in cursor.fetchall()]
    print(result)


    return JsonResponse({'result' : result}, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')

@csrf_exempt
def search_users(request):
    requestdata = json.loads(request.body)

    id = requestdata['search_query']



    with connection.cursor() as cursor:
        cursor.execute(f"""SELECT id, name
                            FROM account
                            WHERE id = '{id}'""")
        
        
        result = cursor.fetchall()[0]

        json_data = {"id" : result[0], "name" : result[1]}

        return JsonResponse(json_data, json_dumps_params={'ensure_ascii': False}, content_type = 'application/json; charest=utf-8')
# # This is an auto-generated Django model module.
# # You'll have to do the following manually to clean this up:
# #   * Rearrange models' order
# #   * Make sure each model has one field with primary_key=True
# #   * Make sure each ForeignKey and OneToOneField has `on_delete` set to the desired behavior
# #   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# # Feel free to rename the models, but don't rename db_table values or field names.
# from django.db import models
# from django.contrib.auth.models import AbstractUser



# class Account(AbstractUser):
#     id = models.CharField(primary_key=True, max_length=45)
#     passward = models.CharField(max_length=45)
#     name = models.CharField(max_length=45)
#     zip_code = models.IntegerField()
#     family_relation_certificate = models.TextField(blank=True, null=True)
#     phone_number = models.IntegerField()
#     # password = None
#     # last_login = None
#     # is_superuser = None
#     # username = None

#     class Meta:
#         managed = False
#         db_table = 'account'
#         # app_label = 'Account'


# class Child(models.Model):
#     name = models.CharField(primary_key=True, max_length=45)
#     age = models.IntegerField()
#     resident_registration_number = models.IntegerField()
#     phone_number = models.IntegerField()
#     personal_data = models.CharField(max_length=45, blank=True, null=True)
#     approval = models.CharField(max_length=45)
#     account = models.ForeignKey(Account, models.DO_NOTHING)

#     class Meta:
#         managed = False
#         db_table = 'child'
#         unique_together = (('name', 'account'),)


# class Manager(models.Model):
#     id = models.CharField(primary_key=True, max_length=45)
#     passward = models.CharField(max_length=45)
#     phone_number = models.IntegerField()
#     name = models.CharField(max_length=45)
#     relax_service = models.ForeignKey('RelaxService', models.DO_NOTHING)

#     class Meta:
#         managed = False
#         db_table = 'manager'
#         unique_together = (('id', 'relax_service'),)


# class RelaxService(models.Model):
#     start_location = models.CharField(max_length=45)
#     arrival_location = models.CharField(max_length=45)
#     hospital = models.CharField(max_length=45, blank=True, null=True)
#     real_time_personal_data = models.CharField(max_length=45, blank=True, null=True)
#     result = models.CharField(max_length=45, blank=True, null=True)
#     payment = models.CharField(max_length=45, blank=True, null=True)
#     child_name = models.ForeignKey(Child, models.DO_NOTHING, related_name='relax_services_account')
#     child_account = models.ForeignKey(Child, models.DO_NOTHING, related_name='relax_services_name')

#     class Meta:
#         managed = False
#         db_table = 'relax_service'


# class Status(models.Model):
#     status = models.CharField(primary_key=True, max_length=45)
#     time = models.DateTimeField()
#     relax_service = models.ForeignKey(RelaxService, models.DO_NOTHING)

#     class Meta:
#         managed = False
#         db_table = 'status'
#         unique_together = (('status', 'relax_service'),)
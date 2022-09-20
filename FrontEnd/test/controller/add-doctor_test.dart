import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/add-doctor/controllers/add_doctor_controller.dart';
import 'package:nock/nock.dart';
import 'package:test/test.dart';

void main(){
  WidgetsFlutterBinding.ensureInitialized();
  group("Unit test for add doctor",
          (){
    late AddDoctorController controller;
    setUp((){
      Get.reset();
      controller = AddDoctorController();
    });

  test("Testing the doctor name is less than 4 characters and match the error hint",
          () {
        controller.nameChanged("tes");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.nameError.value, "Name should be at least 4");
      });

  test("Testing the doctor name is entered correctly",
          () {
        controller.nameChanged("test");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.nameError.value, null);
      });

  test("Testing the doctor email format on add doctor controller is valid",
          () {
        controller.emailChanged("bai2");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.emailError.value, "Please input a valid email");
      });
  test("Testing if the doctor email format without @",
          () {
        controller.emailChanged("bai2.com");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.emailError.value, "Please input a valid email");
      });
  test("Testing if the doctor email format enter correctly",
          () {
        controller.emailChanged("bai2@bai.com");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.emailError.value, null);
      });

  test("Testing if the doctor password on add doctor controller less than 8 characters",
  () {
    final controller = AddDoctorController();
    controller.passwordChanged("123456");
    try{
      controller.validate();
    }catch(_){}
    expect(controller.passwordError.value, "Password length must be 8-24 characters");
  });
  test("Testing if the doctor password on add doctor controller more than 24 characters",
          () {
        controller.passwordChanged("1234564324325453253252352323523");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.passwordError.value, "Password length must be 8-24 characters");
      });
  test("Testing if the doctor password on add doctor controller is working",
          () {
        controller.passwordChanged("12345678");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.passwordError.value, null);
      });

  test("Testing if the loading status on add doctor controller is initially false",
          () {
        expect(controller.isLoading.value, false);
      } );

  test("Test add correct doctor", () async{
    nock("http://10.0.2.2:9000/api/v1/admin/doctor")
        .post("/")
        .reply(201, null);
    controller.token = "sdsadssa";
    controller.nameChanged("testing");
    controller.emailChanged("testing@testing.com");
    controller.passwordChanged("12345678");
    try {
      controller.addDoctor();
    } catch (_) {}
    expect(controller.isLoading.value, true);
    await Future.delayed(Duration(seconds: 2));
    expect(controller.isLoading.value, false);
  });

  });
}
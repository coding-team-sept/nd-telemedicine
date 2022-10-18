import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:http_mock_adapter/http_mock_adapter.dart';
import 'package:nd/app/data/const.dart';
import 'package:nd/app/modules/add-doctor/controllers/add_doctor_controller.dart';
import 'package:nd/app/modules/add-doctor/views/add_doctor_view.dart';
import 'package:nd/app/routes/app_pages.dart';

void main() {
  group("Unit test for add doctor", () {
    late AddDoctorController controller;
    final dio = Dio();
    final dioAdapter = DioAdapter(dio: dio);
    dio.httpClientAdapter = dioAdapter;
    setUp(() {
      Get.reset();
      controller = AddDoctorController(dio: dio, token: "token");
    });

    test(
        "Testing the doctor name is less than 4 characters and match the error hint",
        () {
      controller.nameChanged("tes");
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.nameError.value, "Name should be at least 4");
    });

    test("Testing the doctor name is entered correctly", () {
      controller.nameChanged("test");
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.nameError.value, null);
    });

    test("Testing the doctor email format on add doctor controller is valid",
        () {
      controller.emailChanged("bai2");
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.emailError.value, "Please input a valid email");
    });
    test("Testing if the doctor email format without @", () {
      controller.emailChanged("bai2.com");
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.emailError.value, "Please input a valid email");
    });
    test("Testing if the doctor email format enter correctly", () {
      controller.emailChanged("bai2@bai.com");
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.emailError.value, null);
    });

    test(
        "Testing if the doctor password on add doctor controller less than 8 characters",
        () {
      final controller = AddDoctorController();
      controller.passwordChanged("123456");
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.passwordError.value,
          "Password length must be 8-24 characters");
    });
    test(
        "Testing if the doctor password on add doctor controller more than 24 characters",
        () {
      controller.passwordChanged("1234564324325453253252352323523");
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.passwordError.value,
          "Password length must be 8-24 characters");
    });
    test("Testing if the doctor password on add doctor controller is working",
        () {
      controller.passwordChanged("12345678");
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.passwordError.value, null);
    });

    test(
        "Testing if the loading status on add doctor controller is initially false",
        () {
      expect(controller.isLoading.value, false);
    });

    test('Get messages', () async {
      dioAdapter.onPost('${C.url}/app/admin/doctor', (server) {
        server.reply(
          201,
          {},
          delay: const Duration(seconds: 1),
          headers: {
            Headers.contentTypeHeader: [Headers.jsonContentType],
            "Authorization": ["Bearer token"]
          },
        );
      }, data: {
        'name': 'ravel',
        'email': 'raveltan@ravel.com',
        'password': 'raveltanjaya',
      });
      controller.name.value = "ravel";
      controller.email.value = "raveltan@ravel.com";
      controller.password.value = "raveltanjaya";
      expect(() async => await controller.addDoctor(), returnsNormally);
    });
  });
  group("UI test for add doctor", () {
    late AddDoctorController controller;
    late GetMaterialApp view;
    final dio = Dio();
    final dioAdapter = DioAdapter(dio: dio);

    setUp(() {
      dio.httpClientAdapter = dioAdapter;
      controller = AddDoctorController(dio: dio, token: "token");
      view = GetMaterialApp(
        home: const AddDoctorView(),
        getPages: AppPages.routes,
      );
      Get.reset();
      Get.testMode = true;
      Get.put(controller);
    });
    tearDown(() {
      Get.reset();
    });
    testWidgets("Test widgets completeness", (t) async {
      await t.pumpWidget(view);
      expect(find.byType(Stack), findsNWidgets(2));
      expect(find.byType(TextField), findsNWidgets(3));
      expect(find.byType(ElevatedButton), findsNWidgets(1));
    });
  });
}

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:http_mock_adapter/http_mock_adapter.dart';
import 'package:nd/app/modules/add-admin/controllers/add_admin_controller.dart';
import 'package:nd/app/modules/add-admin/views/add_admin_view.dart';
import 'package:nd/app/routes/app_pages.dart';
import 'package:nock/nock.dart';
import 'package:get/get.dart';

void main() {
  group("Unit test for add admin", () {
    late AddAdminController controller;
    setUp(() {
      Get.reset();
      controller = AddAdminController();
    });
    test('Testing if the add admin status is already loading ', () {
      final controller = AddAdminController();
      expect(controller.isLoading.value, false);
    });

    test('Testing name length is less 4', () {
      controller.nameChanged('ab4');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.nameError.value, 'Name should be at least 4');
    });

    test('Testing name authentication is correct', () {
      controller.nameChanged('ab49');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.nameError.value, null);
    });

    test('Testing name is empty', () {
      controller.nameChanged('');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.nameError.value, 'Name should be at least 4');
    });

    test('Testing email is empty', () {
      controller.emailChanged('');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.emailError.value, 'Please input a valid email');
    });

    test('Testing email authentication when I do not input xxx.com', () {
      controller.emailChanged('aaaa@');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.emailError.value, 'Please input a valid email');
    });

    test('Testing email authentication if correct when I do not input .com',
            () {
          controller.emailChanged('aaaa@dwe');
          try {
            controller.validate();
          } catch (_) {}
          expect(controller.emailError.value, 'Please input a valid email');
        });

    test('Testing input a empty email', () {
      controller.emailChanged('');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.emailError.value, 'Please input a valid email');
    });

    test('Testing input a valid email ', () {
      controller.emailChanged('aaaa@dwe.com');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.emailError.value, null);
    });

    test('Testing Password length < 8 characters ', () {
      controller.passwordChanged('1234567');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.passwordError.value,
          'Password length must 8-24 characters');
    });

    test('Testing Password length > 24 characters ', () {
      controller.passwordChanged('1234567891234567890000000');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.passwordError.value,
          'Password length must 8-24 characters');
    });

    test('Testing Password is empty ', () {
      controller.passwordChanged('');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.passwordError.value,
          'Password length must 8-24 characters');
    });

    test('Testing Password is correct ', () {
      controller.passwordChanged('wfwfwefefwefwefwfwe');
      try {
        controller.validate();
      } catch (_) {}
      expect(controller.passwordError.value, null);
    });
  });
  group("UI test for add admin", () {
    late AddAdminController controller;
    late GetMaterialApp view;
    final dio = Dio();
    final dioAdapter = DioAdapter(dio: dio);

    setUp(() {
      dio.httpClientAdapter = dioAdapter;
      controller = AddAdminController(dio: dio, token: "token");
      view = GetMaterialApp(
        home: const AddAdminView(),
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

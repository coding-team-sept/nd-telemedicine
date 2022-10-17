import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:http_mock_adapter/http_mock_adapter.dart';
import 'package:nd/app/data/const.dart';
import 'package:nd/app/modules/register/controllers/register_controller.dart';
import 'package:nd/app/routes/app_pages.dart';

void main() {
  group("Register Controller Unit Testing", () {
    late RegisterController controller;

    setUp(() {
      Get.reset();
      controller = RegisterController();
      Get.testMode = true;
    });

    tearDown(() {
      Get.reset();
    });

    test('Testing if the register status is already loading ', () {
      expect(controller.isLoading.value, false);
    });

    test('Testing full name authentication is correct', () {
      controller.onNameChange('ab');
      try {
        controller.signUp();
      } catch (_) {}
      expect(
          controller.fullNameError.value, 'Please input at least 3 characters');
    });

    test('Testing full name is empty', () {
      controller.onNameChange('');
      try {
        controller.signUp();
      } catch (_) {}
      expect(
          controller.fullNameError.value, 'Please input at least 3 characters');
    });

    test(
        'Testing email authentication if correct when I do not input @ and not xxx.com',
        () {
      controller.onEmailChange('aaaa');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.emailError.value, 'Please input a valid email');
    });

    test('Testing email authentication if correct when I do not input xxx.com',
        () {
      controller.onEmailChange('aaaa@');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.emailError.value, 'Please input a valid email');
    });

    test('Testing email authentication if correct when I do not input .com',
        () {
      controller.onEmailChange('aaaa@dwe');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.emailError.value, 'Please input a valid email');
    });

    test('Testing input a empty email', () {
      controller.onEmailChange('');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.emailError.value, 'Please input a valid email');
    });

    test('Testing input a valid email ', () {
      controller.onEmailChange('aaaa@dwe.com');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.emailError.value, '');
    });

    test('Testing Password length < 8 characters ', () {
      controller.onPasswordChange('1234567');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.passwordError.value,
          'Password length must 8-24 characters');
    });

    test('Testing Password length > 24 characters ', () {
      controller.onPasswordChange('1234567891234567890000000');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.passwordError.value,
          'Password length must 8-24 characters');
    });

    test('Testing Password is empty ', () {
      controller.onPasswordChange('');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.passwordError.value, 'Please input a non-empty value');
    });

    test(
        'Testing : 8< Password length <24 characters and confirm_password is same as password',
        () {
      controller.onPasswordChange('123456789123456789');
      controller.onConfirmPasswordChange('123456789123456789');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.passwordError.value, '');
    });

    test(
        'Testing : 8< Password length <24 characters But confirm_password is not same as password',
        () {
      controller.onPasswordChange('123456789123456789');
      controller.onConfirmPasswordChange('11111111');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.passwordError.value,
          'Please make sure password is the same');
    });

    test(
        'Testing : 8< Password length <24 characters But confirm_password is not same as password',
        () {
      controller.onPasswordChange('123456789123456789');
      controller.onConfirmPasswordChange('11111111');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.confirmPasswordError.value,
          'Please make sure password is the same');
    });

    test(
        'Testing : 8< Password length <24 characters But confirm_password is empty',
        () {
      controller.onPasswordChange('123456789123456789');
      controller.onConfirmPasswordChange('');
      try {
        controller.signUp();
      } catch (_) {}
      expect(controller.confirmPasswordError.value,
          'Please input a non-empty value');
    });
  });

  group("Widget testing", () {
    late RegisterController c;
    late GetMaterialApp v;
    final dio = Dio();
    final dioAdapter = DioAdapter(dio: dio);

    setUp(() {
      dio.httpClientAdapter = dioAdapter;
      c = RegisterController(dio: dio, testMode: true);
      v = GetMaterialApp(
        initialRoute: Routes.REGISTER,
        getPages: AppPages.routes,
      );
      Get.reset();
      Get.testMode = true;
      Get.put(c);
    });

    tearDown(() {
      Get.reset();
    });
    testWidgets("Testing if application show error if there is duplicate user",
        (t) async {
      dioAdapter.onPost("${C.url}/auth/register", (server) {
        server.reply(500, {"message": "Email has been taken"},
            delay: const Duration(seconds: 1));
      }, data: {
        "name": "Patient",
        "email": "patient@patient.com",
        "password": "patient123"
      });
      await t.pumpWidget(v);
      var textFields = find.byType(TextField);
      await t.enterText(textFields.at(0), "Patient");
      await t.enterText(textFields.at(1), "patient@patient.com");
      await t.enterText(textFields.at(2), "patient123");
      await t.enterText(textFields.at(3), "patient123");
      var registerButton = find.byType(ElevatedButton);
      await t.tap(registerButton);
      await t.pump(const Duration(milliseconds: 500));
      expect(c.isLoading.value, true);
      await t.pump(const Duration(seconds: 2));
      expect(c.isLoading.value, false);
      expect(Get.currentRoute, Routes.REGISTER);
      var errorDialog = find.byType(AlertDialog);
      expect(errorDialog, findsOneWidget);
      var errorDialogWidget =
          errorDialog.evaluate().elementAt(0).widget as AlertDialog;
      expect((errorDialogWidget.title as Text).data, "Error");
      expect((errorDialogWidget.content as Text).data, "Email has been taken");
    });

    testWidgets("Testing toggle password", (widgetTester) async {
      await widgetTester.pumpWidget(v);
      var showPasswordToggle = find.byType(IconButton);
      expect(showPasswordToggle, findsNWidgets(2));
      await widgetTester.tap(showPasswordToggle.at(0));
      await widgetTester.pump(const Duration(milliseconds: 500));
      expect(c.showPassword.value, false);
      await widgetTester.tap(showPasswordToggle.at(1));
      await widgetTester.pump(const Duration(milliseconds: 500));
      expect(c.showPassword.value, false);
    });


    testWidgets("Test if we can go to sign in page", (tester) async {
      await tester.pumpWidget(v);
      var signUpButton = find.byType(TextButton);
      expect(signUpButton, findsOneWidget);
      await tester.tap(signUpButton);
      await tester.pump(Duration(milliseconds: 500));
      expect(Get.currentRoute, Routes.LOGIN);
    });

    testWidgets("Testing if application can register data successfully",
        (t) async {
      dioAdapter.onPost("${C.url}/auth/register", (server) {
        server.reply(
            201,
            {
              "data": {
                "token": {
                  "access":
                      "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXRpZW50QHBhdGllbnQuY29tIiwicm9sZSI6IlJPTEVfUEFUSUVOVCIsImlkIjo1MywiZXhwIjoxNjk2MTcxNTUyLCJpYXQiOjE2NjQ3MjE5NTJ9.KhPVpMNoATNlXomuRZOsQcfgL1_uVGCDvXvwk3znHrk"
                }
              }
            },
            delay: const Duration(seconds: 1));
      }, data: {
        "name": "Patient",
        "email": "patient@patient.com",
        "password": "patient123"
      });
      await t.pumpWidget(v);
      var textFields = find.byType(TextField);
      await t.enterText(textFields.at(0), "Patient");
      await t.enterText(textFields.at(1), "patient@patient.com");
      await t.enterText(textFields.at(2), "patient123");
      await t.enterText(textFields.at(3), "patient123");
      var registerButton = find.byType(ElevatedButton);
      await t.tap(registerButton);
      await t.pump(const Duration(milliseconds: 500));
      expect(c.isLoading.value, true);
      await t.pump(const Duration(seconds: 2));
      expect(c.isLoading.value, false);
      expect(Get.currentRoute, Routes.HOME);
    });

    testWidgets(
        "Testing if the error is diplayed when the textfield is all empty",
        (t) async {
      await t.pumpWidget(v);
      expect(find.text("Welcome"), findsOneWidget);
      var textFields = find.byType(TextField);
      expect(textFields, findsNWidgets(4));
      var registerButton = find.byType(ElevatedButton);
      expect(registerButton, findsOneWidget);
      await t.tap(registerButton);
      await t.pump(const Duration(seconds: 1));
      expect(
          (textFields.evaluate().elementAt(0).widget as TextField)
                  .decoration
                  ?.errorText ??
              "",
          "Please input at least 3 characters");
      expect(
          (textFields.evaluate().elementAt(1).widget as TextField)
                  .decoration
                  ?.errorText ??
              "",
          "Please input a valid email");
      expect(
          (textFields.evaluate().elementAt(2).widget as TextField)
                  .decoration
                  ?.errorText ??
              "",
          "Please input a non-empty value");
      expect(
          (textFields.evaluate().elementAt(3).widget as TextField)
                  .decoration
                  ?.errorText ??
              "",
          "Please input a non-empty value");
      var dialog = find.byType(AlertDialog);
      expect(dialog, findsOneWidget);
      var dialogWidget = dialog.evaluate().elementAt(0).widget as AlertDialog;
      expect((dialogWidget.title as Text).data ?? "", "Sign Up Failed");
      expect((dialogWidget.content as Text).data ?? "",
          "Please check your credentials");
      Get.back();
      await t.pump();
      dialog = find.byType(AlertDialog);
      expect(dialog, findsNothing);
    });
  });
}

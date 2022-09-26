import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/register/controllers/register_controller.dart';
import 'package:nd/app/modules/register/views/register_view.dart';
import 'package:nock/nock.dart';

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

    //test("Test successful login", () async {
    //WidgetsFlutterBinding.ensureInitialized();
    //nock('http://10.0.2.2:9000/api/v1/register').post('/').reply(201, {
    //'data': {
    //'token': {'token': 'fasfsafsdafasfas'}
    //}
    //});
    //controller.onNameChange("raveltan");
    //controller.onEmailChange("ravel@ravel.com");
    //controller.onPasswordChange("jokojokojoko");
    //controller.onConfirmPasswordChange("jokojokojoko");
    //try {
    //controller.signUp();
    //} catch (_) {}
    //await Future.delayed(const Duration(milliseconds: 500));
    //expect(controller.isLoading.value, true);
    //});
  });
  group("Widget testing", () {
    late RegisterController c;
    late GetMaterialApp v;

    setUpAll(nock.init);

    setUp(() {
      nock.cleanAll();
      c = RegisterController();
      v = const GetMaterialApp(
        home: RegisterView(),
      );
      Get.reset();
      Get.testMode = true;
      Get.put(c);
    });

    tearDown(() {
      Get.reset();
    });

    testWidgets("Testing if application can register data successfully",
        (t) async {
      nock('http://10.0.2.2:9000/api/v1/register').post('/').reply(201, null);
      await t.pumpWidget(v);
      var textFields = find.byType(TextField);
      await t.enterText(textFields.at(0), "budi budi budi");
      await t.enterText(textFields.at(1), "butdi@fads.com");
      await t.enterText(textFields.at(2), "fsdfsadfsd@fads.com");
      await t.enterText(textFields.at(3), "fsdfsadfsd@fads.com");
      var registerButton = find.byType(ElevatedButton);
      await t.tap(registerButton);
      await t.pump(const Duration(milliseconds: 500));
      expect(c.isLoading.value, true);
      await t.pump(const Duration(seconds: 3));
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

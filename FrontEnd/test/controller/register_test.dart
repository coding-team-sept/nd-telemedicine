import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/register/controllers/register_controller.dart';
import 'package:nock/nock.dart';
import 'package:test/test.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  group("Register Controller Unit Testing", () {
    late RegisterController controller;

    setUp(() {
      Get.reset();
      controller = RegisterController();
      Get.testMode = true;
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

    test("Test successful login", () async {
      nock('http://10.0.2.2:9000/api/v1/register').post('/').reply(201, {
        'data': {
          'token': {'token': 'fasfsafsdafasfas'}
        }
      });
      controller.onNameChange("raveltan");
      controller.onEmailChange("ravel@ravel.com");
      controller.onPasswordChange("jokojokojoko");
      controller.onConfirmPasswordChange("jokojokojoko");
      controller.signUp();
      await Future.delayed(const Duration(milliseconds: 500));
      expect(controller.isLoading.value, true);
    });
  });
}

import 'package:flutter/material.dart';
import 'package:nd/app/modules/add-admin/controllers/add_admin_controller.dart';
import 'package:nock/nock.dart';
import 'package:test/test.dart';
import 'package:get/get.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
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

    test('Test add correct admin', () async {
      nock('http://10.0.2.2:9000/api/v1/admin/admin')
          .post('/')
          .reply(201, null);
      controller.token = "fasfsafsa";
      controller.emailChanged("fsafs@fadsfs.com");
      controller.nameChanged("fadsfs");
      controller.passwordChanged("fsafdsfasfs");
      try {
        controller.addAdmin();
      } catch (_) {}
      expect(controller.isLoading.value, true);
      await Future.delayed(Duration(seconds: 2));
      expect(controller.isLoading.value, false);
    });
  });
}

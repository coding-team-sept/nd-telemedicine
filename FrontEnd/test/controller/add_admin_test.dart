import 'package:nd/app/modules/add-admin/controllers/add_admin_controller.dart';
import 'package:test/test.dart';

void main() {
  test('Testing if the add admin status is already loading ', () {
    final controller = AddAdminController();
    expect(controller.isLoading.value, false);
  });

  test('Testing name length is less 4', () {
    final controller = AddAdminController();
    controller.nameChanged('ab4');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.nameError.value, 'Name should be at least 4');
  });

  test('Testing name authentication is correct', () {
    final controller = AddAdminController();
    controller.nameChanged('ab49');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.nameError.value, null);
  });

  test('Testing name is empty', () {
    final controller = AddAdminController();
    controller.nameChanged('');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.nameError.value, 'Name should be at least 4');
  });

  test('Testing email is empty', () {
    final controller = AddAdminController();
    controller.emailChanged('');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.emailError.value, 'Please input a valid email');
  });

  test('Testing email authentication when I do not input xxx.com', () {
    final controller = AddAdminController();
    controller.emailChanged('aaaa@');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.emailError.value, 'Please input a valid email');
  });

  test('Testing email authentication if correct when I do not input .com', () {
    final controller = AddAdminController();
    controller.emailChanged('aaaa@dwe');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.emailError.value, 'Please input a valid email');
  });

  test('Testing input a empty email', () {
    final controller = AddAdminController();
    controller.emailChanged('');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.emailError.value, 'Please input a valid email');
  });

  test('Testing input a valid email ', () {
    final controller = AddAdminController();
    controller.emailChanged('aaaa@dwe.com');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.emailError.value, null);
  });

  test('Testing Password length < 8 characters ', () {
    final controller = AddAdminController();
    controller.passwordChanged('1234567');
    try {
      controller.validate();
    } catch (_) {}
    expect(
        controller.passwordError.value, 'Password length must 8-24 characters');
  });

  test('Testing Password length > 24 characters ', () {
    final controller = AddAdminController();
    controller.passwordChanged('1234567891234567890000000');
    try {
      controller.validate();
    } catch (_) {}
    expect(
        controller.passwordError.value, 'Password length must 8-24 characters');
  });

  test('Testing Password is empty ', () {
    final controller = AddAdminController();
    controller.passwordChanged('');
    try {
      controller.validate();
    } catch (_) {}
    expect(
        controller.passwordError.value, 'Password length must 8-24 characters');
  });

  test('Testing Password is correct ', () {
    final controller = AddAdminController();
    controller.passwordChanged('wfwfwefefwefwefwfwe');
    try {
      controller.validate();
    } catch (_) {}
    expect(controller.passwordError.value, null);
  });
}

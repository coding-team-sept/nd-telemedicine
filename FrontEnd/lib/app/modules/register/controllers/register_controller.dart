import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/data/const.dart';
import 'package:nd/app/routes/app_pages.dart';

class RegisterController extends GetxController {
  void login() async {
    Get.offNamedUntil(Routes.LOGIN, (route) => false);
  }

  final fullName = "".obs;
  final email = "".obs;
  final password = "".obs;
  final confirmPassword = "".obs;

  final fullNameError = "".obs;
  final emailError = "".obs;
  final passwordError = "".obs;
  final confirmPasswordError = "".obs;

  final showPassword = true.obs;
  final showConfirmPassword = true.obs;

  final isLoading = false.obs;
  final bool testMode;
  final Dio dio;

  RegisterController({bool? testMode, Dio? dio}) : testMode= testMode ?? false, dio = dio ?? Dio();

  void toggleShowPassword() {
    showPassword.value = !showPassword.value;
  }

  void toggleShowConfirmPassword() {
    showConfirmPassword.value = !showConfirmPassword.value;
  }

  void finishSignUp() async {
    isLoading.value = true;
    await Future.delayed(const Duration(seconds: 1));
    //send request to server

    try {
      var response = await dio.post('${C.url}/auth/register', data: {
        'name': fullName.value,
        'email': email.value,
        'password': password.value,
      });
      isLoading.value = false;
      if (response.statusCode == 201) {
        if(!testMode){
          const storage = FlutterSecureStorage();
          await storage.write(
              key: 'token', value: response.data['data']['token']['access']);
          await storage.write(key: 'role', value: 'patient');
          await storage.write(key: 'email', value: email.value);
        }
        Get.offNamedUntil(Routes.HOME, (route) => false);
      } else {
        Get.dialog(AlertDialog(
          title: const Text('Error'),
          content: Text(response.data['message']),
        ));
      }
      //this is from website DIO Flutter, Copy that, but not found dio so we Make a new Dio() and import.
    } on DioError catch (e, _) {
      isLoading.value = false;
      Get.dialog(AlertDialog(
        title: const Text('Error'),
        content: Text(e.response?.data['message'] ?? 'Unknown error'),
      ));
    }
  }

  void onNameChange(String newName) => fullName.value = newName;

  void onPasswordChange(String newName) => password.value = newName;

  void onConfirmPasswordChange(String newName) =>
      confirmPassword.value = newName;

  void onEmailChange(String newName) => email.value = newName;

  void signUp() {
    fullNameError.value = "";
    emailError.value = "";
    passwordError.value = "";
    confirmPasswordError.value = "";

    bool success = true;

    if (fullName.value.length < 3) {
      fullNameError.value = "Please input at least 3 characters";
      success = false;
    }

    if (password.value != confirmPassword.value) {
      passwordError.value = "Please make sure password is the same";
      confirmPasswordError.value = "Please make sure password is the same";
      success = false;
    }

    bool emailValid = RegExp(
            r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
        .hasMatch(email.value);
    if (!emailValid) {
      emailError.value = "Please input a valid email";
      success = false;
    }

    if (password.value.length < 8 || password.value.length > 24) {
      passwordError.value = "Password length must 8-24 characters";
      success = false;
    }

    if (password.isEmpty) {
      passwordError.value = "Please input a non-empty value";
      success = false;
    }

    if (confirmPassword.isEmpty) {
      confirmPasswordError.value = "Please input a non-empty value";
      success = false;
    }

    if (!success) {
      Get.dialog(const AlertDialog(
        title: Text("Sign Up Failed"),
        content: Text("Please check your credentials"),
      ));
      return;
    }

    finishSignUp();
  }
}

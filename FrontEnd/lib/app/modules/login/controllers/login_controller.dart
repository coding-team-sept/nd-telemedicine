import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/data/const.dart';

import '../../../routes/app_pages.dart';

class LoginController extends GetxController {
  final email = "".obs;
  final password = "".obs;

  final emailError = "".obs;
  final passwordError = "".obs;

  final showPassword = true.obs;

  final Dio dio;

  final bool testMode;
  //Allowing data injection for testing
  LoginController({Dio? dio, bool? testMode}) : dio = dio ?? Dio(), testMode = testMode ?? false;

  final isLoading = false.obs;

  void toggleShowPassword() {
    showPassword.value = !showPassword.value;
  }

  void onEmailChange(String newEmail) => email.value = newEmail;

  void onPasswordChange(String newPassword) => password.value = newPassword;

  void doSignUp() async {
    isLoading.value = true;
    await Future.delayed(const Duration(seconds: 1));

    //send request to server
    try {
      var response = await dio.post('${C.url}/auth/login', data: {
        'email': email.value,
        'password': password.value,
      });
      isLoading.value = false;
      if (response.statusCode == 200) {
        if(!testMode){
          var storage = const FlutterSecureStorage();
          await storage.write(
              key: 'token', value: response.data["data"]["token"]["access"]);
          await storage.write(
              key: 'role', value: response.data["data"]["user"]["role"]);
          await storage.write(key: 'email', value: email.value);
        }
        Get.offNamedUntil(Routes.HOME, (route) => false);
      } else {
        Get.dialog(AlertDialog(
          title: const Text("Error"),
          content: Text(response.data['message']),
        ));
      }
    } on DioError catch (e, _) {
      isLoading.value = false;
      Get.dialog(AlertDialog(
        title: Text("Error"),
        content: Text(e.response?.data['message'] ?? "Unknown error!"),
      ));
    }
  }

  void login() {
    bool success = true;
    emailError.value = "";
    passwordError.value = "";

    bool emailValid = RegExp(
            r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
        .hasMatch(email.value);
    if (!emailValid) {
      success = false;
      emailError.value = "Please enter a valid email";
    }

    if (password.value.isEmpty) {
      success = false;
      passwordError.value = "Password cannot be empty";
    }
    if (password.value.length < 8 || password.value.length > 24) {
      success = false;
      passwordError.value = "Password should be 8 to 24 characters";
    }

    if (!success) {
      Get.dialog(const AlertDialog(
        title: Text("Sign In Failed"),
        content: Text("Please check your email and password"),
      ));
      return;
    }
    doSignUp();
  }

  void signUp() {
    Get.offNamedUntil(Routes.REGISTER, (r) => false);
  }
}

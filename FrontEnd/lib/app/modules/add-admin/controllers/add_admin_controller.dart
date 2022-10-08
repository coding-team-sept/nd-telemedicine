import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';

class AddAdminController extends GetxController {
  final name = ''.obs;
  final email = ''.obs;
  final password = ''.obs;

  final nameError = Rx<String?>(null);
  final emailError = Rx<String?>(null);
  final passwordError = Rx<String?>(null);

  final isLoading = false.obs;

  late final token;

  @override
  void onInit() async {
    token = await const FlutterSecureStorage().read(key: "token");
    super.onInit();
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }

  void nameChanged(String n) => name.value = n;

  void emailChanged(String n) => email.value = n;

  void passwordChanged(String n) => password.value = n;

  bool validate() {
    bool valid = true;

    if (name.value.length < 4) {
      nameError.value = "Name should be at least 4";
      valid = false;
    } else {
      nameError.value = null;
    }
    if (password.value.length < 8 || password.value.length > 24) {
      passwordError.value = "Password length must 8-24 characters";
      valid = false;
    } else {
      passwordError.value = null;
    }
    bool emailValid = RegExp(
            r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+")
        .hasMatch(email.value);
    if (!emailValid) {
      emailError.value = "Please input a valid email";
      valid = false;
    } else {
      emailError.value = null;
    }
    return valid;
  }

  void addAdmin() async {
    if (!validate()) return;
    isLoading.value = true;
    await Future.delayed(const Duration(seconds: 1));
    isLoading.value = false;

    var url = 'http://95.111.217.168:9000/api/v1';

    try {
      var response = await Dio().post('$url/app/admin/admin',
          data: {
            'name': name.value,
            'email': email.value,
            'password': password.value,
          },
          options: Options(headers: {"Authorization": "Bearer $token"}));
      isLoading.value = false;
      if (response.statusCode == 201) {
        Get.back();
      } else {
        Get.dialog(AlertDialog(
          title: const Text('Error'),
          content: Text(response.data['error']['message']),
        ));
      }
      //this is from website DIO Flutter, Copy that, but not found dio so we Make a new Dio() and import.
    } on DioError catch (e, _) {
      print(e);
      isLoading.value = false;
      if (e.response?.statusCode == 401) {
        Get.dialog(const AlertDialog(
          title: Text('Error'),
          content: Text('Unauthorized'),
        ));
      }
      Get.dialog(AlertDialog(
        title: const Text('Error'),
        content: Text(e.response?.data['error']['message'] ?? 'Unknown error'),
      ));
    }
  }
}

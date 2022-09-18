import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';


class AddDoctorController extends GetxController {
  final name = ''.obs;
  final email = ''.obs;
  final password = ''.obs;

  final nameError = Rx<String?>(null);
  final emailError = Rx<String?>(null);
  final passwordError = Rx<String?>(null);

  final isLoading = false.obs;

  @override
  void onInit() {
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

  bool validate(){
    bool valid = true;

    if (name.value.length < 4){
      nameError.value = "Name should be at least 4";
      valid = false;
    }else{
      nameError.value = null;
    }

    if (password.value.length < 8 || password.value.length > 24){
      passwordError.value = "Password length must be 8-24 characters";
      valid = false;
    }else{
      passwordError.value = null;
    }

    bool emailValid = RegExp(r"^[a-zA-Z0-9.a-zA-Z0-9.!#$%&'*+-/=?^_`{|}~]+@[a-zA-Z0-9]+\.[a-zA-Z]+").hasMatch(email.value);
    if (!emailValid){
      emailError.value = "Please input a valid email";
      valid = false;
    } else{
      emailError.value = null;
    }
    return valid;
  }

  void addDoctor() async{
    if(!validate()) return;
    isLoading.value = true;
    await Future.delayed(const Duration(seconds: 1));
    isLoading.value = false;

    var url = 'http://10.0.2.2:9000/api/v1';

    try {
      var response = await Dio().post('$url/admin/doctor', data: {
        'name': name.value,
        'email': email.value,
        'password': password.value,
      });
      isLoading.value = false;
      if (response.statusCode == 201){
        Get.back();
      } else {
        Get.dialog(AlertDialog(
          title: const Text('Error'),
          content: Text(response.data['error']['message']),
        ));
      }
      //this is from website DIO Flutter, Copy that, but not found dio so we Make a new Dio() and import.
    } on DioError catch (e, _) {
      isLoading.value = false;
      Get.dialog(AlertDialog(
        title: const Text('Error'),
        content: Text(e.response?.data['error']['message'] ?? 'Unknown data'),
      ));
    }
  }
}

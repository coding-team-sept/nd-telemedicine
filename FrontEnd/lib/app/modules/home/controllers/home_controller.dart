import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/routes/app_pages.dart';

class HomeController extends GetxController {
  //TODO: Implement HomeController

  final storage = const FlutterSecureStorage();

  @override
  void onInit() async {
    String? value = await storage.read(key: "token");
    if (value == null || value==""){
      Get.offNamedUntil(Routes.LOGIN, (route) => false);
    }


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

}

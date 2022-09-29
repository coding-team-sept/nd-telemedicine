import 'package:flutter/material.dart';
import 'package:get/get.dart';

class OfflineDoctorBookingController extends GetxController {



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

void showDetail(){
  Get.dialog(const AlertDialog(
    title: Text("Information"),
    content: Text("Please go to the location directly"),
  ));
}
}

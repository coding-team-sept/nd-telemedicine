import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/routes/app_pages.dart';

import '../model/appointment_model.dart';

class AppointmentController extends GetxController {
  final isLoading = false.obs;
  RxList<AppointmentModel> appointmentData = <AppointmentModel>[].obs;
  late String token;

  @override
  void onInit() async {
    token = await const FlutterSecureStorage().read(key: "token") ?? '';
    super.onInit();
  }

  @override
  void onReady() async {
    getAppointment();
  }

  void getAppointment() async {
    isLoading.value = true;
    // Get doctors list from server
    const url = 'http://10.0.2.2:9001/api/v1';
    try {
      final response = await Dio().get('$url/app/patient/appointment',
          options: Options(headers: {"Authorization": "Bearer $token"}));
      appointmentData.clear();
      for (var element in (response.data["data"] as List)) {
        print(element);
        appointmentData.add(AppointmentModel(
            id: element['id'],
            name: element['doctor']['name'],
            time: element['datetime'],
            isOnline: element['session'] == "ONLINE"));
      }

      print(response.data);
    } on DioError catch (e, s) {
      print(s);
      print(e.error);
    }

    isLoading.value = false;
  }

  void newAppointment() => Get.toNamed(Routes.CREATE_APPOINTMENT);

  void showAppointmentDetail(int id, bool isOnline) {
    if (!isOnline) {
      Get.dialog(const AlertDialog(
        title: Text("Booking is Offline"),
        content: Text("Please go to the clinic and meet the doctor directly"),
      ));

      return;
    }
    Get.toNamed(Routes.CHAT);
  }
}

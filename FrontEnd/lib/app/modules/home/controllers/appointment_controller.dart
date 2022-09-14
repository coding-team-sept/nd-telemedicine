import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:nd/app/routes/app_pages.dart';

import '../model/appointment_model.dart';

class AppointmentController extends GetxController {
  final isLoading = false.obs;
  RxList<AppointmentModel> appointmentData = <AppointmentModel>[].obs;
  @override
  void onReady() async {
    getAppointment();
  }

  void getAppointment() async {
    isLoading.value = true;
    // Get doctors list from server
    const url = 'http://10.0.2.2:9001/api/v1';
    const token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXRpZW50QHBhdGllbnQuY29tIiwicm9sZSI6IlJPTEVfUEFUSUVOVCIsImlkIjoyLCJleHAiOjE2NjI0NDgwNDIsImlhdCI6MTY2MjM2MTY0Mn0.qG_a0Y-sYd1HkmvhHC-sT0nW5EWFA3gWnXIEsheE09E";
    try {
      final response = await Dio().get('$url/patient/appointment',
          options: Options(headers: {"Authorization": "Bearer $token"}));
      appointmentData.clear();
      for (var element in (response.data as List)) {
        appointmentData.add(AppointmentModel(
            id: element['id'],
            name: element['doctor']['name'],
            time: element['datetime'],
            isOnline: true));
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
    if (isOnline) {
      Get.dialog(const AlertDialog(
        title: Text("Booking is Offline"),
        content: Text("Please go to the clinic and meet the doctor directly"),
      ));

      return;
    }
    Get.dialog(AlertDialog(
      title: const Text("Not implemented"),
      content: Text(id.toString()),
    ));
  }
}

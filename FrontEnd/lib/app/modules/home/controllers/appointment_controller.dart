import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/data/const.dart';
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

  //function for get the appointments
  void getAppointment() async {
    isLoading.value = true;
    // Get doctors list from server
    try {
      final response = await Dio().get('${C.urlA}/app/patient/appointment',
          options: Options(headers: {"Authorization": "Bearer $token"}));
      appointmentData.clear();
      for (var element in (response.data["data"] as List)) {
        appointmentData.add(AppointmentModel(
            id: element['id'],
            doctorID: element['doctor']['id'],
            name: element['doctor']['name'],
            time: element['datetime'],
            isOnline: element['session'] == "ONLINE"));
      }
    } on DioError catch (e, _) {
      Get.dialog(const AlertDialog(
        title: Text("Unknown Error"),
      ));
    }

    isLoading.value = false;
  }

  //run this function will go to create appointment page
  void newAppointment() => Get.toNamed(Routes.CREATE_APPOINTMENT);

  //if the patient click offline will shown message through AlertDialog
  void showAppointmentDetail(int id, bool isOnline, int doctorID) {
    if (!isOnline) {
      Get.dialog(const AlertDialog(
        title: Text("Booking is Offline"),
        content: Text("Please go to the clinic and meet the doctor directly"),
      ));

      return;
    }
    //this function will navigate the chat page when patient clicks online booking
    Get.toNamed(Routes.CHAT, arguments: {
      "isDoctor": false,
      "appointmentID": id,
      "token": token,
      "doctorID": doctorID,
      "patientID": -999
    });
  }
}

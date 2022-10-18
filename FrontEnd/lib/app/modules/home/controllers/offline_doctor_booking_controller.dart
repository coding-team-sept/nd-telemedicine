import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/data/const.dart';

import '../model/offline_model.dart';

class OfflineDoctorBookingController extends GetxController {
  final isLoading = false.obs;
  RxList<OfflinePatientAppointmentModel> offlineData =
      <OfflinePatientAppointmentModel>[].obs;
  late String token;

  @override
  void onInit() async {
    token = await const FlutterSecureStorage().read(key: "token") ?? '';
    getOfflineAppointment();
    super.onInit();
  }

  //function to get the offline booking
  void getOfflineAppointment() async {
    isLoading.value = true;
    // Get doctors list from server
    try {
      final response = await Dio().get('${C.urlA}/app/doctor/appointment',
          options: Options(headers: {"Authorization": "Bearer $token"}));
      offlineData.clear();
      for (var element in (response.data["data"] as List)) {
        if (element['session'] == "OFFLINE") {
          //determine the patient is offline booking
          offlineData.add(OfflinePatientAppointmentModel(
            id: element['id'],
            datetime: element['datetime'],
            patientName: element['patient']['name'],
            patientId: element['patient']['id'],
            patientEmail: element['patient']['email'],
          ));
        }
      }
    } on DioError catch (e) {
      Get.dialog(const AlertDialog(
        title: Text("Unknown Error"),
      ));
    }

    isLoading.value = false;
  }

  //function to show the detail for offline patient
  void showPatientDetail(int id) {
    Get.dialog(const AlertDialog(
      title: Text("Booking is Offline"),
      content: Text("Please go to the clinic and meet the doctor directly"),
    ));
  }
}

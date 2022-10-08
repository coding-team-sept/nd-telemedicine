import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';

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

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }

  void getOfflineAppointment() async {
    isLoading.value = true;
    // Get doctors list from server
    const url = 'http://95.111.217.168:9001/api/v1';
    try {
      final response = await Dio().get('$url/app/doctor/appointment',
          options: Options(headers: {"Authorization": "Bearer $token"}));
      offlineData.clear();
      for (var element in (response.data["data"] as List)) {
        print(element);
        if (element['session'] == "OFFLINE") {
          offlineData.add(OfflinePatientAppointmentModel(
            id: element['id'],
            datetime: element['datetime'],
            patientName: element['patient']['name'],
            patientId: element['patient']['id'],
            patientEmail: element['patient']['email'],
          ));
        }
      }

      print(response.data);
    } on DioError catch (e, s) {
      print(s);
      print(e.error);
    }

    isLoading.value = false;
  }

  void showPatientDetail(int id) {
    Get.dialog(const AlertDialog(
      title: Text("Booking is Offline"),
      content: Text("Please go to the clinic and meet the doctor directly"),
    ));
  }
}

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:nd/app/modules/create_appointment/model/doctor_model.dart';

class CreateAppointmentController extends GetxController {
  RxList<DoctorModel> doctorData = <DoctorModel>[].obs;

  final date = DateTime.now().obs;
  final time = TimeOfDay.now().obs;
  final isLoading = false.obs;

  String get formattedDate => DateFormat.yMMMMd().format(date.value);
  String get formattedTime => "${time.value.hour}:${time.value.minute}";
  void selectDate() async {
    date.value = await showDatePicker(
            context: Get.context!,
            initialDate: date.value,
            firstDate: DateTime.now(),
            lastDate: DateTime.now().add(const Duration(days: 365))) ??
        date.value;
    getDoctors();
  }

  void selectTime() async {
    time.value =
        await showTimePicker(context: Get.context!, initialTime: time.value) ??
            time.value;
    getDoctors();
  }

  void getDoctors() async {
    isLoading.value = true;
    // Get doctors list from server
    const url = 'http://10.0.2.2:9001/api/v1';
    const token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXRpZW50QHBhdGllbnQuY29tIiwicm9sZSI6IlJPTEVfUEFUSUVOVCIsImlkIjoyLCJleHAiOjE2NjI0NDgwNDIsImlhdCI6MTY2MjM2MTY0Mn0.qG_a0Y-sYd1HkmvhHC-sT0nW5EWFA3gWnXIEsheE09E";
    final timestamp =
        '${date.value.year}-${date.value.month}-${date.value.day}_${time.value.hour}:${time.value.minute}';
    try {
      final response = await Dio().get('$url/patient/doctor/$timestamp',
          options: Options(headers: {"Authorization": "Bearer $token"}));
      doctorData.clear();
      for (var element in (response.data as List)) {
        doctorData.add(DoctorModel(name: element['name'], id: element['id']));
      }
      print(response.data);
    } on DioError catch (e, s) {
      print(s);
      print(e.error);
      Get.back();
    }

    isLoading.value = false;
  }

  void doBooking(int id) {
    Get.dialog(SimpleDialog(
      title: const Text("Choose Location"),
      children: [
        SimpleDialogOption(
          child: const Text("Offline"),
          onPressed: () => doOfflineBooking(id),
        ),
        //SimpleDialogOption(
        //child: const Text("Online"),
        //onPressed: () => doOnlineBooking(id),
        //),
      ],
    ));
  }

  void doOnlineBooking(int id) async {
    Get.back();
  }

  void doOfflineBooking(int id) async {
    Get.back();
    isLoading.value = true;
    print("booking");
    // Get doctors list from server
    const url = 'http://10.0.2.2:9001/api/v1';
    const token =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXRpZW50QHBhdGllbnQuY29tIiwicm9sZSI6IlJPTEVfUEFUSUVOVCIsImlkIjoyLCJleHAiOjE2NjI0NDgwNDIsImlhdCI6MTY2MjM2MTY0Mn0.qG_a0Y-sYd1HkmvhHC-sT0nW5EWFA3gWnXIEsheE09E";
    final timestamp =
        '${date.value.year}-${date.value.month}-${date.value.day}_${time.value.hour}:${time.value.minute}';
    try {
      final response = await Dio().post('$url/patient/appointment',
          data: {'doctorId': id, 'datetime': timestamp},
          options: Options(headers: {"Authorization": "Bearer $token"}));
      Get.back();
    } on DioError catch (e, s) {
      print(s);
      print(e.error);
      Get.dialog(AlertDialog(
        title: const Text("Error"),
        content: Text(e.response?.data ?? "Unknown error"),
      ));
    } catch (e) {
      print(e);
      Get.dialog(const AlertDialog(
        title: Text("Error"),
        content: Text("Unknown Error"),
      ));
    }

    isLoading.value = false;
  }
}

import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:intl/intl.dart';
import 'package:nd/app/data/const.dart';
import 'package:nd/app/modules/create_appointment/model/doctor_model.dart';

class CreateAppointmentController extends GetxController {
  RxList<DoctorModel> doctorData = <DoctorModel>[].obs;
  late String token;
  final bool debug;

  @override
  void onInit() async {
    //for testing purposes
    if (debug) {
      token = await const FlutterSecureStorage().read(key: "token") ?? 'token';
    } else {
      token = "";
    }
    super.onInit();
  }

  final Dio dio;

  //for testing purposes
  CreateAppointmentController({Dio? dio})
      : dio = dio ?? Dio(),
        debug = dio == null ? true : false;

  final date = DateTime.now().obs;
  final time = TimeOfDay.now().obs;
  final isLoading = false.obs;
  var todayDate = DateTime.now();

  //Standard format of Date
  String get formattedDate => DateFormat.yMMMMd().format(date.value);
  //Standard format of Time
  String get formattedTime => "${time.value.hour}:${time.value.minute}";

  void selectDate() async {
    date.value = await showDatePicker(
            context: Get.context!,
            initialDate: todayDate,
            firstDate: todayDate,
            lastDate: todayDate.add(const Duration(days: 365))) ??
        date.value;
    getDoctors();
  }

  void selectTime() async {
    time.value =
        await showTimePicker(context: Get.context!, initialTime: time.value) ??
            time.value;
    getDoctors();
  }

  Future getDoctors() async {
    isLoading.value = true;
    // Get doctors list from server
    final timestamp =
        '${date.value.year}-${date.value.month}-${date.value.day}_${time.value.hour}:${time.value.minute}';
    try {
      final response = await dio.get('${C.urlA}/app/patient/doctor/$timestamp',
          options: Options(headers: {"Authorization": "Bearer $token"}));
      doctorData.clear();
      for (var element in (response.data['data'] as List)) {
        doctorData.add(DoctorModel(name: element['name'], id: element['id']));
      }
    } on DioError catch (e, _) {
      if ((e.response?.statusCode ?? 500) == 400) {
        var error = e.response?.data["message"] ?? "";
        //show this error when the user select the doctor's time before 10am or after 8pm, which is not working time
        if (error == "Invalid datetime: Out of operation time") {
          Get.dialog(const AlertDialog(
              title: Text("Error"), content: Text("Out of operation time")));
        } //show this error when the user select the past date
        else if (error == "Invalid datetime: Datetime in the past") {
          Get.dialog(const AlertDialog(
              title: Text("Error"),
              content: Text("Please select a time in the future")));
        }
        //otherwise, if the status code is 404, unknown error will be shown
      } else if (e.response?.statusCode != 404) {
        Get.dialog(const AlertDialog(
            title: Text("Error"), content: Text("Unknown Error")));
      }
    }
    isLoading.value = false;
  }

  //show choice dialog for booking function to choose offline and online
  void doBooking(int id) {
    Get.dialog(SimpleDialog(
      title: const Text("Choose Location"),
      children: [
        SimpleDialogOption(
          child: const Text("Offline"),
          onPressed: () => doOfflineBooking(id),
        ),
        SimpleDialogOption(
          child: const Text("Online"),
          onPressed: () => doOnlineBooking(id),
        ),
      ],
    ));
  }

  //run when the patient select online booking
  void doOnlineBooking(int id) async {
    Get.back();
    isLoading.value = true;
    // Get doctors list from server
    final timestamp =
        '${date.value.year}-${date.value.month}-${date.value.day}_${time.value.hour}:${time.value.minute}';
    try {
      final response = await dio.post('${C.urlA}/app/patient/appointment',
          data: {'doctorId': id, 'datetime': timestamp, 'session': "ONLINE"},
          options: Options(headers: {"Authorization": "Bearer $token"}));
      Get.back();
    } on DioError catch (e, s) {
      Get.dialog(AlertDialog(
        title: const Text("Error"),
        content: Text(e.response?.data ?? "Unknown error"),
      ));
    } catch (e) {
      Get.dialog(const AlertDialog(
        title: Text("Error"),
        content: Text("Unknown Error"),
      ));
    }

    isLoading.value = false;
  }

  //run when the patient select offline booking
  Future<void> doOfflineBooking(int id) async {
    Get.back();
    isLoading.value = true;
    // Get doctors list from server
    final timestamp =
        '${date.value.year}-${date.value.month}-${date.value.day}_${time.value.hour}:${time.value.minute}';
    try {
      final response = await dio.post('${C.urlA}/app/patient/appointment',
          data: {'doctorId': id, 'datetime': timestamp, 'session': "OFFLINE"},
          options: Options(headers: {"Authorization": "Bearer $token"}));
      Get.back();
    } on DioError catch (e, s) {
      Get.dialog(AlertDialog(
        title: const Text("Error"),
        content: Text(e.response?.data ?? "Unknown error"),
      ));
    } catch (e) {
      Get.dialog(const AlertDialog(
        title: Text("Error"),
        content: Text("Unknown Error"),
      ));
    }

    isLoading.value = false;
  }
}

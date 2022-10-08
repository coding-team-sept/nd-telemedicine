import 'package:dio/dio.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/home/model/online_patient_appointment_model.dart';

import '../../../routes/app_pages.dart';

class DoctorOnlineBookingController extends GetxController {

  final isLoading = false.obs;
  RxList<OnlinePatientAppointmentModel> onlinepatientappointmentData = <OnlinePatientAppointmentModel>[].obs;
  late String token;

  @override
  void onInit() async{
    token = await const FlutterSecureStorage().read(key: "token") ?? '';
    getOnlinePatientAppointment();
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

  void getOnlinePatientAppointment() async {
    isLoading.value = true;
    // Get doctors list from server
    const url = 'http://95.111.217.168:9001/api/v1';
    try {
      final response = await Dio().get('$url/app/doctor/appointment',
          options: Options(headers: {"Authorization": "Bearer $token"}));
      onlinepatientappointmentData.clear();
      for (var element in (response.data["data"] as List)) {
        if(element['session'] == "ONLINE"){
          onlinepatientappointmentData.add(OnlinePatientAppointmentModel(
            id: element['id'],
            datetime: element['datetime'],
            session: element['session'],
            patientId: element['patient']['id'],
            patientEmail: element['patient']['email'],
            patientName: element['patient']['name'],
          ));
        }
      }
      } on DioError catch (e, s){

    }
      isLoading.value = false;

    }
  void showOnlinePatientAppointmentDetail(int id) {
    Get.toNamed(Routes.CHAT);
  }
}

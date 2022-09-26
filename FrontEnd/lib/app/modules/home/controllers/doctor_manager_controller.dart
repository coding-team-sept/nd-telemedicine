import 'package:get/get.dart';

import '../../../routes/app_pages.dart';

class DoctorManagerController extends GetxController {
  //TODO: Implement DoctorManagerController

  final count = 0.obs;
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

  void newAddDoctor() => Get.toNamed(Routes.ADD_DOCTOR);
}

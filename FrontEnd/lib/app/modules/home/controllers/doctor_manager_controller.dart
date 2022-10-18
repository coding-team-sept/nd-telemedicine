import 'package:get/get.dart';

import '../../../routes/app_pages.dart';

class DoctorManagerController extends GetxController {
  final count = 0.obs;

  //run this function will go to add doctor page to add new doctor
  void newAddDoctor() => Get.toNamed(Routes.ADD_DOCTOR);
}

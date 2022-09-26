import 'package:get/get.dart';

import '../../../routes/app_pages.dart';

class DoctorManagerController extends GetxController {
  final count = 0.obs;

  void newAddDoctor() => Get.toNamed(Routes.ADD_DOCTOR);
}

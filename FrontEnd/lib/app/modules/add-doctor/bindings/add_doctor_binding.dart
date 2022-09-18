import 'package:get/get.dart';

import '../controllers/add_doctor_controller.dart';

class AddDoctorBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<AddDoctorController>(
      () => AddDoctorController(),
    );
  }
}

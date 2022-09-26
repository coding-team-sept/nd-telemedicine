import 'package:get/get.dart';

import 'package:nd/app/modules/home/controllers/admin_manager_controller.dart';
import 'package:nd/app/modules/home/controllers/appointment_controller.dart';
import 'package:nd/app/modules/home/controllers/profile_controller.dart';

import '../controllers/doctor_manager_controller.dart';
import '../controllers/home_controller.dart';

class HomeBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut<AdminManagerController>(
      () => AdminManagerController(),
    );
    Get.lazyPut<DoctorManagerController>(
      () => DoctorManagerController(),
    );
    Get.lazyPut<ProfileController>(() => ProfileController());
    Get.put<HomeController>(HomeController());
    Get.lazyPut<AppointmentController>(() => AppointmentController());
  }
}

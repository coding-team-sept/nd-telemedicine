import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/home_controller.dart';
import 'package:nd/app/routes/app_pages.dart';

class ProfileController extends GetxController {
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

  void logout() async => Get.find<HomeController>().logout();
}

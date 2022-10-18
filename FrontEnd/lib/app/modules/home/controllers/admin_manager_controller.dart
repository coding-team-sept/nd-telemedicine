import 'package:get/get.dart';

import '../../../routes/app_pages.dart';

class AdminManagerController extends GetxController {

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
  // function for add new admin and will go to add-admin page
  void newAddAdmin() => Get.toNamed(Routes.ADD_ADMIN);

}

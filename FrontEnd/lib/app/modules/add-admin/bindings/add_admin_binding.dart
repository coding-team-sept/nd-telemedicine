import 'package:get/get.dart';

import '../controllers/add_admin_controller.dart';

class AddAdminBinding extends Bindings {
  @override
  void dependencies() {
    Get.put<AddAdminController>(
      AddAdminController(),
    );
  }
}

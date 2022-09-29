import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/routes/app_pages.dart';

class HomeController extends GetxController {
  final storage = const FlutterSecureStorage();
  final token = Rx<String?>(null);
  final email = Rx<String?>(null);
  final role = Rx<String?>(null);

  @override
  void onInit() async {
    token.value = await storage.read(key: "token");
    email.value = await storage.read(key: "email");
    role.value = await storage.read(key: "role");
    if (token.value == null) {
      Get.offNamedUntil(Routes.LOGIN, (route) => false);
    }
    super.onInit();
  }

  void logout() async {
    await storage.delete(key: "token");
    Get.offNamedUntil(Routes.LOGIN, (r) => false);
  }

  @override
  void onReady() {
    super.onReady();
  }

  @override
  void onClose() {
    super.onClose();
  }
}

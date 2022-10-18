import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:get/get.dart';
import 'package:nd/app/routes/app_pages.dart';

class HomeController extends GetxController {

  final bool testMode;
  final storage = const FlutterSecureStorage();
  final token = Rx<String?>(null);
  final email = Rx<String?>(null);
  final role = Rx<String?>(null);

  HomeController({bool? testMode}): testMode = testMode ?? false;

  @override
  void onInit() async {
    if(!testMode){
      // if not the test mode, will get user's token, email and role
      token.value = await storage.read(key: "token");
      email.value = await storage.read(key: "email");
      role.value = await storage.read(key: "role");
      if (token.value == null) {
        Get.offNamedUntil(Routes.LOGIN, (route) => false);
      }
    } else {
      //if it is test mode, just emulate data for test
      token.value = "token";
      email.value = "email";
      role.value = "admin";
    }
    super.onInit();
  }

  void logout() async {
    //when user click logout, the token will be deleted
    await storage.delete(key: "token");
    //And they will back to Login page
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

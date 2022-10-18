import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/profile_controller.dart';
import 'package:nd/app/modules/home/views/profile_view.dart';
import 'package:nd/app/routes/app_pages.dart';

void main(){
  group("UI test for add doctor", () {
    late ProfileController controller;
    late GetMaterialApp view;

    setUp(() {
      controller = ProfileController();
      view = GetMaterialApp(
        home: const ProfileView(),
        getPages: AppPages.routes,
      );
      Get.reset();
      Get.testMode = true;
      Get.put(controller);
    });
    tearDown(() {
      Get.reset();
    });
    testWidgets("Test widgets completeness", (t) async {
      await t.pumpWidget(view);
      expect(find.byType(Expanded), findsNWidgets(1));
      expect(find.byType(Container), findsNWidgets(2));
      expect(find.byType(ElevatedButton), findsNWidgets(1));
    });
  });
}
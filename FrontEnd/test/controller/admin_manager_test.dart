import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/admin_manager_controller.dart';
import 'package:nd/app/modules/home/controllers/profile_controller.dart';
import 'package:nd/app/modules/home/views/admin_manager_view.dart';
import 'package:nd/app/modules/home/views/profile_view.dart';
import 'package:nd/app/routes/app_pages.dart';

void main(){
  group("UI test for admin manager", () {
    late AdminManagerController controller;
    late GetMaterialApp view;

    setUp(() {
      controller = AdminManagerController();
      view = GetMaterialApp(
        home: const AdminManagerView(),
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
      expect(find.byType(Expanded), findsOneWidget);
      expect(find.byType(Flex), findsOneWidget);
      expect(find.byType(FloatingActionButton), findsOneWidget);
    });
  });
}

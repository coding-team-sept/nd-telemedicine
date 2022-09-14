import 'package:flutter/material.dart';

import 'package:get/get.dart';

import 'package:nd/app/routes/app_pages.dart';
import '../controllers/dashboard_controller.dart';

class DashboardView extends GetView<DashboardController> {
  const DashboardView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          const SizedBox(
            height: 12,
          ),
          const Text(
            'DashboardView is working, and will be replaced with actual content later',
            style: TextStyle(fontSize: 20),
          ),
          const SizedBox(
            height: 12,
          ),
          // TODO: add code to go to login view
          ElevatedButton(
              onPressed: () => Get.toNamed(Routes.LOGIN),
              child: const Text("Login")),
          const SizedBox(
            height: 12,
          ),
          // TODO: add code to go to login view
          ElevatedButton(
              onPressed: () => Get.toNamed(Routes.REGISTER),
              child: const Text("Register")),
        ],
      ),
    );
  }
}

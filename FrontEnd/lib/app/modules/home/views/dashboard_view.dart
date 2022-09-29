import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/views/doctor_online_booking_view.dart';

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

          ElevatedButton(
              onPressed: () => Get.toNamed(Routes.LOGIN),
              child: const Text("Login")),
          const SizedBox(
            height: 12,
          ),

          ElevatedButton(
              onPressed: () => Get.toNamed(Routes.REGISTER),
              child: const Text("Register")),

          const SizedBox(
            height: 12,
          ),
          ElevatedButton(
              onPressed: () => Get.toNamed(Routes.ADD_DOCTOR),
              child: const Text("Add Doctor")),

        const SizedBox(
          height: 12,
        ),
        ElevatedButton(
            onPressed: () => Get.toNamed(Routes.ADD_ADMIN),
            child: const Text("Add Admin")),

          const SizedBox(
            height: 12,
          ),
          ElevatedButton(
              onPressed: () => Get.toNamed(Routes.CHAT),
              child: const Text("Chat")),

          const SizedBox(
            height: 12,
          ),
          ElevatedButton(
              onPressed: () => Get.toNamed(Routes.ADMIN_LIST_VIEW),
              child: const Text("Admin Listview")),

          const SizedBox(
            height: 12,
          ),
          ElevatedButton(
              onPressed: () => Get.to(DoctorOnlineBookingView()),
              child: const Text("Doctor online"))

        ],
      ),
    );
  }
}

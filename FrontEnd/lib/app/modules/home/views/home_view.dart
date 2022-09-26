import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/dashboard_controller.dart';
import 'package:nd/app/modules/home/views/dashboard_view.dart';
import 'package:nd/app/modules/home/views/appointment_view.dart';
import 'package:nd/app/modules/home/views/profile_view.dart';

import '../controllers/home_controller.dart';

class HomeView extends GetView<HomeController> {
  const HomeView({Key? key}) : super(key: key);
  static const adminTabBar = [
    Tab(
      icon: Icon(Icons.admin_panel_settings),
      text: "Admin",
    ),
    Tab(
      icon: Icon(Icons.emergency),
      text: "Doctor",
    ),
  ];
  static const patientTabBar = [
    Tab(
      icon: Icon(Icons.home),
      text: "Dashboard",
    ),
    Tab(
      icon: Icon(Icons.timer),
      text: "Appointment",
    ),
  ];
  @override
  Widget build(BuildContext context) {
    Get.put(DashboardController());

    return DefaultTabController(
      length: 3,
      child: Obx(
        () => Scaffold(
          appBar: AppBar(
            title: Text(controller.email.value ?? ""),
            bottom: TabBar(tabs: [
              if (controller.role.value == "ROLE_ADMIN") ...adminTabBar,
              if (controller.role.value != "ROLE_ADMIN") ...patientTabBar,
              const Tab(
                icon: Icon(Icons.account_circle),
                text: "Profile",
              ),
            ]),
          ),
          body: const TabBarView(children: [
            DashboardView(),
            AppointmentView(),
            ProfileView(),
          ]),
        ),
      ),
    );
  }
}

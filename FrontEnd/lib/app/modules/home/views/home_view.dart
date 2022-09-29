import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/dashboard_controller.dart';
import 'package:nd/app/modules/home/views/dashboard_view.dart';
import 'package:nd/app/modules/home/views/appointment_view.dart';
import 'package:nd/app/modules/home/views/doctor_manager_view.dart';
import 'package:nd/app/modules/home/views/offline_doctor_booking_view.dart';
import 'package:nd/app/modules/home/views/profile_view.dart';

import '../controllers/home_controller.dart';
import 'admin_manager_view.dart';

class HomeView extends GetView<HomeController> {
  const HomeView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    const adminTabBar = [
      Tab(
        icon: Icon(Icons.admin_panel_settings),
        text: "Admin",
      ),
      Tab(
        icon: Icon(Icons.emergency),
        text: "Doctor",
      ),
    ];
    const patientTabBar = [
      Tab(
        icon: Icon(Icons.home),
        text: "Dashboard",
      ),
      Tab(
        icon: Icon(Icons.timer),
        text: "Appointment",
      ),
    ];
    const doctorTabBar = [
      Tab(
        icon: Icon(Icons.book_online),
        text: "Offline Booking",
      ),
      Tab(
        icon: Icon(Icons.book_outlined),
        text: "Online Booking",
      ),
    ];
    const patientContent = [
      DashboardView(),
      AppointmentView(),
    ];
    var adminContent = [
      const AdminManagerView(),
      const DoctorManagerView(),
    ];
    const doctorContent = [
      OfflineDoctorBookingView(),
      Scaffold(),
    ];
    Get.put(DashboardController());

    return DefaultTabController(
      length: 3,
      child: Obx(
        () => Scaffold(
          appBar: AppBar(
            title: Text(controller.email.value ?? ""),
            bottom: TabBar(tabs: [
              if (controller.role.value == "ROLE_ADMIN") ...adminTabBar,
              if (controller.role.value == "ROLE_PATIENT") ...patientTabBar,
              if (controller.role.value == "ROLE_DOCTOR") ...doctorTabBar,
              const Tab(
                icon: Icon(Icons.account_circle),
                text: "Profile",
              ),
            ]),
          ),
          body: TabBarView(children: [
            if (controller.role.value == "ROLE_PATIENT") ...patientContent,
            if (controller.role.value == "ROLE_ADMIN") ...adminContent,
            if (controller.role.value == "ROLE_DOCTOR") ...doctorContent,
            const ProfileView(),
          ]),
        ),
      ),
    );
  }
}

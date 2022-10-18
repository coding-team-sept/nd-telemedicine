import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/dashboard_controller.dart';
import 'package:nd/app/modules/home/views/dashboard_view.dart';
import 'package:nd/app/modules/home/views/appointment_view.dart';
import 'package:nd/app/modules/home/views/doctor_manager_view.dart';

import 'package:nd/app/modules/home/views/doctor_online_booking_view.dart';

import 'package:nd/app/modules/home/views/offline_doctor_booking_view.dart';

import 'package:nd/app/modules/home/views/profile_view.dart';

import '../controllers/home_controller.dart';
import 'admin_manager_view.dart';

class HomeView extends GetView<HomeController> {
  const HomeView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    //admin point of view show all the admin and doctor
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
      //patient point of view show appointment and dashboard
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
      //doctor point of view show the offline and online booking page
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
      //widget for get dashboard and appointment from patient side
      DashboardView(),
      AppointmentView(),
    ];
    var adminContent = [
      //widget for get admin and doctor manager from admin side
      const AdminManagerView(),
      const DoctorManagerView(),
    ];

    const doctorContent = [
      //widget for get offline and online booking from doctor side
      OfflineDoctorBookingView(),
      DoctorOnlineBookingView(),
    ];
     var emptyContent = [
       //for testing purposes
      Container(),
      Container(),
    ];
    Get.put(DashboardController());

    return DefaultTabController(
      length: 3,
      child: Obx(
        () => Scaffold(
          appBar: AppBar(
            //show the icon on the leading of AppBar
            leading: Image.asset("assets/icon.jpg"),
            title: Text(controller.email.value ?? ""),
            bottom: TabBar(tabs: [
              // display the detail according to the role
              if (controller.role.value == "ROLE_ADMIN") ...adminTabBar,
              if (controller.role.value == "ROLE_PATIENT" ||
                  controller.role.value == "patient")
                ...patientTabBar,
              if (controller.role.value == "ROLE_DOCTOR") ...doctorTabBar,
              if(controller.role.value == null) ...doctorTabBar,
              const Tab(
                icon: Icon(Icons.account_circle),
                text: "Profile",
              ),
            ]),
          ),
          body: TabBarView(children: [
            // display the detail according to the role
            if (controller.role.value == "ROLE_PATIENT" ||
                controller.role.value == "patient")
              ...patientContent,
            if (controller.role.value == "ROLE_ADMIN") ...adminContent,
            if (controller.role.value == "ROLE_DOCTOR") ...doctorContent,
            if(controller.role.value == null) ...emptyContent,
            const ProfileView(),
          ]),

        ),
      ),
    );
  }
}

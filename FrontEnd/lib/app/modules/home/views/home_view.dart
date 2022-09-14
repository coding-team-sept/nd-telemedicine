import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/dashboard_controller.dart';
import 'package:nd/app/modules/home/views/dashboard_view.dart';
import 'package:nd/app/modules/home/views/appointment_view.dart';
import 'package:nd/app/modules/home/views/profile_view.dart';

import '../controllers/home_controller.dart';

class HomeView extends GetView<HomeController> {
  const HomeView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    Get.put(DashboardController());

    return DefaultTabController(
      length: 3,
      child: Scaffold(
        appBar: AppBar(
          title: const Text('Ravel Tanjaya'),
          bottom: const TabBar(tabs: [
            Tab(
              icon: Icon(Icons.home),
              text: "Dashboard",
            ),
            Tab(
              icon: Icon(Icons.timer),
              text: "Appointment",
            ),
            Tab(
              icon: Icon(Icons.account_circle),
              text: "Profile",
            ),
          ]),
        ),
        body: const TabBarView(children: [
          DashboardView(),
          // TODO: replace with actual page
          AppointmentView(),
          // TODO: replace with actual page
          ProfileView(),
        ]),
      ),
    );
  }
}

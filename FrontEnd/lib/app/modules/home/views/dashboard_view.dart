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
      body: Stack(
        children: [
          Container(
            height: 600,
            decoration: BoxDecoration(
                image: DecorationImage(
                  //the icon for this project display on the screen and store in assets file
                    image: AssetImage('assets/icon.jpg'),
                    colorFilter: ColorFilter.mode(Colors.white.withOpacity(0.3), BlendMode.modulate,)
                )
            ),
          ),
        ],
      ),
    );
  }
}

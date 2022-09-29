import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/doctor_online_booking_controller.dart';

class DoctorOnlineBookingView extends GetView<DoctorOnlineBookingController> {
  const DoctorOnlineBookingView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Flex(
        direction: Axis.vertical,
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              decoration: InputDecoration(
                  hintText: "Search",
                  suffixIcon: IconButton(
                    onPressed: () {},
                    icon: const Icon(Icons.search),
                  )),
            ),
          ),
          Expanded(
              child: ListView(
                children: [
                  Divider(),
                  ListTile(
                      onTap: controller.newChat,
                      title: Text("Steve Johnson"),
                      subtitle: Text("patient1@patient.com")),
                  Divider(),
                  ListTile(
                      onTap: controller.newChat,
                      title: Text("Emma Island"),
                      subtitle: Text("patient2@patient.com")),
                  Divider(),
                  ListTile(
                      onTap: controller.newChat,
                      title: Text("Jack Robert"),
                      subtitle: Text("patient3@patient.com")),
                  Divider(),
                  ListTile(
                      onTap: controller.newChat,
                      title: Text("Charles Johnson"),
                      subtitle: Text("patient4@patient.com")),
                  Divider(),
                ],
              ))
        ],
      )
    );
  }
}

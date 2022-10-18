import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/offline_doctor_booking_controller.dart';

import '../widget/offline-patient.dart';

class OfflineDoctorBookingView extends GetView<OfflineDoctorBookingController> {
  const OfflineDoctorBookingView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Flex(direction: Axis.vertical, children: [
        Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              //UI for search function
                decoration: InputDecoration(
                    suffixIcon: IconButton(
                      onPressed: () {},
                      icon: const Icon(Icons.search),
                    ),
                    hintText: "Search"))),
        const Padding(
          padding: EdgeInsets.symmetric(vertical: 8),
          child: Text('Pull down to refresh'),
        ),
        const Divider(),
        const SizedBox(
          height: 4,
        ),
        Expanded(
          child: RefreshIndicator(
            //when pull down to fresh the page, new offline booking will be shown on the screen
            onRefresh: () async => controller.getOfflineAppointment(),
            child: Obx(
              () => ListView.builder(
                  itemBuilder: (context, index) => OfflineAppointmentTile(
                    //display the offline booking detail
                        controller.offlineData[index],
                        controller.showPatientDetail,
                      ),
                  itemCount: controller.offlineData.length),
            ),
          ),
        ),
      ]),
    );
  }
}

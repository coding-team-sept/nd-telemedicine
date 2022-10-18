import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/doctor_online_booking_controller.dart';
import 'package:nd/app/modules/home/model/online_patient_appointment_model.dart';
import 'package:nd/app/modules/home/widget/online_patient_apppoinment_w.dart';

class DoctorOnlineBookingView extends GetView<DoctorOnlineBookingController> {
  const DoctorOnlineBookingView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Obx(
      () => controller.isLoading.value
          ? Center(
              child: CircularProgressIndicator(),
            )
          : RefreshIndicator(
        //when pull down refresh the page, new patient will be shown on the screen
              onRefresh: () async => controller.getOnlinePatientAppointment(),
              child: Flex(
                direction: Axis.vertical,
                children: [
                  Padding(
                    padding: const EdgeInsets.symmetric(vertical: 8),
                    child: Text('Pull down to refresh'),
                  ),
                  Divider(),
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: TextField(
                      //UI for search function
                      decoration: InputDecoration(
                          hintText: "Search",
                          suffixIcon: IconButton(
                            onPressed: () {},
                            icon: const Icon(Icons.search),
                          )),
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                        itemBuilder: (context, index) =>
                            OnlinePatientAppointmentTile(
                              //get the patient appointment detail on the screen
                              controller.onlinepatientappointmentData[index],
                              controller.showOnlinePatientAppointmentDetail,
                            ),
                        itemCount:
                            controller.onlinepatientappointmentData.length),
                  ),
                ],
              ),
            ),
    ));
  }
}

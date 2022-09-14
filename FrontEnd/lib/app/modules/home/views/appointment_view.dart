import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/appointment_controller.dart';
import '../widget/appointment-w.dart';

class AppointmentView extends GetView<AppointmentController> {
  const AppointmentView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        floatingActionButton: FloatingActionButton(
          //onPressed: controller.newAppointment,
          onPressed: controller.newAppointment,
          child: const Icon(Icons.add),
        ),
        body: Obx(
          () => controller.isLoading.value
              ? const Center(
                  child: CircularProgressIndicator(),
                )
              : ListView.builder(
                  itemBuilder: (context, index) =>
                      index == controller.appointmentData.length
                          ? ElevatedButton(
                              onPressed: controller.getAppointment,
                              child: const Text("Refresh"))
                          : AppointmentTile(
                              controller.appointmentData[index],
                              controller.showAppointmentDetail,
                            ),
                  itemCount: controller.appointmentData.length + 1),
        ));
  }
}

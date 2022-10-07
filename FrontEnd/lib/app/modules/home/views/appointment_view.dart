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
              : RefreshIndicator(
                onRefresh: () async => controller.getAppointment(),
                child: Flex(
                  direction: Axis.vertical,
                  children: [
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 8),
                      child: Text('Pull down to refresh'),
                    ),
                    Divider(),
                    Expanded(
                      child: ListView.builder(
                          itemBuilder: (context, index) =>
                             AppointmentTile(
                                      controller.appointmentData[index],
                                      controller.showAppointmentDetail,
                                    ),
                          itemCount: controller.appointmentData.length),
                    ),
                  ],
                ),
              ),
        ));
  }
}

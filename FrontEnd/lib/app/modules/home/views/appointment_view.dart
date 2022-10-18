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
          onPressed: controller.newAppointment,
          child: const Icon(Icons.add),
        ),
        body: Obx(
          () => controller.isLoading.value
              ? const Center(
                  child: CircularProgressIndicator(),
                )
              : RefreshIndicator(
            //when pull down to refresh the page, then new appointments will be fetched
                onRefresh: () async => controller.getAppointment(),
                child: Flex(
                  direction: Axis.vertical,
                  children: [
                    const Padding(
                      padding: EdgeInsets.symmetric(vertical: 8),
                      child: Text('Pull down to refresh'),
                    ),
                    const Divider(),
                    Expanded(
                      child: ListView.builder(
                          itemBuilder: (context, index) =>
                             AppointmentTile(
                               //show the detail of all the appointment on the page
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

import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/create_appointment_controller.dart';

class CreateAppointmentView extends GetView<CreateAppointmentController> {
  const CreateAppointmentView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text("Book a Doctor"),
          centerTitle: true,
        ),
        body: Flex(
          direction: Axis.vertical,
          children: [
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Obx(
                () => Flex(
                  direction: Axis.horizontal,
                  children: [
                    Expanded(
                      child: TextButton(
                        //run controller function to select a date and get doctor
                        onPressed: controller.selectDate,
                        child: Text(controller.formattedDate),
                      ),
                    ),
                    Expanded(
                      child: TextButton(
                        //run controller function to select a time and get doctor
                        onPressed: controller.selectTime,
                        child: Text(controller.formattedTime),
                      ),
                    ),
                  ],
                ),
              ),
            ),
            Obx(
              () => controller.isLoading.value
                  ? const Expanded(
                      child: Center(
                        //shape for loading is circular
                        child: CircularProgressIndicator(),
                      ),
                    )
                  : Expanded(
                      child: ListView.builder(
                          itemCount: controller.doctorData.length,
                          itemBuilder: (context, index) => ListTile(
                            // display the booking data
                                onTap: () => controller
                                    .doBooking(controller.doctorData[index].id),
                                leading: CircleAvatar(
                                  backgroundColor: Colors.greenAccent.shade700,
                                  child: Text(
                                    controller.doctorData[index].name[0],
                                    style: const TextStyle(color: Colors.white),
                                  ),
                                ),
                                title: Text(controller.doctorData[index].name),
                                subtitle: const Text("Available"),
                              )),
                    ),
            ),
          ],
        ));
  }
}

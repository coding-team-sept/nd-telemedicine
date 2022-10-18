import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/add_doctor_controller.dart';

class AddDoctorView extends GetView<AddDoctorController> {
  const AddDoctorView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    //Scaffold is a class in flutter which provides many widgets
    return Scaffold(
        appBar: AppBar(
          //Use the AppBar to reserve a place at the top of the page with the title "Add Admin"
          title: const Text('Add New Doctor'),
          centerTitle: true,
        ),
        body: Stack(
          // Use stack to design because we have to make "isLoading" stack on the page when user click "Add doctor" button
          children: [
            ListView(
              padding: const EdgeInsets.all(16),
              children: [
                Obx(
                  () => TextField(
                    onChanged: controller.nameChanged,
                    decoration: InputDecoration(
                        hintText: "Doctor Name",
                        errorText: controller.nameError.value),
                  ),
                ),
                const SizedBox(
                  height: 8,
                ),
                Obx(
                  () => TextField(
                    onChanged: controller.emailChanged,
                    decoration: InputDecoration(
                        hintText: "Doctor Email",
                        errorText: controller.emailError.value),
                  ),
                ),
                const SizedBox(
                  height: 8,
                ),
                Obx(
                  () => TextField(
                    onChanged: controller.passwordChanged,
                    decoration: InputDecoration(
                        hintText: "Password",
                        errorText: controller.passwordError.value),
                  ),
                ),
                const SizedBox(
                  height: 8,
                ),
                ElevatedButton(
                    onPressed: controller.isLoading.value
                        ? null
                        : controller.addDoctor,
                    child: const Text('Add')),
              ],
            ),
            Obx(
              () => controller.isLoading.value
                  ? Container(
                      alignment: Alignment.center,
                      color: Colors.grey.withOpacity(0.5),
                      child: const CircularProgressIndicator(),
                    )
                  : Container(),
            ),
          ],
        ));
  }
}

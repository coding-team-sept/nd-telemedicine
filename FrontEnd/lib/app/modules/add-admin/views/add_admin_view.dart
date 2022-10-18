import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/add_admin_controller.dart';

class AddAdminView extends GetView<AddAdminController> {
  const AddAdminView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      //Scaffold is a class in flutter which provides many widgets
        appBar: AppBar(
          //Use the AppBar to reserve a place at the top of the page with the title "Add Admin"
          title: const Text('Add new Admin'),
          centerTitle: true,
        ),
        body: Stack(
          // Use stack to design because we have to make "isLoading" stack on the page when user click "Add admin" button
          children: [
            ListView(
              padding: const EdgeInsets.all(16),
              children: [
                Obx(
                  () => TextField(
                    onChanged: controller.emailChanged,
                    decoration: InputDecoration(
                        hintText: "Email",
                        errorText: controller.emailError.value),
                  ),
                ),
                const SizedBox(height: 8),
                Obx(
                  () => TextField(
                    onChanged: controller.nameChanged,
                    decoration: InputDecoration(
                        hintText: "Name",
                        errorText: controller.nameError.value),
                  ),
                ),
                const SizedBox(height: 8),
                Obx(
                  () => TextField(
                    onChanged: controller.passwordChanged,
                    decoration: InputDecoration(
                        hintText: "Password",
                        errorText: controller.passwordError.value),
                  ),
                ),
                const SizedBox(height: 8),
                ElevatedButton(
                    onPressed:
                        controller.isLoading.value ? null : controller.addAdmin,
                    child: const Text('Add'))
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

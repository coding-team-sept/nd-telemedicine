import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/profile_controller.dart';

class ProfileView extends GetView<ProfileController> {
  const ProfileView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Flex(
      direction: Axis.vertical,
      children: [
        Container(
            alignment: Alignment.center,
            padding: const EdgeInsets.symmetric(vertical: 32, horizontal: 32),
            child: Text(
              "Ravel Tanjaya",
              style: Get.theme.textTheme.headlineLarge,
            )),
        Expanded(child: Container()),
        Container(
          padding: const EdgeInsets.all(32),
          child: SizedBox(
            width: double.infinity,
            child: ElevatedButton(
                onPressed: controller.logout, child: const Text("Log Out")),
          ),
        )
      ],
    ));
  }
}

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
        Expanded(child: Container()),
        Container(
          padding: const EdgeInsets.all(32),
          child: SizedBox(
            width: double.infinity,
            child: ElevatedButton(
              //when press the button, will run the logout function on controller
                onPressed: controller.logout, child: const Text("Log Out")),
          ),
        )
      ],
    ));
  }
}

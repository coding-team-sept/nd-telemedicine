import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/doctor_manager_controller.dart';

class DoctorManagerView extends GetView<DoctorManagerController> {
  const DoctorManagerView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      floatingActionButton: FloatingActionButton(
        onPressed: controller.newAddDoctor,
        child: const Icon(Icons.add),
      ),
      body: Flex(
        direction: Axis.vertical,
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              //write a UI for search
              decoration: InputDecoration(
                  hintText: "Search",
                  suffixIcon: IconButton(
                    onPressed: () {},
                    icon: const Icon(Icons.search),
                  )),
            ),
          ),
          const SizedBox(height: 4),
          Expanded(
              child: ListView(
            children: const [
              //UI for display all the doctor on the screen
              Divider(),
              ListTile(
                  title: Text("Charles Robert"),
                  subtitle: Text("doctor1@doctor.com")),
              Divider(),
              ListTile(
                  title: Text("George Bush"),
                  subtitle: Text("george.bush@doctor.com")),
              Divider(),
              ListTile(
                  title: Text("John Smith"),
                  subtitle: Text("john.smith3@doctor.com")),
              Divider(),
              ListTile(
                  title: Text("Davis Johnson"),
                  subtitle: Text("davis.johnson4@doctor.com")),
              Divider(),
            ],
          ))
        ],
      ),
    );
  }
}

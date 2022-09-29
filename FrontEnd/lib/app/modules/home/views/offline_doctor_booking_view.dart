import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/offline_doctor_booking_controller.dart';

class OfflineDoctorBookingView extends GetView<OfflineDoctorBookingController> {
  const OfflineDoctorBookingView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(

      body: Flex(
          direction: Axis.vertical,
          children: [

            Padding(
                padding: const EdgeInsets.all(8.0),
                child: TextField(
                    decoration: InputDecoration(
                        suffixIcon: IconButton(
                          onPressed: () {},icon: Icon(Icons.search), ),
                        hintText: "Search"

                    ))
            ),


            const SizedBox(height: 4,),
            Expanded(child: ListView(
              children:  [
                Divider(),
                ListTile(title: Text('Wenlao'),subtitle:Text('2022-08-09 10:00:00'),onTap:controller.showDetail,),
                Divider(),
                ListTile(title: Text('Alex'),subtitle:Text('2022-08-09 10:00:00'),onTap:controller.showDetail),
                Divider(),
                ListTile(title: Text('Bai'),subtitle:Text('2022-08-09 10:00:00'),onTap:controller.showDetail),
                Divider(),
                ListTile(title: Text('Ravel'),subtitle:Text('2022-08-09 10:00:00'),onTap:controller.showDetail),
                Divider(),
                ListTile(title: Text('Ling'),subtitle:Text('2022-08-09 10:00:00'),onTap:controller.showDetail),
                Divider()
              ],
            ))

          ]
      ),
    );
  }
}


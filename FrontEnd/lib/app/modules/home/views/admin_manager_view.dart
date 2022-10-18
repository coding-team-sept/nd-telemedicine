import 'package:flutter/material.dart';

import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/admin_manager_controller.dart';

class AdminManagerView extends GetView<AdminManagerController> {
  const AdminManagerView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
//add new admin when floating action button is pressed
      floatingActionButton: FloatingActionButton(onPressed: controller.newAddAdmin,
        child: const Icon(Icons.add),
      ),

      body: Flex(
        direction: Axis.vertical,
        children: [

          Padding(
            padding: const EdgeInsets.all(8.0),
            child: TextField(
              //search UI for admin
                decoration: InputDecoration(
                    suffixIcon: IconButton(
                        onPressed: () {},icon: Icon(Icons.search), ),
                  hintText: "Search"

                ))
            ),


          const SizedBox(height: 4,),
        Expanded(child: ListView(
          children: const [
            //show all the admin detail about their name and email on the page
            Divider(),
            ListTile(title: Text('Wenlao'),subtitle:Text('12345@gmail.com'),),
            Divider(),
            ListTile(title: Text('Alex'),subtitle:Text('ergrgsr@gmail.com'),),
            Divider(),
            ListTile(title: Text('Bai'),subtitle:Text('good@gmail.com'),),
            Divider(),
            ListTile(title: Text('Ravel'),subtitle:Text('1111111@gmail.com'),),
            Divider(),
            ListTile(title: Text('Ling'),subtitle:Text('12sdfs@gmail.com'),),
            Divider()
          ],
        ))

        ]
      ),
    );
  }
}

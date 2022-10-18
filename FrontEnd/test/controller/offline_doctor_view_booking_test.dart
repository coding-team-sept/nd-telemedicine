import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/offline_doctor_booking_controller.dart';
import 'package:nd/app/modules/home/views/offline_doctor_booking_view.dart';

void main(){
  group('Doctor view offline booking', () {
    late OfflineDoctorBookingController controller;
    late  GetMaterialApp view;

    setUp((){
      controller = OfflineDoctorBookingController();
      view = const GetMaterialApp(
        home: OfflineDoctorBookingView(),
      );
      Get.reset();
      Get.testMode = true;
      Get.put(controller);
    });

    // testWidgets('Test Doctor view offline booking', (tester) async{
    //   const childWidget = Padding(padding: EdgeInsets.all(8.0));
    //   await tester.pumpWidget(view);
    //   expect(find.byType(TextField), findsOneWidget);
    //   expect((find.byType(TextField).evaluate().elementAt(0).widget as TextField).decoration?.hintText,"Search");
    //   expect(find.byIcon(Icons.search),findsOneWidget);
    //   expect(find.byType(ListTile), findsNWidgets(5));
    //
    //   expect((((find.byType(ListTile).evaluate().elementAt(0).widget as ListTile)
    //       .title) as Text).data, "Wenlao",);
    //   expect((((find.byType(ListTile).evaluate().elementAt(0).widget as ListTile)
    //       .subtitle) as Text).data, "2022-08-09 10:00:00",);
    //   await tester.tap(find.byType(ListTile).at(0));
    //   await tester.pump(const Duration(seconds: 1));
    //   expect(find.byType(AlertDialog), findsOneWidget);
    //
    //   expect((((find.byType(AlertDialog).evaluate().elementAt(0).widget as AlertDialog)
    //       .title) as Text).data, "Information",);
    //
    //   expect((((find.byType(AlertDialog).evaluate().elementAt(0).widget as AlertDialog)
    //       .content) as Text).data, "Please go to the location directly",);
    // });
  });
}
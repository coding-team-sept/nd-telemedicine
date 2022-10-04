import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/home/controllers/doctor_online_booking_controller.dart';
import 'package:nd/app/modules/home/views/doctor_online_booking_view.dart';
import 'package:nd/app/routes/app_pages.dart';
import 'package:nock/nock.dart';

void main(){
  group("Unit test for doctor online booking",
  () {
    late DoctorOnlineBookingController controller;
    setUp(() {
      Get.reset();
      controller = DoctorOnlineBookingController();
    });

  });

  group("Widget testing", ()
  {
    late DoctorOnlineBookingController controller;
    late GetMaterialApp view;


    setUp(() {
      controller = DoctorOnlineBookingController();
      view = GetMaterialApp(
        home: DoctorOnlineBookingView(),

        getPages: AppPages.routes,
      );
      Get.reset();
      Get.testMode = true;
      Get.put(controller);
    });

    tearDown(() {
      Get.reset();
    });

    testWidgets("test the onTap function is working and go to chat page", (
        tester) async {
      await tester.pumpWidget(view);
      var listTiles = find.byType(ListTile);
      await tester.tap(listTiles.at(0));
      await tester.pump(Duration(milliseconds: 500));
      expect(Get.currentRoute, Routes.CHAT);
      Get.back();
      await tester.pump(Duration(milliseconds: 500));

      await tester.tap(listTiles.at(1));
      await tester.pump(Duration(milliseconds: 500));
      expect(Get.currentRoute, Routes.CHAT);
      Get.back();
      await tester.pump(Duration(milliseconds: 500));

      await tester.tap(listTiles.at(2));
      await tester.pump(Duration(milliseconds: 500));
      expect(Get.currentRoute, Routes.CHAT);
      Get.back();
      await tester.pump(Duration(milliseconds: 500));

      await tester.tap(listTiles.at(3));
      await tester.pump(Duration(milliseconds: 500));
      expect(Get.currentRoute, Routes.CHAT);
      Get.back();
    });

    testWidgets("Test all the title shows on the screen",
            (tester) async {
          await tester.pumpWidget(view);
          var listTiles = find.byType(ListTile);
          expect(listTiles, findsNWidgets(4));
          expect((((listTiles
              .evaluate()
              .elementAt(0)
              .widget as ListTile)
              .title) as Text).data ?? "",
              "Steve Johnson");
          expect((((listTiles
              .evaluate()
              .elementAt(1)
              .widget as ListTile)
              .title) as Text).data ?? "",
              "Emma Island");
          expect((((listTiles
              .evaluate()
              .elementAt(0)
              .widget as ListTile)
              .title) as Text).data ?? "",
              "Jack Robert");
          expect(((listTiles
              .evaluate()
              .elementAt(0)
              .widget as ListTile)
              .title as Text).data ?? "",
              "Charles Johnson");
        });

    testWidgets("Test all the subtitle shows on the screen",
            (tester) async {
          await tester.pumpWidget(view);
          var listTiles = find.byType(ListTile);
          expect(listTiles, findsNWidgets(4));
          expect((((listTiles
              .evaluate()
              .elementAt(0)
              .widget as ListTile)
              .subtitle) as Text).data ?? "",
              "patient1@patient.com");
          expect((((listTiles
              .evaluate()
              .elementAt(1)
              .widget as ListTile)
              .subtitle) as Text).data ?? "",
              "patient2@patient.com");
          expect((((listTiles
              .evaluate()
              .elementAt(0)
              .widget as ListTile)
              .subtitle) as Text).data ?? "",
              "patient3@patient.com");
          expect(((listTiles
              .evaluate()
              .elementAt(0)
              .widget as ListTile)
              .subtitle as Text).data ?? "",
              "patient4@patient.com");
        });

    testWidgets("Test the search show on the screen",
            (tester) async {
          await tester.pumpWidget(view);
          var textFields = find.byType(TextField);
          expect((textFields.evaluate().elementAt(0).widget as TextField).decoration?.hintText ?? "", "Search");
        });
  });

  }
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:http_mock_adapter/http_mock_adapter.dart';
import 'package:nd/app/data/const.dart';
import 'package:nd/app/modules/create_appointment/controllers/create_appointment_controller.dart';
import 'package:nd/app/modules/create_appointment/model/doctor_model.dart';
import 'package:nd/app/routes/app_pages.dart';

void main() {
  group("Appointment Controller Unit Testing", () {
    late CreateAppointmentController controller;
    final dio = Dio();
    final dioAdapter = DioAdapter(dio: dio);

    setUp(() {
      Get.reset();
      dio.httpClientAdapter = dioAdapter;
      controller = CreateAppointmentController(dio: dio);
      Get.testMode = true;
    });

    tearDown(() => Get.reset());

    test('Testing if get doctors is working', () async {
      final date = DateTime.now();
      final time = TimeOfDay.now();
      final timestamp =
          '${date.year}-${date.month}-${date.day}_${time.hour}:${time.minute}';
      dioAdapter.onGet("${C.urlA}/app/patient/doctor/$timestamp", (server) {
        server.reply(
          200,
          {
            "data": [
              {"id": 55, "email": "doctor@doctor.com", "name": "Doctor"}
            ]
          },
          delay: const Duration(milliseconds: 500),
          headers: {
            Headers.contentTypeHeader: [Headers.jsonContentType],
            "Authorization": ["Bearer token"]
          },
        );
      });
      controller.date.value = date;
      controller.time.value = time;
      controller.token = "token";
      controller.getDoctors();
      expect(controller.isLoading.value, true);
      await Future.delayed(const Duration(seconds: 1));
      expect(controller.isLoading.value, false);
      expect(controller.doctorData.length, 1);
      expect(controller.doctorData[0].id, 55);
      expect(controller.doctorData[0].name, "Doctor");
    });
  });

  group("Widget Testing Create Appointment", () {
    late CreateAppointmentController controller;
    late GetMaterialApp view;
    final dio = Dio();
    final dioAdapter = DioAdapter(dio: dio);

    setUp(() {
      dio.httpClientAdapter = dioAdapter;
      controller = CreateAppointmentController(dio: dio);
      controller.date.value = DateTime(2022, 1, 1, 9, 0, 0, 0, 0);
      controller.time.value = const TimeOfDay(hour: 12, minute: 0);
      controller.token = "token";
      view = GetMaterialApp(
        initialRoute: Routes.CREATE_APPOINTMENT,
        getPages: AppPages.routes,
      );
      Get.reset();
      Get.testMode = true;
      Get.put(controller);
    });

    tearDown(() {
      Get.reset();
    });

    testWidgets("Testing find doctor feature",
        (tester) async {
      var date = DateTime(2022, 1, 1, 9, 0, 0, 0, 0);
      var time = const TimeOfDay(hour: 12, minute: 0);
      final timestamp =
          '${date.year}-${date.month}-${date.day}_${time.hour}:${time.minute}';
      dioAdapter.onGet("${C.urlA}/app/patient/doctor/$timestamp", (server) {
        server.reply(
          200,
          {
            "data": [
              {"id": 55, "email": "doctor@doctor.com", "name": "Doctor"}
            ]
          },
          delay: const Duration(milliseconds: 500),
          headers: {
            Headers.contentTypeHeader: [Headers.jsonContentType],
            "Authorization": ["Bearer token"]
          },
        );
      });
      await tester.pumpWidget(view);
      var buttons = find.byType(TextButton);
      expect(buttons, findsNWidgets(2));
      controller.token = "token";
      controller.selectTime();
      await tester.pump(const Duration(milliseconds: 500));
      await tester.tap(find.text("OK"));
      controller.selectDate();
      await tester.pump(const Duration(milliseconds: 500));
      await tester.tap(find.text("OK"));
      // controller.getDoctors();
      await tester.pump(const Duration(seconds: 1));
      var listTile = find.byType(ListTile);
      expect(listTile, findsOneWidget);
      expect(controller.doctorData.length, 1);
      expect(controller.doctorData[0].id, 55);
      expect(controller.doctorData[0].name, "Doctor");
    });

    testWidgets("Testing creation of offline booking",
            (tester) async {
          dioAdapter.onPost("${C.urlA}/app/patient/appointment", (server) {
            server.reply(
                200,
                {
                  "data": {
                    "id": 252,
                    "datetime": "2022-12-01_12:0",
                    "session": "ONLINE",
                    "doctor": {
                      "id": 55,
                      "email": "doctor@doctor.com",
                      "name": "Doctor"
                    }
                  }
                },
                delay: const Duration(seconds: 1));
          }, data:
          {
            "doctorId": 55,
            "datetime": "2022-12-01_12:00",
            "session": "ONLINE"
          }
          );
          await tester.pumpWidget(view);
          var date = DateTime(2022, 12, 1, 9, 0, 0, 0, 0);
          var time = const TimeOfDay(hour: 12, minute: 0);
          controller.date.value = date;
          controller.time.value = time;
          controller.doctorData.add(DoctorModel(name: "Doctor", id: 55));
          await tester.pump(Duration(milliseconds: 500));
          var tile = find.byType(ListTile);
          expect(tile, findsOneWidget);
          await tester.tap(tile);
          await tester.pump(Duration(milliseconds: 500));
          var sd = find.byType(SimpleDialog);
          expect(sd, findsOneWidget);
          var offline = find.byType(SimpleDialogOption);
          expect(offline, findsNWidgets(2));
          await tester.tap(offline.at(1));
          await tester.pump(Duration(milliseconds: 1500));
        });

    testWidgets("Testing creation of online booking",
            (tester) async {
          dioAdapter.onPost("${C.urlA}/app/patient/appointment", (server) {
            server.reply(
                200,
                {
                  "data": {
                    "id": 252,
                    "datetime": "2022-12-01_12:0",
                    "session": "OFFLINE",
                    "doctor": {
                      "id": 55,
                      "email": "doctor@doctor.com",
                      "name": "Doctor"
                    }
                  }
                },
                delay: const Duration(seconds: 1));
          }, data:
          {
            "doctorId": 55,
            "datetime": "2022-12-01_12:00",
            "session": "OFFLINE"
          }
          );
          await tester.pumpWidget(view);
          var date = DateTime(2022, 12, 1, 9, 0, 0, 0, 0);
          var time = const TimeOfDay(hour: 12, minute: 0);
          controller.date.value = date;
          controller.time.value = time;
          controller.doctorData.add(DoctorModel(name: "Doctor", id: 55));
          await tester.pump(Duration(milliseconds: 500));
          var tile = find.byType(ListTile);
          expect(tile, findsOneWidget);
          await tester.tap(tile);
          await tester.pump(Duration(milliseconds: 500));
          var sd = find.byType(SimpleDialog);
          expect(sd, findsOneWidget);
          var offline = find.byType(SimpleDialogOption);
          expect(offline, findsNWidgets(2));
          await tester.tap(offline.at(0));
          await tester.pump(Duration(milliseconds: 1500));
        });
  });
}

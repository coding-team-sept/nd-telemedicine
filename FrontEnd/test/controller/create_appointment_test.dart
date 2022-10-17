import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:http_mock_adapter/http_mock_adapter.dart';
import 'package:nd/app/data/const.dart';
import 'package:nd/app/modules/create_appointment/controllers/create_appointment_controller.dart';
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
      controller.date.value = DateTime(2022,1,1,9,0,0,0,0);
      controller.time.value = const TimeOfDay(hour: 11, minute: 0);
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

    testWidgets(
        "Testing to if the system fetch the doctor on date change",
            (tester) async {
              var date = DateTime(2022,1,1,9,0,0,0,0);
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
              await controller.getDoctors();
              await tester.pump(const Duration(seconds: 1));
              var listTile = find.byType(ListTile);
              expect(listTile,findsOneWidget);
              expect(controller.doctorData.length, 1);

            });
  });
}

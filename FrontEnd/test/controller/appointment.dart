import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/create_appointment/controllers/create_appointment_controller.dart';
import 'package:nock/nock.dart';
import 'package:test/test.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  group("Appointment Controller Unit Testing", () {
    late CreateAppointmentController controller;

    setUp(() {
      Get.reset();
      controller = CreateAppointmentController();
      Get.testMode = true;
    });

    test('Testing if the appointment status is already loading ', () {
      expect(controller.isLoading.value, false);
    });

    test('Testing if create appointment is working', () async {
      final date = DateTime.now();
      final time = TimeOfDay.now();
      final timestamp =
          '${date.year}-${date.month}-${date.day}_${time.hour}:${time.minute}';
      nock('http://10.0.2.2:9000/api/v1/patient/appointment')
          .post('/')
          .reply(201, null);
      controller.date.value = date;
      controller.time.value = time;
      controller.token = "fasfsafsa";
      controller.doOfflineBooking(10);
      expect(controller.isLoading.value, true);
    });
    test('Testing if get available doctors is working', () async {
      final date = DateTime.now();
      final time = TimeOfDay.now();
      final timestamp =
          '${date.year}-${date.month}-${date.day}_${time.hour}:${time.minute}';
      nock('http://10.0.2.2:9000/api/v1/patient/doctor/$timestamp')
          .get('/')
          .reply(200, null);
      controller.date.value = date;
      controller.time.value = time;
      controller.token = "fasfsafsa";
      controller.getDoctors();
      expect(controller.isLoading.value, true);
    });
  });
}

import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:nd/app/modules/home/model/appointment_model.dart';
import 'package:nd/app/modules/home/model/offline_model.dart';
import 'package:nd/app/modules/home/widget/appointment-w.dart';
import 'package:nd/app/modules/home/widget/offline-patient.dart';

void main() {
  testWidgets("Test for appointment widget", (widgetTester) async {
    await widgetTester.pumpWidget(MaterialApp(
      home: Scaffold(
          body: OfflineAppointmentTile(
              OfflinePatientAppointmentModel(
                  id: 1,
                  datetime: "12-12-12",
                  patientEmail: "fdasfs@fasfd.com",
                  patientId: 12,
                  patientName: "fdsafsa"),
              (int) => print("done"))),
    ));
    expect(find.byType(ListTile), findsOneWidget);
    expect(find.byType(Text), findsNWidgets(2));
  });
}

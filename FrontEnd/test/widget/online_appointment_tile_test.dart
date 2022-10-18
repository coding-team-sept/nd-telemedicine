import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:nd/app/modules/home/model/appointment_model.dart';
import 'package:nd/app/modules/home/model/offline_model.dart';
import 'package:nd/app/modules/home/model/online_patient_appointment_model.dart';
import 'package:nd/app/modules/home/widget/appointment-w.dart';
import 'package:nd/app/modules/home/widget/offline-patient.dart';
import 'package:nd/app/modules/home/widget/online_patient_apppoinment_w.dart';

void main() {
  testWidgets("Test for online appointment widget", (widgetTester) async {
    await widgetTester.pumpWidget(MaterialApp(
      home: Scaffold(
          body: OnlinePatientAppointmentTile(
              OnlinePatientAppointmentModel(
                  id: 1,
                  datetime: "12-12-12",
                  patientEmail: "fdasfs@fasfd.com",
                  patientId: 12,
                  patientName: "fdsafsa", session: 'ONLINE'),
              (a,b) => print("done"))),
    ));
    expect(find.byType(ListTile), findsOneWidget);
    expect(find.byType(Text), findsNWidgets(2));
  });
}

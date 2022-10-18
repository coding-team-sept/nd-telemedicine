import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:nd/app/modules/home/model/appointment_model.dart';
import 'package:nd/app/modules/home/widget/appointment-w.dart';

void main() {
  testWidgets("Test for appointment widget", (widgetTester) async {
    await widgetTester.pumpWidget(MaterialApp(
      home: Scaffold(
        body: AppointmentTile(
            AppointmentModel(
                id: 1,
                name: "arvel",
                time: "12-12-12",
                doctorID: 12,
                isOnline: true),
            (p0, p1, p2) => print("done")),
      ),
    ));
    expect(find.byType(ListTile), findsOneWidget);
    expect(find.byType(Text), findsNWidgets(3));

  });
}

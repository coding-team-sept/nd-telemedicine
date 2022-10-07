import 'package:flutter/material.dart';
import 'package:nd/app/modules/home/model/online_patient_appointment_model.dart';

class OnlinePatientAppointmentTile extends StatelessWidget {
  final OnlinePatientAppointmentModel data;
  final Function(int) tapCallback;
  const OnlinePatientAppointmentTile(this.data, this.tapCallback, {key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () => tapCallback(data.id),
      title: Text("${data.patientName}"),
      subtitle: Text(data.datetime),
    );
  }
}
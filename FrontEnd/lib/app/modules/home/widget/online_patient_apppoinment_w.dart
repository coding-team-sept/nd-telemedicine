import 'package:flutter/material.dart';
import 'package:nd/app/modules/home/model/online_patient_appointment_model.dart';

class OnlinePatientAppointmentTile extends StatelessWidget {
  final OnlinePatientAppointmentModel data;
  const OnlinePatientAppointmentTile(this.data, this.tapCallback, {key})
      : super(key: key);
  final Function(int, int) tapCallback;

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () => tapCallback(data.id, data.patientId),
      title: Text("${data.patientName}"),
      subtitle: Text(data.datetime),
    );
  }
}


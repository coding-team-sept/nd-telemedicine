import 'package:flutter/material.dart';
import 'package:nd/app/modules/home/model/appointment_model.dart';

class AppointmentTile extends StatelessWidget {
  final AppointmentModel data;
  final Function(int, bool, int) tapCallback;
  const AppointmentTile(this.data, this.tapCallback, {key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () => tapCallback(data.id, data.isOnline, data.doctorID),
      title: Text("Dr. ${data.name}"),
      subtitle: Text(data.time),
      trailing: Chip(
        label: Text(data.isOnline ? "ONLINE" : "OFFLINE"),
      ),
    );
  }
}

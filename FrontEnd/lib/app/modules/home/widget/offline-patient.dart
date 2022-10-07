
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../model/offline_model.dart';

class OfflineAppointmentTile extends StatelessWidget {
  final OfflinePatientAppointmentModel data;
  final Function(int) tapCallback;
  const OfflineAppointmentTile(this.data, this.tapCallback, {key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return ListTile(
      onTap: () => tapCallback(data.id),
      title: Text(data.patientName),
      subtitle: Text(data.datetime),
    );
  }
}
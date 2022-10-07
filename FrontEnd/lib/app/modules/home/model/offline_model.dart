class OfflinePatientAppointmentModel {
  int id;
  String datetime;
  int patientId;
  String patientEmail;
  String patientName;

  OfflinePatientAppointmentModel({
    required this.id,
    required this.datetime,
    required this.patientId,
    required this.patientEmail,
    required this.patientName,
  });
}

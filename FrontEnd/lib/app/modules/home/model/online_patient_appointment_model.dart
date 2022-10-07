class OnlinePatientAppointmentModel{
  int id;
  String datetime;
  String session;
  int patientId;
  String patientEmail;
  String patientName;

  OnlinePatientAppointmentModel(
      {required this.id,
        required this.datetime,
        required this.session,
        required this.patientId,
        required this.patientEmail,
        required this.patientName,
      });

}
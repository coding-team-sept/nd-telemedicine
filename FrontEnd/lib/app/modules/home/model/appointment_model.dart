class AppointmentModel {
  int id;
  String name;
  int doctorID;
  String time;
  bool isOnline;

  AppointmentModel(
      {required this.id,
      required this.name,
      required this.time,
      required this.doctorID,
      required this.isOnline});
}

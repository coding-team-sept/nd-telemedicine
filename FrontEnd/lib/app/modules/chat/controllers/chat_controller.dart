import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:flutter_chat_types/flutter_chat_types.dart' as types;
import 'package:nd/app/data/const.dart';

class ChatController extends GetxController {
  final patientUser = const types.User(id: 'p');
  final doctorUser = const types.User(id: 'd');
  RxList<types.Message> messages = (List<types.Message>.of([])).obs;

  late final int appointmentID;
  late final String token;
  late int doctorID;
  late int patientID;
  late final bool isDoctor;
  final bool debug;
  final Dio dio;

  ChatController(
      {Dio? dio,
      String? token,
      int? appointmentID,
      bool? isDoctor,
      int? doctorID,
      int? patientID})
      : dio = dio ?? Dio(),
        debug = dio != null,
        isDoctor = isDoctor ?? Get.arguments['doctorID'],
        doctorID = doctorID ?? Get.arguments['doctorID'],
        appointmentID = appointmentID ?? Get.arguments['appointmentID'],
        token = token ?? Get.arguments['token'],
        patientID = patientID ?? Get.arguments['patientID'];


  final isLoading = false.obs;
  bool checkMessage = true;

  @override
  void onInit() async {
    if (!debug) {
      isLoading.value = true;
      appointmentID = Get.arguments['appointmentID'];
      token = Get.arguments['token'];
      isDoctor = Get.arguments['isDoctor'];
      doctorID = Get.arguments['doctorID'];
      patientID = Get.arguments['patientID'];
      await getStatus();
      await getMessages();
      isLoading.value = false;
      getNewMessages();
    }
    super.onInit();
  }

  Future getNewMessages() async {
    if (!checkMessage) return;
    try {
      var response = await dio.get("${C.urlC}/app/chat/message/$appointmentID",
          queryParameters: {
            "isAll": "false",
          },
          options: Options(headers: {"Authorization": "Bearer $token"}));

      for (var element in response.data['data']['messages']) {
        if (doctorID == -999) {
          if (element['senderId'] != patientID) {
            addMessage(createMessage(element['message'], true));
          } else {
            addMessage(createMessage(element['message'], false));
          }
        } else {
          if (element['senderId'] == doctorID) {
            addMessage(createMessage(element['message'], true));
          } else {
            addMessage(createMessage(element['message'], false));
          }
        }
      }
    } on DioError catch (_) {
      await Get.dialog(const AlertDialog(
          title: Text(
        "Unknown Error",
      )));
      Get.back();
    }
    await Future.delayed(const Duration(seconds: 1));
    getNewMessages();
  }

  Future getStatus() async {
    try {
      await dio.get("${C.urlC}/app/chat/status/$appointmentID",
          options: Options(headers: {"Authorization": "Bearer $token"}));
      // JUst ignore uread not important
    } on DioError catch (_) {
      await Get.dialog(const AlertDialog(
          title: Text(
        "Unknown Error",
      )));
      Get.back();
    }
  }

  Future getMessages() async {
    try {
      var response = await dio.get("${C.urlC}/app/chat/message/$appointmentID",
          options: Options(headers: {"Authorization": "Bearer $token"}));

      messages.clear();
      for (var element in response.data['data']['messages']) {
        if (doctorID == -999) {
          if (element['senderId'] != patientID) {
            addMessage(createMessage(element['message'], true));
          } else {
            addMessage(createMessage(element['message'], false));
          }
        } else {
          if (element['senderId'] == doctorID) {
            addMessage(createMessage(element['message'], true));
          } else {
            addMessage(createMessage(element['message'], false));
          }
        }
      }
    } on DioError catch (_) {
      await Get.dialog(const AlertDialog(
          title: Text(
        "Unknown Error",
      )));
      Get.back();
    }
  }

  void addMessage(types.Message message) {
    messages.insert(0, message);
  }

  Future handleSendPressed(types.PartialText message) async {
    try {
      await dio.post("${C.urlC}/app/chat/message/",
          data: {"appointmentId": appointmentID, "message": message.text},
          options: Options(headers: {"Authorization": "Bearer $token"}));
      addMessage(createMessage(message.text, isDoctor));
    } on DioError catch (_) {
      await Get.dialog(const AlertDialog(
          title: Text(
        "Unable to send message",
      )));
      Get.back();
    }
  }

  types.Message createMessage(String text, bool isDoctor) {
    final m = types.TextMessage(
      author: isDoctor ? doctorUser : patientUser,
      createdAt: DateTime.now().millisecondsSinceEpoch,
      id: isDoctor ? "doctor" : "patient",
      text: text,
    );
    return m;
  }
}

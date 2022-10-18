import 'package:flutter/material.dart';
import 'package:flutter_chat_ui/flutter_chat_ui.dart';

import 'package:get/get.dart';

import '../controllers/chat_controller.dart';
//chat view link to chat controller
class ChatView extends GetView<ChatController> {
  const ChatView({Key? key}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Chat'),
          centerTitle: true,
        ),
        body: Obx(
          //if the state is loading show progress indicator
          () => controller.isLoading.value
              ? const Center(
              //the shape for loading is circular
                  child: CircularProgressIndicator(),
                )
              : Chat(
            //using controller to get user for Chat function, to determine who is doctor or patient
                  user: controller.isDoctor
                      ? controller.doctorUser
                      : controller.patientUser,
                  messages: controller.messages.value,
                  //onSend Pressed is a function that will have a partial text message as a parameter
                  onSendPressed: controller.handleSendPressed,
                ),
        ));
  }
}

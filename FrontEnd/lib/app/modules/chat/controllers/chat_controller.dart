import 'package:get/get.dart';
import 'package:flutter_chat_types/flutter_chat_types.dart' as types;

class ChatController extends GetxController {
  final user = const types.User(id: 'blbla');
  RxList<types.Message> messages = (List<types.Message>.of([])).obs;

  void addMessage(types.Message message) {
    messages.insert(0, message);
  }

  void handleSendPressed(types.PartialText message) {
    final textMessage = types.TextMessage(
      author: user,
      createdAt: DateTime.now().millisecondsSinceEpoch,
      id: 'thatsh',
      text: message.text,
    );

    addMessage(textMessage);
  }
}

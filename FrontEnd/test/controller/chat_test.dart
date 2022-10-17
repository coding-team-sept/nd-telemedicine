import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:http_mock_adapter/http_mock_adapter.dart';
import 'package:nd/app/data/const.dart';
import 'package:nd/app/modules/chat/controllers/chat_controller.dart';
import 'package:nd/app/modules/chat/views/chat_view.dart';
import 'package:nd/app/routes/app_pages.dart';
import 'package:flutter_chat_types/flutter_chat_types.dart' as types;

void main() {
  group("Unit test for chat test", () {
    late ChatController controller;
    setUp(() {
      Get.reset();
      controller = ChatController(
          isDoctor: true, patientID: 123, doctorID: 234, dio: Dio());
    });

    test("Testing if the loading status on chat controller is initially false",
        () {
      expect(controller.isLoading.value, false);
    });

    test("Testing for create message",(){
      var messsage = controller.createMessage("hello", true);
      expect(messsage.author, const types.User(id: 'd'));
    });
  });

  group("Widget Testing for chat", () {
    late ChatController controller;
    late GetMaterialApp view;
    final dio = Dio();
    final dioAdapter = DioAdapter(dio: dio);

    setUp(() {
      dio.httpClientAdapter = dioAdapter;
      controller =
          ChatController(dio: dio, isDoctor: true, doctorID: 1, patientID: 2);
      view = GetMaterialApp(
        home: const ChatView(),
        getPages: AppPages.routes,
      );
      Get.reset();
      Get.testMode = true;
      Get.put(controller);
    });
    tearDown(() {
      Get.reset();
    });

    testWidgets("Testing chat view appbar", (widgetTester) async {
      await widgetTester.pumpWidget(view);
      expect(find.byType(AppBar), findsOneWidget);
      expect(find.text("Chat"), findsOneWidget);
    });

    testWidgets("Testing get status", (tester) async {
      dioAdapter.onGet(
        "${C.urlC}/app/chat/status/123",
        (server) {
          server.reply(
            200,
            {},
            delay: const Duration(seconds: 1),
            headers: {
              Headers.contentTypeHeader: [Headers.jsonContentType],
              "Authorization": ["Bearer token"]
            },
          );
        },
      );
      await tester.pumpWidget(view);
      controller.getStatus();
      await tester.pump(Duration(milliseconds: 1500));
      expect(controller.isLoading.value, false);
    });
  });
}

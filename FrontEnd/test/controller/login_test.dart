
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:get/get.dart';
import 'package:nd/app/modules/login/controllers/login_controller.dart';
import 'package:nd/app/modules/login/views/login_view.dart';
import 'package:nock/nock.dart';

void main(){
  group("Unit test for login test", () {
    late LoginController controller;
    setUp((){
      Get.reset();
      controller = LoginController();
    });

  test("Testing if the loading status on login controller is initially false",
          () {
    expect(controller.isLoading.value, false);
  } );
  
  test("Testing if the email format on login controller is valid",
          () {
    Get.put(controller);
    controller.onEmailChange("bai@");
    try{
      controller.login();
    }catch(_){}
    expect(controller.emailError.value, "Please enter a valid email");
    controller.onEmailChange("bai.com");
    try{
      controller.login();
    }catch(_){}
    expect(controller.emailError.value, "Please enter a valid email");
    controller.onEmailChange("bai@bai.com");
    try{
      controller.login();
    }catch(_){}
    expect(controller.emailError.value, '');
  });

  test("Testing if the password on login controller is working",
          () {
    Get.put(controller);
    controller.onPasswordChange("123456");
    try{
      controller.login();
    }catch(_){}
    expect(controller.passwordError.value, "Password should be 8 to 24 characters");
    controller.onPasswordChange("12345678911111111111111111");
    try{
      controller.login();
    }catch(_){}
    expect(controller.passwordError.value, "Password should be 8 to 24 characters");
    controller.onPasswordChange("123456789");
    try{
      controller.login();
    }catch(_){}
    expect(controller.passwordError.value, "");
  });

  test("Testing if the password on login controller is empty",
          () {
        controller.onPasswordChange("");
        try{
          controller.login();
        }catch(_){}
        expect(controller.passwordError.value, "Password should be 8 to 24 characters");
  });

  test("Testing if the show password is hidden and initially true",
          () {
    expect(controller.showPassword.value, true);
  });

    test("Test login test", () async{
      nock("http://10.0.2.2:9000/api/v1/login")
          .post("/")
          .reply(201, null);
      controller.onEmailChange("patient@test.com");
      controller.onPasswordChange("12345678");
      try {
        controller.login();
      } catch (_) {}
      expect(controller.isLoading.value, true);
    });

  });

  group("Testing Login work",(){
    late LoginController controller;
    late GetMaterialApp view;

    setUpAll(nock.init);

    setUp((){
      nock.cleanAll();
      controller = LoginController();
      view = GetMaterialApp(
        home: LoginView(),
      );
      Get.reset();
      Get.testMode = true;
      Get.put(controller);
    });

    tearDown((){
      Get.reset();
    });

    testWidgets("Testing if application can login successfully", (tester) async{
      nock("http://10.0.2.2:9000/api/v1/login").post("/").reply(201, null);
      await tester.pumpWidget(view);
      var textFields = find.byType(TextField);
      expect(textFields, findsNWidgets(2));
      await tester.enterText(textFields.at(0), "email@gmail.com");
      await tester.enterText(textFields.at(1), "12345678");
      var loginButton = find.byType(ElevatedButton);
      await tester.tap(loginButton);
      await tester.pump(Duration(milliseconds: 500));
      expect(controller.isLoading.value, true);
      await tester.pump(Duration(seconds: 3));

    });

    testWidgets(
        "Testing if the Login error is displayed when the text field is all empty",
            (tester) async{
      await tester.pumpWidget(view);
      expect(find.text("Welcome"), findsOneWidget);
      var textFields = find.byType(TextField);
      expect(textFields, findsNWidgets(2));
      var loginButton = find.byType(ElevatedButton);
      expect(loginButton, findsOneWidget);
      await tester.tap(loginButton);
      await tester.pump(Duration(seconds: 1));
      expect((textFields.evaluate().elementAt(0).widget as TextField)
          .decoration
          ?.errorText ??
          "",
          "Please enter a valid email");
      expect((textFields.evaluate().elementAt(1).widget as TextField)
          .decoration
          ?.errorText ??
          "",
          "Password should be 8 to 24 characters");
      var dialog = find.byType(AlertDialog);
      expect(dialog, findsOneWidget);
      var dialogWidget = dialog.evaluate().elementAt(0).widget as AlertDialog;
      expect((dialogWidget.title as Text).data ?? "", "Sign In Failed");
      expect((dialogWidget.content as Text).data ?? "", "Please check your email and password");
      Get.back();
      await tester.pump();
      dialog = find.byType(AlertDialog);
      expect(dialog, findsNothing);
    });


  });

  
}
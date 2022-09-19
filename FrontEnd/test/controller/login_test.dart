import 'package:get/instance_manager.dart';
import 'package:nd/app/modules/login/controllers/login_controller.dart';
import 'package:test/test.dart';

void main(){
  test("Testing if the loading status on login controller is initially false",
          () {
    final controller = LoginController();
    expect(controller.isLoading.value, false);
  } );
  
  test("Testing if the email format on login controller is valid",
          () {
    final controller = LoginController();
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
    final controller = LoginController();
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
        final controller = LoginController();
        controller.onPasswordChange("");
        try{
          controller.login();
        }catch(_){}
        expect(controller.passwordError.value, "Password should be 8 to 24 characters");
  });

  test("Testing if the show password is hidden and initially true",
          () {
    final controller = LoginController();
    expect(controller.showPassword.value, true);
  });
  
  
}
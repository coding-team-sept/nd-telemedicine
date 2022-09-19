import 'package:get/get.dart';
import 'package:nd/app/modules/register/controllers/register_controller.dart';
import 'package:test/test.dart';


void main(){
  test(
      'Testing if the register status is already loading ',
          (){
        final controller = RegisterController();
        expect(controller.isLoading.value, false);
      }
  );


  test(
      'Testing full name authentication is correct',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onNameChange('ab');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.fullNameError.value, 'Please input at least 3 characters');
      }
  );

  test(
      'Testing full name is empty',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onNameChange('');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.fullNameError.value, 'Please input at least 3 characters');
      }
  );

  test(
      'Testing email authentication if correct when I do not input @ and not xxx.com',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onEmailChange('aaaa');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.emailError.value, 'Please input a valid email');
      }
  );

  test(
      'Testing email authentication if correct when I do not input xxx.com',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onEmailChange('aaaa@');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.emailError.value, 'Please input a valid email');
      }
  );

  test(
      'Testing email authentication if correct when I do not input .com',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onEmailChange('aaaa@dwe');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.emailError.value, 'Please input a valid email');
      }
  );

  test(
      'Testing input a empty email',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onEmailChange('');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.emailError.value, 'Please input a valid email');
      }
  );

  test(
      'Testing input a valid email ',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onEmailChange('aaaa@dwe.com');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.emailError.value, '');
      }
  );

  test(
      'Testing Password length < 8 characters ',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onPasswordChange('1234567');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.passwordError.value, 'Password length must 8-24 characters');
      }
  );

  test(
      'Testing Password length > 24 characters ',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onPasswordChange('1234567891234567890000000');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.passwordError.value, 'Password length must 8-24 characters');
      }
  );

  test(
      'Testing Password is empty ',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onPasswordChange('');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.passwordError.value, 'Please input a non-empty value');
      }
  );



  test(
      'Testing : 8< Password length <24 characters and confirm_password is same as password',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onPasswordChange('123456789123456789');
        controller.onConfirmPasswordChange('123456789123456789');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.passwordError.value,'');
      }
  );

  test(
      'Testing : 8< Password length <24 characters But confirm_password is not same as password',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onPasswordChange('123456789123456789');
        controller.onConfirmPasswordChange('11111111');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.passwordError.value,'Please make sure password is the same');
      }
  );

  test(
      'Testing : 8< Password length <24 characters But confirm_password is not same as password',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onPasswordChange('123456789123456789');
        controller.onConfirmPasswordChange('11111111');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.confirmPasswordError.value,'Please make sure password is the same');
      }
  );


  test(
      'Testing : 8< Password length <24 characters But confirm_password is empty',
          (){
        final controller = RegisterController();
        Get.put(controller);
        controller.onPasswordChange('123456789123456789');
        controller.onConfirmPasswordChange('');
        try{
          controller.signUp();
        }catch(_){}
        expect(controller.confirmPasswordError.value,'Please input a non-empty value');
      }
  );

}
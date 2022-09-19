import 'package:nd/app/modules/add-doctor/controllers/add_doctor_controller.dart';
import 'package:test/test.dart';

void main(){
  test("Testing the doctor name is less than 4 characters and match the error hint",
          () {
        final controller = AddDoctorController();
        controller.nameChanged("tes");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.nameError.value, "Name should be at least 4");
      });

  test("Testing the doctor name is entered correctly",
          () {
        final controller = AddDoctorController();
        controller.nameChanged("test");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.nameError.value, null);
      });

  test("Testing the doctor email format on add doctor controller is valid",
          () {
        final controller = AddDoctorController();
        controller.emailChanged("bai2");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.emailError.value, "Please input a valid email");
      });
  test("Testing if the doctor email format without @",
          () {
        final controller = AddDoctorController();
        controller.emailChanged("bai2.com");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.emailError.value, "Please input a valid email");
      });
  test("Testing if the doctor email format enter correctly",
          () {
        final controller = AddDoctorController();
        controller.emailChanged("bai2@bai.com");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.emailError.value, null);
      });

  test("Testing if the doctor password on add doctor controller less than 8 characters",
  () {
    final controller = AddDoctorController();
    controller.passwordChanged("123456");
    try{
      controller.validate();
    }catch(_){}
    expect(controller.passwordError.value, "Password length must be 8-24 characters");
  });
  test("Testing if the doctor password on add doctor controller more than 24 characters",
          () {
        final controller = AddDoctorController();
        controller.passwordChanged("1234564324325453253252352323523");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.passwordError.value, "Password length must be 8-24 characters");
      });
  test("Testing if the doctor password on add doctor controller is working",
          () {
        final controller = AddDoctorController();
        controller.passwordChanged("12345678");
        try{
          controller.validate();
        }catch(_){}
        expect(controller.passwordError.value, null);
      });

  test("Testing if the loading status on add doctor controller is initially false",
          () {
        final controller = AddDoctorController();
        expect(controller.isLoading.value, false);
      } );


}
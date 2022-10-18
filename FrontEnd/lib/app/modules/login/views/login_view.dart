import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/login_controller.dart';

class LoginView extends GetView<LoginController> {
  const LoginView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    //Scaffold is a class in flutter which provides many widgets
    return Scaffold(
        body: Stack(children:[ Container(
      width: double.infinity,
      padding: const EdgeInsets.only(top: 80, bottom: 24, left: 32, right: 32),
      decoration: BoxDecoration(
            borderRadius:
                BorderRadius.circular(24) //change the shape of container
            ),
      child: Flex(
          direction: Axis.vertical,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Expanded(
              child: ListView(
                children: [
                  Text("Welcome", style: Get.theme.textTheme.displaySmall),
                  const SizedBox(
                    height: 8,
                  ),
                  const Text(
                    "Hello there, login to continue!",
                    style: TextStyle(fontWeight: FontWeight.w700),
                  ),
                  const SizedBox(
                    height: 32,
                  ),
                  const Text(
                    "Email:",
                    style: TextStyle(fontWeight: FontWeight.w600),
                  ),
                  const SizedBox(
                    height: 8,
                  ),
                  Obx(
                    () => TextField(
                      onChanged: controller.onEmailChange,
                      decoration: InputDecoration(
                        filled: true, //fill the background colour
                        hintText: "johndoe@example.com",
                        //Check if emailError is "", if it is then it is null, if not then it shows emailError
                        errorText: controller.emailError.value == ""
                            ? null
                            : controller.emailError.value,
                      ),
                    ),
                  ),
                  const SizedBox(
                    height: 32,
                  ),
                  const Text(
                    "Password:",
                    style: TextStyle(fontWeight: FontWeight.w600),
                  ),
                  const SizedBox(
                    height: 8,
                  ),
                  Obx(
                    () => TextField(
                      obscureText: controller.showPassword.value,
                      onChanged: controller.onPasswordChange,
                      decoration: InputDecoration(
                          suffixIcon: IconButton(
                            onPressed: controller.toggleShowPassword,
                            icon: const Icon(Icons.remove_red_eye),
                          ),
                          filled: true, //fill the background colour
                          hintText: "********",
                          //Check if passwordError is "", if it is then it is null, if not then it shows passwordError
                          errorText: controller.passwordError.value == ""
                              ? null
                              : controller.passwordError.value),
                    ),
                  ),
                  const SizedBox(
                    height: 40,
                  ),
                  SizedBox(
                    height: 40,
                    width: double.infinity,
                    child: Obx(
                      ()=> ElevatedButton(
                        //isLoading runs and then runs controller.login
                        onPressed: controller.isLoading.value ? null : controller.login,
                        child: const Text("Login"),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const Text("Don't have an account?"),
                TextButton(
                  onPressed: controller.signUp,
                  child: const Text("Sign up"),
                )
              ],
            )
          ],
      ),
    ),
      Obx(
      ()=> Center(child: Container(
        alignment: Alignment.center,
        width: controller.isLoading.value ? 110 : 0,
        height: controller.isLoading.value ? 110 : 0,
        child: CircularProgressIndicator(),
        decoration: BoxDecoration(
            boxShadow: [
              BoxShadow(
                  color: Colors.black.withOpacity(0.4),
                  blurRadius: 7,
                  spreadRadius: 1)
            ],
            //border: Border.all(color: Colors.black),
            color: Colors.white,
            borderRadius: BorderRadius.circular(20)),
    ),)
      )]
      )
    );
  }
}

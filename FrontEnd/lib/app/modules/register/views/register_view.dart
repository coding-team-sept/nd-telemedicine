import 'package:flutter/material.dart';

import 'package:get/get.dart';

import '../controllers/register_controller.dart';

class RegisterView extends GetView<RegisterController> {
  const RegisterView({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        //backgroundColor: Colors.blue.shade900,
        body: Stack(
          children:[ Container(
              width: double.infinity,
              padding: const EdgeInsets.all(32),
              decoration: BoxDecoration(borderRadius: BorderRadius.circular(32)),
              child: Flex(
                direction: Axis.vertical,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Expanded(
                    child: ListView(
                      children: [
                        Text('Welcome', style: Get.theme.textTheme.displaySmall),
                        const SizedBox(
                          height: 8,
                        ),
                        Text('Hello there, Create an account',
                            style: Get.theme.textTheme.bodyText1),
                        const SizedBox(
                          height: 32,
                        ),

                        const Text(
                          'Full name:',
                          style: TextStyle(
                              fontWeight: FontWeight.w700),
                        ),
                        const SizedBox(
                          height: 8,
                        ),

                        Obx(
                          () => TextField(
                            onChanged: controller.onNameChange,
                            decoration: InputDecoration(
                              filled: true,
                              hintText: "Enter your full name",
                              errorText: controller.fullNameError.value == ""
                                  ? null
                                  : controller.fullNameError.value,
                            ),
                          ),
                        ),
                        const SizedBox(
                          height: 8,
                        ),
                        const Text(
                          'Email:',
                          style: TextStyle(
                              fontWeight: FontWeight.w700),
                        ),
                        const SizedBox(
                          height: 8,
                        ),

                        Obx(
                          () => TextField(
                            onChanged: controller.onEmailChange,
                            decoration: InputDecoration(
                              filled: true,
                              hintText: "Enter your email",
                              errorText: controller.emailError.value == ""
                                  ? null
                                  : controller.emailError.value,
                            ),
                          ),
                        ),

                        const SizedBox(
                          height: 8,
                        ),
                        const Text(
                          'Password:',
                          style: TextStyle(
                              fontWeight: FontWeight.w700),
                        ),
                        const SizedBox(
                          height: 8,
                        ),

                        Obx(
                          () => TextField(
                            onChanged: controller.onPasswordChange,
                            obscureText: controller.showPassword.value,
                            decoration: InputDecoration(
                              suffixIcon: IconButton(
                                icon: const Icon(Icons.remove_red_eye),
                                onPressed: controller.toggleShowPassword,
                              ),
                              filled: true,
                              hintText: "Enter your password",
                              errorText: controller.passwordError.value == ""
                                  ? null
                                  : controller.passwordError.value,
                            ),
                          ),
                        ),

                        const SizedBox(
                          height: 8,
                        ),
                        const Text(
                          'Confirm Password:',
                          style: TextStyle(
                              fontWeight: FontWeight.w700),
                        ),
                        const SizedBox(
                          height: 8,
                        ),

                        Obx(
                          () => TextField(
                            onChanged: controller.onConfirmPasswordChange,
                            obscureText: controller.showConfirmPassword.value,
                            decoration: InputDecoration(
                              suffixIcon: IconButton(
                                icon: const Icon(Icons.remove_red_eye),
                                onPressed: controller.toggleShowConfirmPassword,
                              ),
                              filled: true,
                              hintText: "Confirm your password",
                              errorText:
                                  controller.confirmPasswordError.value == ""
                                      ? null
                                      : controller.confirmPasswordError.value,
                            ),
                          ),
                        ),
                        const SizedBox(
                          height: 32,
                        ),

                        SizedBox(
                          height: 40,
                          width: double.infinity,
                          child: Obx(
                            ()=> ElevatedButton(
                              onPressed: controller.isLoading.value? null: controller.signUp,
                              child: const Text("SIGN UP"),
                            ),
                          ),
                        ),

                        //yong size box baoqilai ranhou infinty qu lakai
                      ],
                    ),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Text("Already have an account ?"),
                      TextButton(onPressed: controller.login, child: const Text('Log In'))
                    ],
                  )
                ],
              )),
            Obx(
                  ()=>Center(
                      child: Container(
                alignment: Alignment.center,width: controller.isLoading.value? 110:0,height: controller.isLoading.value? 110 :0,
                decoration: BoxDecoration(
                  boxShadow: [BoxShadow(color: Colors.black.withOpacity(0.4),blurRadius: 7,spreadRadius: 0.3)],
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(20),
                ),
                child: const CircularProgressIndicator(),)
              ),
            )
          ],

        ));
  }
}

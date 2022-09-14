import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:get/get.dart';
import 'package:json_theme/json_theme.dart';

import 'app/routes/app_pages.dart';

void main() async {
  // Load modifiable theme data from JSON
  WidgetsFlutterBinding.ensureInitialized();
  final themeDataString = await rootBundle.loadString('assets/theme.json');
  final themeData = ThemeDecoder.decodeThemeData(jsonDecode(themeDataString));

  runApp(
    GetMaterialApp(
      title: "ND Telemedicine",
      initialRoute: AppPages.INITIAL,
      getPages: AppPages.routes,
      theme: themeData,
    ),
  );
}

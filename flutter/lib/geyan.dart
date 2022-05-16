import 'dart:async';
import 'package:flutter/services.dart';

class geyan {
  static const MethodChannel _channel = MethodChannel('geyan_plugin');

  static Future get platformVersion async {
     await _channel.invokeMethod('initPlatform');
  }
}

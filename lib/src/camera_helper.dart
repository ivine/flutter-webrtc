import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class CameraHelper {
  static const MethodChannel _channel = MethodChannel("FlutterWebRTC.Method");

  static Future<String?> getCameraDeviceInfo(String deviceId) async {
    try {
      final result = await _channel.invokeMethod<Map>("getCameraDeviceInfo", {
        "deviceId": deviceId,
      });
      if (result == null) return null;
      final resultString = jsonEncode(result);
      return resultString;
    } catch (e) {
      debugPrint("getCameraDeviceInfo error: $e");
      return null;
    }
  }
}

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class CameraInfo {
  CameraInfo({
    required this.currentZoom,
    required this.minZoom,
    required this.maxZoom,
    required this.maxFPS,
  });
  final double currentZoom;
  final double minZoom;
  final double maxZoom;
  final int maxFPS;
}

class CameraHelper {
  static const MethodChannel _channel = MethodChannel("FlutterWebRTC.Method");

  static Future<CameraInfo?> getCameraInfo(String deviceId) async {
    try {
      final result = await _channel.invokeMethod<Map>("getCameraInfo", {
        "deviceId": deviceId,
      });

      if (result == null) return null;

      return CameraInfo(
        currentZoom: (result["currentZoom"] as num).toDouble(),
        minZoom: (result["minZoom"] as num).toDouble(),
        maxZoom: (result["maxZoom"] as num).toDouble(),
        maxFPS: (result["maxFPS"] as num).toInt(),
      );
    } catch (e) {
      debugPrint("getCameraInfo error: $e");
      return null;
    }
  }
}

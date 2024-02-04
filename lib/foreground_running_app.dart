
import 'foreground_running_app_platform_interface.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'dart:io' show Platform;

/// Custom Exception for the plugin.
/// Thrown whenever the plugin is used on platforms other than Android.
class ForegroundRunningAppException implements Exception {
  String _cause;

  ForegroundRunningAppException(this._cause);

  @override
  String toString() => _cause;
}

class RunningAppInfo {
  late String packageName;

  RunningAppInfo(String packageName) {
    this.packageName = packageName;
  }

  String getPackageName() {
    return packageName;
  }

}

class ForegroundRunningApp {

  static const MethodChannel _channel = MethodChannel("running_app.methodChannel");

  static final ForegroundRunningApp _instance = ForegroundRunningApp._();
  ForegroundRunningApp._();
  factory ForegroundRunningApp() => _instance;

  Future<String> getRunningApp() async {
    if (Platform.isAndroid) {
      return await _channel.invokeMethod('getRunningApp');
    } else {
      throw ForegroundRunningAppException("This plugin is only supported on Android.");
    }
  }




}

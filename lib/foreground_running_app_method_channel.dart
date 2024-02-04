import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'foreground_running_app_platform_interface.dart';

/// An implementation of [ForegroundRunningAppPlatform] that uses method channels.
class MethodChannelForegroundRunningApp extends ForegroundRunningAppPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('running_app.methodChannel');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getRunningApp');
    return version;
  }
}

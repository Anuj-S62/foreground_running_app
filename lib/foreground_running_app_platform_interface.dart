import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'foreground_running_app_method_channel.dart';

abstract class ForegroundRunningAppPlatform extends PlatformInterface {
  /// Constructs a ForegroundRunningAppPlatform.
  ForegroundRunningAppPlatform() : super(token: _token);

  static final Object _token = Object();

  static ForegroundRunningAppPlatform _instance = MethodChannelForegroundRunningApp();

  /// The default instance of [ForegroundRunningAppPlatform] to use.
  ///
  /// Defaults to [MethodChannelForegroundRunningApp].
  static ForegroundRunningAppPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [ForegroundRunningAppPlatform] when
  /// they register themselves.
  static set instance(ForegroundRunningAppPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}


import 'foreground_running_app_platform_interface.dart';

class ForegroundRunningApp {
  Future<String?> getPlatformVersion() {
    return ForegroundRunningAppPlatform.instance.getPlatformVersion();
  }
}

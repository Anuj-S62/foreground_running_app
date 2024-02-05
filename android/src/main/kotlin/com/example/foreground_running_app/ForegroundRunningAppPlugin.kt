package com.example.foreground_running_app

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.embedding.engine.plugins.activity.ActivityAware



import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import android.app.usage.UsageStatsManager
import android.app.usage.UsageEvents
import android.content.ContextWrapper
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.util.Log



/** ForegroundRunningAppPlugin */
public class ForegroundRunningAppPlugin: FlutterPlugin, MethodCallHandler,ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel: MethodChannel
  private lateinit var context: Context
  private val methodChannelName = "running_app.methodChannel"
  private lateinit var activity: Activity

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, methodChannelName)
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.applicationContext;
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getRunningApp") {
      getRunningApp(call,result)
    } else {
      result.notImplemented()
    }
  }

  fun getRunningApp(@NonNull call: MethodCall, @NonNull result: Result) {
    handlePermissions()

    val runningApp = getCurrentRunningApp()

    if (runningApp != "") {
      result.success(runningApp)
    } else {
      result.error("UNAVAILABLE", "Running app not available.", "Unknown App")
    }
  }


  private fun requestUsageStatsPermission() {
    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    this.activity.startActivity(intent)
  }

  fun handlePermissions() {
    if(!isUsageStatsPermissionGranted()){
        requestUsageStatsPermission()
    }
  }

  public fun getCurrentRunningApp(): String {
    val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val endTime = System.currentTimeMillis()
    val startTime = endTime - 100000  // Look back in time for the last 10 seconds
    val usageEvents = usageStatsManager.queryEvents(startTime, endTime)
    Log.d(UsageEvents.Event().packageName,"usageEvents.hasNextEvent()")
    var currentApp = ""
    var eventTime = 0L

    while (usageEvents.hasNextEvent()) {
      val event = UsageEvents.Event()
      usageEvents.getNextEvent(event)

      if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
        currentApp = event.packageName
        eventTime = event.timeStamp
      }
    }
    return currentApp
  }

  public fun isUsageStatsPermissionGranted(): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
      AppOpsManager.OPSTR_GET_USAGE_STATS,
      android.os.Process.myUid(),
      context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
  }


  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onDetachedFromActivity() {
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    this.activity = binding.activity
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    this.activity = binding.activity
  }

  override fun onDetachedFromActivityForConfigChanges() {
  }


}

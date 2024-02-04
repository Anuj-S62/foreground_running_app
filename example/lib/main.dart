import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:foreground_running_app/foreground_running_app.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _packageName = 'Unknown';

  @override
  void initState() {
    super.initState();
  }

  void getPackageName() async {
    String packageName;
    try {
      packageName = await ForegroundRunningApp().getRunningApp();
    } on PlatformException {
      packageName = 'Failed to get package name.';
    }
    if (!mounted) return;

    setState(() {
      _packageName = packageName;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Text('Running App: $_packageName\n'),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: () async {
            getPackageName();
          },
          child: const Icon(Icons.add),
        ),
      ),
    //  add a floating action button

    );
  }
}

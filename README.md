# PoilabsAnalysis React Native Integration

## iOS

### INSTALLATION

To integrate PoilabsAnalysis into your Xcode project using CocoaPods, specify it in your Podfile. 

```curl
pod 'PoilabsAnalysis'
```

### PRE-REQUIREMENTS

To Integrate this framework you should add some features to your project info.plist file.

Privacy - Location Usage Description

Privacy - Location When In Use Usage Description

Privacy - Location Always Usage Description

Privacy - Location Always and When In Use Usage Description

### USAGE

You should create a header file called **PoilabsAnalysisModule.h** and a Objective-C file  called **PoilabsAnalysisModule.m** with content below. 
 
#### PoilabsAnalysisModule.h

``` objective-c
#ifndef PoilabsAnalysisModule_h
#define PoilabsAnalysisModule_h

#import "PoilabsAnalysis/PoilabsAnalysis.h"
#import <React/RCTBridgeModule.h>
@interface PoilabsAnalysisModule : NSObject <RCTBridgeModule, PLAnalysisManagerDelegate>
@end

#endif /* PoilabsAnalysisModule_h */
```

#### PoilabsAnalysisModule.m

``` objective-c
#import <Foundation/Foundation.h>
#import "PoilabsAnalysisModule.h"

@implementation PoilabsAnalysisModule

RCT_EXPORT_MODULE(PoilabsAnalysisModule);

RCT_EXPORT_METHOD(stopPoilabsAnalysis) {
  [[PLAnalysisSettings sharedInstance] closeAllActions];
}

RCT_EXPORT_METHOD(startPoilabsAnalysis:(NSString *)applicationId applicationSecret:(NSString *) secret uniqueIdentifier:(NSString *) uniqueId) {
  [[PLAnalysisSettings sharedInstance] setApplicationId:applicationId];
  [[PLAnalysisSettings sharedInstance] setApplicationSecret:secret];
  [[PLAnalysisSettings sharedInstance] setAnalysisUniqueIdentifier:uniqueId];
  [[PLConfigManager sharedInstance] getReadyForTrackingWithCompletionHandler:^(PLError *error) {
      if (error) {
          NSLog(@"Error Desc %@",error.errorDescription);
      }
      else
      {
          [[PLSuspendedAnalysisManager sharedInstance] stopBeaconMonitoring];
          [[PLStandardAnalysisManager sharedInstance] startBeaconMonitoring];
          [[PLStandardAnalysisManager sharedInstance] setDelegate:self];
      }
  }];
}

-(void)analysisManagerDidFailWithPoiError:(PLError *)error
{
    NSLog(@"Error Desc %@",error);
}

-(void)analysisManagerResponseForBeaconMonitoring:(NSDictionary *)response
{
    NSLog(@"Response %@",response);
}

@end
```

#### AppDelegate.m

```c
#import "PoilabsAnalysis/PoilabsAnalysis.h"
```

To start suspended mode that allows track location when application is killed, you should call method below in **didFinishLaunchingWithOptions:**

```c
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
.....
  if ([launchOptions objectForKey:UIApplicationLaunchOptionsLocationKey] && [UIApplication sharedApplication].applicationState == UIApplicationStateBackground) {
      [[PLSuspendedAnalysisManager sharedInstance] startBeaconMonitoring];
  }
  .......
  }
```

## Android

### INSTALLATION

You can download our SDK via Gradle with following below steps


1. Add jitpack dependency to your project level build.gradle file with their tokens.  
   **JITPACK_TOKEN** is a token that PoiLabs will provide for you it will allow you to download our sdk.

```groovy 
allprojects {    
    repositories {    
        maven {            
            url "https://jitpack.io"   
            credentials { username = 'JITPACK_TOKEN' }    
        }    
    } 
 }  
```

2. Add PoiLabs Analysis SDK dependency to your app level build.gradle file

```groovy    
 dependencies {    
    implementation 'com.github.poiteam:Android-Analysis-SDK:v3.11.0'    
}
```

3. In order to our SDK can work properly we need location permission and bluetooth usage for scanning for beacons by following below steps you can implement these runtime permissions in your app

**Android Manifest file**

``` 
<uses-permission android:name="android.permission.BLUETOOTH" /> 
     <uses-permission android:name="android.permission.INTERNET" /> 
     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> 
     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />  
     <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
     <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
     <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
     <uses-permission android:name="android.permission.FOREGROUND_SERVICE" android:foregroundServiceType="location"/>
```
 
 ### USAGE
 
#### Add these methods to your MainActivity.kt file
 
 **MainActivity**
 
```kotlin
     companion object {
        private const val REQUEST_FOREGROUND_LOCATION_REQUEST_CODE = 56
        private const val REQUEST_BACKGROUND_LOCATION_REQUEST_CODE = 57
        private const val REQUEST_COARSE_LOCATION = 58
        private const val REQUEST_BLUETOOTH_PERMISSION = 59
        private const val TAG = "MainActivity"
    }

    fun askRuntimePermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val hasFineLocation: Int = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            val hasBackgroundLocation: Int = ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_BACKGROUND_LOCATION)

            if (hasFineLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_FOREGROUND_LOCATION_REQUEST_CODE)
            }
            if (hasBackgroundLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), REQUEST_BACKGROUND_LOCATION_REQUEST_CODE)
            }

            if (hasFineLocation == PackageManager.PERMISSION_GRANTED && hasBackgroundLocation == PackageManager.PERMISSION_GRANTED) {
                startPoiSdk()
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                checkBluetoothPermission()
            }

        } else {
            val hasLocalPermission: Int = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (hasLocalPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_COARSE_LOCATION)
            } else {
                startPoiSdk()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun checkBluetoothPermission() {
        val hasBluetoothPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        if (!hasBluetoothPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN),
                REQUEST_BLUETOOTH_PERMISSION
            )
        } else {
            startPoiSdk()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isEmpty()) {
            return
        }
        if (requestCode == REQUEST_COARSE_LOCATION) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) { // Permission Granted
                startPoiSdk()
            }
        } else if (requestCode == REQUEST_BACKGROUND_LOCATION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                askRuntimePermissionsIfNeeded()
                return
            }
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) { // Permission Granted
                startPoiSdk()
            }
        } else if( requestCode == REQUEST_FOREGROUND_LOCATION_REQUEST_CODE) {
            askRuntimePermissionsIfNeeded()
        } else if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[0]) { // Permission Granted
                startPoiSdk()
            }
        }
    }


    fun startPoiSdk() {
        PoiAnalysis.getInstance().enable()
        PoiAnalysis.getInstance().startScan(applicationContext)
    }
```

#### Imported packages into MainActivity:

 ```kotlin
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import getpoi.com.poibeaconsdk.PoiAnalysis
import android.Manifest
```
 
 
#### Create a Kotlin filed called **PoilabsAnalysisModule** with content below
 
 ```kotlin
 import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import getpoi.com.poibeaconsdk.PoiAnalysis
import getpoi.com.poibeaconsdk.models.PoiAnalysisConfig
import getpoi.com.poibeaconsdk.models.PoiResponseCallback

class PoilabsAnalysisModule  internal constructor(context: ReactApplicationContext?) :
ReactContextBaseJavaModule(context) {
    override fun getName(): String {
        return "PoilabsAnalysisModule"
    }
    @ReactMethod
    fun startPoilabsAnalysis(applicationId: String, applicationSecret: String, uniqueIdentifier: String) {
        currentActivity?.let { activity ->
            val poiAnalysisConfig = PoiAnalysisConfig(applicationId, applicationSecret, uniqueIdentifier)
            PoiAnalysis.getInstance(activity.applicationContext, poiAnalysisConfig)

            poiAnalysisConfig.setForegroundServiceIntent(Intent(activity.applicationContext, MainActivity::class.java))
            poiAnalysisConfig.setServiceNotificationTitle("Test1")
            poiAnalysisConfig.setForegroundServiceNotificationChannelProperties("name1" , "desc1")
            poiAnalysisConfig.enableForegroundService()

            PoiAnalysis.getInstance().setPoiResponseListener(object : PoiResponseCallback {
                override fun onResponse(nodeIds: List<String>?) {
                    
                }

                override fun onFail(cause: Exception?) {
                    
                }
            })
            (activity as MainActivity).askRuntimePermissionsIfNeeded()
        }
    }

    @ReactMethod
    fun stopPoilabsAnalysis() {
        PoiAnalysis.getInstance().stopScan()
    }
}
```

#### Create a Kotlin file called PoilabsPackage with content below

```Kotlin
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class PoilabsPackage : ReactPackage {
    override fun createNativeModules(reactApplicationContext: ReactApplicationContext): List<NativeModule> {
        val modules: MutableList<NativeModule> = ArrayList()
        modules.add(PoilabsAnalysisModule(reactApplicationContext))
        return modules
    }
    override fun createViewManagers(reactApplicationContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList();
    }
}
```

#### Add PoilabsPackage to getPackages method of MainApplication
```Java
    override fun getPackages(): List<ReactPackage> =
        PackageList(this).packages.apply {
            add(PoilabsPackage())
        }
```

## React Native

You should import **NativeModules**

```js
import {
  NativeModules,
} from 'react-native';
```
You can start PoilabsAnalysis with calling bridge method

```js
NativeModules.PoilabsAnalysisModule.startPoilabsAnalysis(APPLICATION_ID, APPLICATION_SECRET, UNIQUE_ID);
```

You can stop PoilabsAnalysis with calling bridge method

```js
NativeModules.PoilabsAnalysisModule.stopPoilabsAnalysis;
```





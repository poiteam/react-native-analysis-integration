
# PoilabsAnalysis React Native Integration

  

## iOS

  

For implementation, **follow all steps** of native iOS documentation.

  

## Android

  

For implementation, **follow all steps** of native Android documentation.

   For using PoiLabs Analysis solution your app must get these permissions  
*	ACCESS_FINE_LOCATION  
*	ACCESS_COARSE_LOCATION  
*	ACCESS_BACKGROUND_LOCATION  
*	FOREGROUND_SERVICE  
**For Androids with version greater than 12 you should also get**  
*    BLUETOOTH_SCAN permission  
**For using setOpenSystemBluetooth function with android 12 and above you should get**  
*    BLUETOOTH_CONNECT permission
  **Without these permission this SDK will not work properly**

In **MainApplication.java** file add below line to onCreate method.

  

```Java

PoiAnalysisConfig  config = new  PoiAnalysisConfig("APPLICATION_SECRET_KEY", "this is a test unique id", "APPLICATION_ID");

config.setOpenSystemBluetooth(true);

config.enableForegroundService();

config.setServiceNotificationTitle("Searching for campaigns...");

config.setForegroundServiceNotificationChannelProperties(

"My Notification Name",

"My Notification Channel Description"

);

PoiAnalysis.getInstance(this, config);

  

PoiAnalysis.getInstance().enable();

  

PoiAnalysis.getInstance().startScan(this);

```
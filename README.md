# PoilabsAnalysis React Native Integration

## iOS

For implementation, **follow all steps** of native iOS documentation.

## Android

For implementation, **follow all steps** of native Android documentation.

Create **PoilabsAnalysisModule.kt** file with content below.

```Java
class PoilabsAnalysisModule internal constructor(var context: ReactApplicationContext) : ReactContextBaseJavaModule(context), PoiResponseCallback {
    
    private val REQUEST_COARSE_LOCATION = 57
    override fun getName(): String {
        return "PoilabsAnalysisModule"
    }

    @ReactMethod
    fun startPoilabsAnalysis() {
        Log.d("PoilabsAnalysisModule", "startPoilabsAnalysis method is called")
        askRuntimePermissionsIfNeeded()
        startPoiSdk()
    }

    companion object {
        private const val REQUEST_FOREGROUND_LOCATION_REQUEST_CODE = 56
        private const val REQUEST_BACKGROUND_LOCATION_REQUEST_CODE = 57
        private const val REQUEST_COARSE_LOCATION = 58
    }

    private fun askRuntimePermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val hasFineLocation: Int = ActivityCompat.checkSelfPermission(currentActivity!!, Manifest.permission.ACCESS_FINE_LOCATION)
            val hasBackgroundLocation: Int = ActivityCompat.checkSelfPermission(
                    currentActivity!!,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            if (hasFineLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( currentActivity!!,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_FOREGROUND_LOCATION_REQUEST_CODE)
            }
            if (hasBackgroundLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(currentActivity!!,
                        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                        REQUEST_BACKGROUND_LOCATION_REQUEST_CODE)
            }
        } else {
            val hasLocalPermission: Int = ActivityCompat.checkSelfPermission(currentActivity!!, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (hasLocalPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(currentActivity!!,
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                        REQUEST_COARSE_LOCATION)
            }
        }
    }

    private fun startPoiSdk() {
        PoiAnalysis.getInstance().enable()
        PoiAnalysis.getInstance().startScan(currentActivity!!)
        PoiAnalysis.getInstance().setPoiResponseListener(this)
    }

    override fun onResponse(p0: String?) {
        Log.i("TAG", "onResponse: $p0")
    }

    override fun onFail(p0: Exception?) {
        Log.e("TAG", "onFail: ${p0?.localizedMessage}")
        p0?.printStackTrace()
    }
}
```

Create file **PoilabsAnalysisPackage.java** with content below.

```Java
public class PoilabsAnalysisPackage implements ReactPackage {
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new PoilabsAnalysisModule(reactContext));

        return modules;
    }
}
```

In **MainApplication.java** file add below line to onCreate method.

```Java
      PoiAnalysisConfig config = new PoiAnalysisConfig("APPLICATION_SECRET_KEY", "this is a test unique id", "APPLICATION_ID");
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

To your react-native code add lines below, and call method **PoilabsAnalysisModule.startPoilabsAnalysis();**

```Js
import { NativeModules, Button } from 'react-native';
const { PoilabsAnalysisModule} = NativeModules;
```
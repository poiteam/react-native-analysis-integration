package com.reactnativeanalysisintegration

import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.facebook.react.ReactActivity
import com.facebook.react.ReactActivityDelegate
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.fabricEnabled
import com.facebook.react.defaults.DefaultReactActivityDelegate
import getpoi.com.poibeaconsdk.PoiAnalysis
import android.Manifest
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ReactActivity() {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  override fun getMainComponentName(): String = "reactNativeAnalysisIntegration"

  /**
   * Returns the instance of the [ReactActivityDelegate]. We use [DefaultReactActivityDelegate]
   * which allows you to enable New Architecture with a single boolean flags [fabricEnabled]
   */
  override fun createReactActivityDelegate(): ReactActivityDelegate =
      DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)

    private var requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        handleNextPermission()
    }

    fun setPermissionLaunchers() {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun handleNextPermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !isPermissionGranted(Manifest.permission.ACCESS_BACKGROUND_LOCATION) -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isPermissionGranted(Manifest.permission.BLUETOOTH_SCAN) -> {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_SCAN)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT) -> {
                requestPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
            }
            else -> {
                startPoiSdk()
            }
        }
    }
    fun isPermissionGranted(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }
    fun startPoiSdk() {
        PoiAnalysis.getInstance().enable()
        PoiAnalysis.getInstance().startScan(applicationContext)
    }
}

package com.reactnativeanalysisintegration

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import getpoi.com.poibeaconsdk.PoiAnalysis
import getpoi.com.poibeaconsdk.models.PoiAnalysisConfig
import getpoi.com.poibeaconsdk.models.PoiResponseCallback

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
package com.reactnativeanalysisintegration

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
                    Log.i("POI_LOG",  nodeIds.toString())
                }

                override fun onFail(cause: Exception?) {
                    Log.i("POI_LOG", cause.toString())
                }
            })
            (activity as MainActivity).setPermissionLaunchers()
        }
    }

    @ReactMethod
    fun stopPoilabsAnalysis() {
        PoiAnalysis.getInstance().stopScan()
    }
}

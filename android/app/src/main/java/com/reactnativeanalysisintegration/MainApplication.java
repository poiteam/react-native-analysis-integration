package com.reactnativeanalysisintegration;

import android.app.Application;
import android.content.Context;

import com.facebook.react.BuildConfig;
import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.soloader.SoLoader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import getpoi.com.poibeaconsdk.PoiAnalysis;
import getpoi.com.poibeaconsdk.models.PoiAnalysisConfig;

public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost =
      new ReactNativeHost(this) {
        @Override
        public boolean getUseDeveloperSupport() {
          return BuildConfig.DEBUG;
        }

        @Override
        protected List<ReactPackage> getPackages() {
          @SuppressWarnings("UnnecessaryLocalVariable")
          List<ReactPackage> packages = new PackageList(this).getPackages();
          // Packages that cannot be autolinked yet can be added manually here, for example:
          // packages.add(new MyReactNativePackage());
          return packages;
        }

        @Override
        protected String getJSMainModuleName() {
          return "index";
        }
      };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    initializeFlipper(this, getReactNativeHost().getReactInstanceManager());

      /**
       * For using PoiLabs Analysis solution your app must get these permissions
       *    ACCESS_FINE_LOCATION
       *     ACCESS_COARSE_LOCATION
       *     ACCESS_BACKGROUND_LOCATION
       *     FOREGROUND_SERVICE
       *    for androids with version greater than 12 you should also get
       *    BLUETOOTH_SCAN permission
       *    for using setOpenSystemBluetooth function with android 12 and above you should get
       *    BLUETOOTH_CONNECT permission
       *
       *
       */
      PoiAnalysisConfig config = new PoiAnalysisConfig("APPLICATION_ID","APPLICATION_SECRET_KEY", "this is a test unique id" );
      config.setOpenSystemBluetooth(false);
      config.enableForegroundService();
      config.setServiceNotificationTitle("Searching for campaigns...");
      config.setForegroundServiceNotificationChannelProperties(
              "My Notification Name",
              "My Notification Channel Description"
      );
      PoiAnalysis.getInstance(this, config);

      PoiAnalysis.getInstance().enable();

      PoiAnalysis.getInstance().startScan(this);
  }

  /**
   * Loads Flipper in React Native templates. Call this in the onCreate method with something like
   * initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
   *
   * @param context
   * @param reactInstanceManager
   */
  private static void initializeFlipper(
      Context context, ReactInstanceManager reactInstanceManager) {
    if (BuildConfig.DEBUG) {
      try {
        /*
         We use reflection here to pick up the class that initializes Flipper,
        since Flipper library is not available in release mode
        */
        Class<?> aClass = Class.forName("com.reactnativeanalysisintegration.ReactNativeFlipper");
        aClass
            .getMethod("initializeFlipper", Context.class, ReactInstanceManager.class)
            .invoke(null, context, reactInstanceManager);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      }
    }
  }
}

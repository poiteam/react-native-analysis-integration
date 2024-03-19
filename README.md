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




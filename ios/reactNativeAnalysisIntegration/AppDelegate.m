#import "AppDelegate.h"

#import <React/RCTBridge.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>

#ifdef FB_SONARKIT_ENABLED
#import <FlipperKit/FlipperClient.h>
#import <FlipperKitLayoutPlugin/FlipperKitLayoutPlugin.h>
#import <FlipperKitUserDefaultsPlugin/FKUserDefaultsPlugin.h>
#import <FlipperKitNetworkPlugin/FlipperKitNetworkPlugin.h>
#import <SKIOSNetworkPlugin/SKIOSNetworkAdapter.h>
#import <FlipperKitReactPlugin/FlipperKitReactPlugin.h>



static void InitializeFlipper(UIApplication *application) {
  FlipperClient *client = [FlipperClient sharedClient];
  SKDescriptorMapper *layoutDescriptorMapper = [[SKDescriptorMapper alloc] initWithDefaults];
  [client addPlugin:[[FlipperKitLayoutPlugin alloc] initWithRootNode:application withDescriptorMapper:layoutDescriptorMapper]];
  [client addPlugin:[[FKUserDefaultsPlugin alloc] initWithSuiteName:nil]];
  [client addPlugin:[FlipperKitReactPlugin new]];
  [client addPlugin:[[FlipperKitNetworkPlugin alloc] initWithNetworkAdapter:[SKIOSNetworkAdapter new]]];
  [client start];
}
#endif

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
  
  if ([launchOptions objectForKey:UIApplicationLaunchOptionsLocationKey]
      && [UIApplication sharedApplication].applicationState == UIApplicationStateBackground)
  {
      NSLog(@"react-native-analysis-integration suspended monitoring started");
      [[PLSuspendedAnalysisManager sharedInstance] startBeaconMonitoring];
  }
  
  
#ifdef FB_SONARKIT_ENABLED
  InitializeFlipper(application);
#endif

  RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];
  RCTRootView *rootView = [[RCTRootView alloc] initWithBridge:bridge
                                                   moduleName:@"reactNativeAnalysisIntegration"
                                            initialProperties:nil];

  if (@available(iOS 13.0, *)) {
      rootView.backgroundColor = [UIColor systemBackgroundColor];
  } else {
      rootView.backgroundColor = [UIColor whiteColor];
  }

  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
  UIViewController *rootViewController = [UIViewController new];
  rootViewController.view = rootView;
  self.window.rootViewController = rootViewController;
  [self.window makeKeyAndVisible];
  return YES;
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    NSLog(@"applicationDidBecomeActive");
    [self startStandardBeaconMonitoring];
}

-(void)startStandardBeaconMonitoring
{
    [[PLAnalysisSettings sharedInstance] setApplicationId:@"6d418502-07be-414f-a0c6-8e9242e60f94"];
    [[PLAnalysisSettings sharedInstance] setApplicationSecret:@"c85e4d57-4278-4622-995e-789be997d5b6"];
    [[PLAnalysisSettings sharedInstance] setAnalysisUniqueIdentifier:@"Akasya_Emre"];
    [[PLConfigManager sharedInstance] getReadyForTrackingWithCompletionHandler:^(PLError *error) {
        
        if (error) {
            NSLog(@"Error Desc %@",error);
        }
        else
        {
            NSLog(@"PLAnalysisSdk Error Nil");
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
    NSLog(@"react-native-analysis-integration %@",response);
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

@end

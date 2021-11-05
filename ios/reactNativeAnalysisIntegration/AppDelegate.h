#import <React/RCTBridgeDelegate.h>
#import <UIKit/UIKit.h>
#import <PoilabsAnalysis/PoilabsAnalysis.h>
@interface AppDelegate : UIResponder <UIApplicationDelegate, RCTBridgeDelegate, PLAnalysisManagerDelegate>

@property (nonatomic, strong) UIWindow *window;

@end

//
//  PoilabsAnalysisModule.h
//  reactNativeAnalysisIntegration
//
//  Created by Emre Kuru on 19.03.2024.
//

#ifndef PoilabsAnalysisModule_h
#define PoilabsAnalysisModule_h

#import "PoilabsAnalysis/PoilabsAnalysis.h"
#import <React/RCTBridgeModule.h>
@interface PoilabsAnalysisModule : NSObject <RCTBridgeModule, PLAnalysisManagerDelegate>
@end

#endif /* PoilabsAnalysisModule_h */

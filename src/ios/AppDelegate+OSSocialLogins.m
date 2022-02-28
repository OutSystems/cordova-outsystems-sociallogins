#import "AppDelegate+OSSocialLogins.h"

#import <FBSDKLoginKit/FBSDKLoginKit.h>

@implementation AppDelegate (OSSocialLogins)

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    [FBSDKApplicationDelegate.sharedInstance application:application didFinishLaunchingWithOptions:launchOptions];
    
    return YES;
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    
    return [FBSDKApplicationDelegate.sharedInstance
            application:app
            openURL:url
            sourceApplication:options[UIApplicationOpenURLOptionsSourceApplicationKey]
            annotation:options[UIApplicationOpenURLOptionsAnnotationKey]];
}

@end
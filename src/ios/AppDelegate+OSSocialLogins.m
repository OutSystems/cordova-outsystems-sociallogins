#import "AppDelegate+OSSocialLogins.h"

#import <OSSocialLoginsLib/OSSocialLoginsLib-Swift.h>
#import <objc/runtime.h>

@implementation AppDelegate (OSSocialLogins)

+ (void)load {
    Method original = class_getInstanceMethod(self, @selector(application:didFinishLaunchingWithOptions:));
    Method swizzled = class_getInstanceMethod(self, @selector(application:swizzledDidFinishLaunchingWithOptions:));
    method_exchangeImplementations(original, swizzled);
}

- (BOOL)application:(UIApplication *)application swizzledDidFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [self application:application swizzledDidFinishLaunchingWithOptions:launchOptions];
    
    (void)[SocialLoginsApplicationDelegate.shared application:application didFinishLaunchingWithOptions:launchOptions];
    
    return YES;
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    return [SocialLoginsApplicationDelegate.shared
            application:app
            openURL:url
            options:options];
}

@end
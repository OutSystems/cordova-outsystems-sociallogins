#import "AppDelegate+OSSocialLogins.h"

#import <OSSocialLoginsLib/OSSocialLoginsLib-Swift.h>
#import <objc/runtime.h>

@implementation AppDelegate (OSSocialLogins)

+ (void)load {
    Method original = class_getInstanceMethod(self, @selector(application:didFinishLaunchingWithOptions:));
    Method swizzled = class_getInstanceMethod(self, @selector(application:socialLoginsPluginDidFinishLaunchingWithOptions:));
    method_exchangeImplementations(original, swizzled);
}

- (BOOL)application:(UIApplication *)application socialLoginsPluginDidFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [self application:application socialLoginsPluginDidFinishLaunchingWithOptions:launchOptions];
    
    (void)[SocialLoginsApplicationDelegate.shared application:application didFinishLaunchingWithOptions:launchOptions];
    
    return YES;
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    NSString *currentUrlScheme = [url.absoluteString componentsSeparatedByString:@"://"].firstObject;
    
    NSArray *urlTypes = [NSBundle.mainBundle objectForInfoDictionaryKey:@"CFBundleURLTypes"];
    NSPredicate *predicate = [NSPredicate predicateWithBlock:^BOOL(id evaluatedObject, NSDictionary<NSString *,id> *bindings) {
        NSArray *urlSchemeArray = evaluatedObject[@"CFBundleURLSchemes"];
        return [urlSchemeArray containsObject:currentUrlScheme];
    }];
    
    NSString *urlName = [urlTypes filteredArrayUsingPredicate:predicate].firstObject[@"CFBundleURLName"];
    if ([@[@"Google", @"Facebook", @"DeepLinkScheme"] containsObject:urlName]) {
        return [SocialLoginsApplicationDelegate.shared
                application:app
                openURL:url
                options:options];
    }
    
    return [super application:app openURL:url options:options];
}

@end

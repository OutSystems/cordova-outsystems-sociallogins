import Foundation

class SocialLoginsPlugin: NSObject {

    var rootViewController:UIViewController?
    var appleProvider: SocialLoginsAppleProvider?
    var googleProvider: SocialLoginsGoogleProvider?
    
    init (appleProvider: SocialLoginsAppleProvider, googleProvider:SocialLoginsGoogleProvider) {
        super.init()
        self.googleProvider = googleProvider
        self.appleProvider?.rootViewController = self.rootViewController
        self.appleProvider = appleProvider
    }
    
    func doLogin(callbackID: String) {
        self.appleProvider?.doLogin(callbackID: callbackID)
    }
    
}

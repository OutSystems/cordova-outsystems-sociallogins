import Foundation

class SocialLoginsPlugin: NSObject {

    var delegate: SocialLoginsProtocol?
    var rootViewController:UIViewController?
    var appleProvider: SocialLoginsAppleProvider?
    var googleProvider: SocialLoginsGoogleProvider?
    
    init (appleProvider: SocialLoginsAppleProvider, googleProvider:SocialLoginsGoogleProvider) {
        super.init()
        self.googleProvider = googleProvider
        self.appleProvider = appleProvider
        self.appleProvider?.rootViewController = self.rootViewController
    }
    
    func doLogin(callbackID: String) {
        self.appleProvider?.doLogin(callbackID: callbackID)
    }
    
}

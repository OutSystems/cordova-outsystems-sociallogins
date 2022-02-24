import Foundation

class SocialLoginsController: NSObject {

    var rootViewController:UIViewController?
    var appleController: SocialLoginsAppleController?
    var googleController: SocialLoginsGoogleController?
    
    init (appleController: SocialLoginsAppleController, googleController:SocialLoginsGoogleController) {
        super.init()
        self.googleController = googleController
        self.appleController?.rootViewController = self.rootViewController
        self.appleController = appleController
    }
    
    func doLogin(callbackID: String) {
        self.appleController?.doLogin(callbackID: callbackID)
    }
    
}

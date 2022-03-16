import Foundation
import OSSocialLoginsLib

@objc(OSSocialLogins)
class OSSocialLogins: CordovaImplementation {
    
    var plugin: SocialLoginsController?
    var callbackId:String=""
    
    override func pluginInitialize() {
        let appleController = SocialLoginsAppleController(delegate:self, rootViewController: self.viewController)
        let googleController = SocialLoginsGoogleController(delegate:self, rootViewController: self.viewController)
        let facebookController = SocialLoginsFacebookController(delegate: self, rootViewController: self.viewController)
        plugin = SocialLoginsController(appleController: appleController, 
                                        googleController: googleController,
                                        facebookController: facebookController)
    }
    
    @objc(loginApple:)
    func loginApple(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        plugin?.loginApple(callbackID: self.callbackId)
    }

    @objc(loginGoogle:)
    func loginGoogle(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        plugin?.loginGoogle(callbackID: self.callbackId)
    }

    @objc(loginFacebook:)
    func loginFacebook(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        plugin?.loginFacebook(callbackID: self.callbackId)
    }
       
}

extension OSSocialLogins: SocialLoginsProtocol {
    func callBackUserInfo(result: UserInfo?, error: SocialLoginsErrors?, callBackID: String) {
        if let error = error {
            self.sendResult(result: nil, error:error as NSError, callBackID: self.callbackId)
        } else {
            let finalResult = result?.encode()
            self.sendResult(result: finalResult, error:nil , callBackID: self.callbackId)
        }
    }
}

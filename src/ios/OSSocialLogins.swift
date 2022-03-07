import Foundation
import OSSocialLoginsLib

@objc(OSSocialLogins)
class OSSocialLogins: CordovaImplementation {
    
    var plugin: SocialLoginsController?
    var callbackId:String=""
    
    override func pluginInitialize() {
        let appleController = SocialLoginsAppleController(delegate:self)
        let googleController = SocialLoginsGoogleController(delegate:self)
        plugin = SocialLoginsController(appleController: appleController, 
                                        googleController: googleController,
                                        rootViewController: self.viewController)
    }
    
    @objc(loginApple:)
    func loginApple(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        plugin?.loginApple(callbackID: self.callbackId)
    }

    @objc(loginGoogle:)
    func loginApple(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        plugin?.loginGoogle(callbackID: self.callbackId)
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

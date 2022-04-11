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
        let linkedInController = SocialLoginsLinkedInController(delegate: self, rootViewController: self.viewController)
        plugin = SocialLoginsController(appleController: appleController, 
                                        googleController: googleController,
                                        facebookController: facebookController,
                                        linkedInController: linkedInController)
    }
    
    @objc(loginApple:)
    func loginApple(command: CDVInvokedUrlCommand) {
        self.callbackId = command.callbackId
        self.plugin?.loginApple()
    }

    @objc(loginGoogle:)
    func loginGoogle(command: CDVInvokedUrlCommand) {
        self.callbackId = command.callbackId
        self.plugin?.loginGoogle()
    }

    @objc(loginFacebook:)
    func loginFacebook(command: CDVInvokedUrlCommand) {
        self.callbackId = command.callbackId
        self.plugin?.loginFacebook()
    }

    @objc(loginLinkedIn:)
    func loginLinkedIn(command: CDVInvokedUrlCommand) {
        self.callbackId = command.callbackId

        guard 
            let state = command.arguments[0] as? String,
            let clientID = command.arguments[1] as? String,
            let redirectURI = command.arguments[2] as? String
        else {
            self.sendResult(result: "", error:SocialLoginsErrors.linkedInConfigurationNotValidError as NSError, callBackID: self.callbackId)
            return
        }
        
        self.plugin?.loginLinkedIn(state: state, clientID: clientID, redirectURI: redirectURI)
    }
       
}

extension OSSocialLogins: SocialLoginsProtocol {
    func callBackUserInfo(result: UserInfo?, error: SocialLoginsErrors?) {
        if let error = error {
            self.sendResult(result: nil, error:error as NSError, callBackID: self.callbackId)
        } else {
            let finalResult = result?.encode()
            self.sendResult(result: finalResult, error:nil , callBackID: self.callbackId)
        }
    }
}

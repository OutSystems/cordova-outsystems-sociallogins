import Foundation
import OSCore

@objc(OSSocialLogins)
class OSSocialLogins: CordovaImplementation {
    
    var plugin: SocialLoginsPlugin?
    var callbackId:String=""
    
    override func pluginInitialize() {
        let googleProvider = SocialLoginsGoogleProvider()
        let appleProvider = SocialLoginsAppleProvider()
        plugin = SocialLoginsPlugin(appleProvider: appleProvider, googleProvider: googleProvider)
        plugin?.rootViewController = self.viewController
        plugin?.delegate = self
    }
    
    @objc(login:)
    func doLogin(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        plugin?.doLogin(callbackID: self.callbackId)
    }
       
}

extension OSSocialLogins: SocialLoginsProtocol {
    func callBackUserInfoResponse(result: UserInfoResponse?, error: NSError?, callBackID: String) {
        let finalResult = result?.encode(object:result)
        self.sendResult(result: finalResult, error:nil , callBackID: self.callbackId)
    }
    func callBackCredentialState(result: String?, error: NSError?, callBackID: String) {
        self.sendResult(result: result, error:nil , callBackID: self.callbackId)
    }
}

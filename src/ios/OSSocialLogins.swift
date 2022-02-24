import Foundation

@objc(OSSocialLogins)
class OSSocialLogins: CordovaImplementation {
    
    var plugin: SocialLoginsController?
    var callbackId:String=""
    
    override func pluginInitialize() {
        let googleController = SocialLoginsGoogleController()
        let appleController = SocialLoginsAppleController(delegate:self)
        plugin = SocialLoginsController(appleController: appleController, googleController: googleController)
        plugin?.rootViewController = self.viewController
    }
    
    @objc(login:)
    func doLogin(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        plugin?.doLogin(callbackID: self.callbackId)
    }
       
}

extension OSSocialLogins: SocialLoginsProtocol {
    func callBackUserInfoResponse(result: UserInfo?, error: NSError?, callBackID: String) {
        let finalResult = result?.encode(object:result)
        self.sendResult(result: finalResult, error:nil , callBackID: self.callbackId)
    }
    func callBackCredentialState(result: String?, error: NSError?, callBackID: String) {
        self.sendResult(result: result, error:nil , callBackID: self.callbackId)
    }
}

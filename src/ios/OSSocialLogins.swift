import Foundation
import OSSocialLoginsLib

@objc(OSSocialLogins)
class OSSocialLogins: CordovaImplementation {
    
    var plugin: SocialLoginsController?
    var callbackId:String=""
    
    override func pluginInitialize() {
        let appleController = SocialLoginsAppleController(delegate:self)
        plugin = SocialLoginsController(appleController: appleController, rootViewController: self.viewController)
    }
    
    @objc(login:)
    func doLogin(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        plugin?.loginApple(callbackID: self.callbackId)
    }
       
}

extension OSSocialLogins: SocialLoginsProtocol {
    func callBackUserInfo(result: UserInfo?, error: NSError?, callBackID: String) {
        let finalResult = result?.encode(object:result)
        self.sendResult(result: finalResult, error:nil , callBackID: self.callbackId)
    }
}

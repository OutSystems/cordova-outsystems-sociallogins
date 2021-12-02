import Foundation

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
    
    @objc(doLogin:)
    func doLogin(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        let provider = command.arguments[0] as? String ?? ""
        
        do {
            try plugin?.doLogin(callbackID: self.callbackId, provider: provider)
        } catch {
            self.sendResult(result: "", error:nil , callBackID: self.callbackId)
        }
    }
    
    @objc(logout:)
    func logout(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        let provider = command.arguments[0] as? String ?? ""
        
        do {
            try plugin?.doLogout(callbackID: self.callbackId, provider: provider)
        } catch {
            self.sendResult(result: "", error:nil , callBackID: self.callbackId)
        }
    }
    
    @objc(checkLoginStatus:)
    func checkLoginStatus(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        let provider = command.arguments[0] as? String ?? ""
        
        do {
            try plugin?.getCredentialState(userID: "", provider: provider)
        } catch {
            self.sendResult(result: "", error:nil , callBackID: self.callbackId)
        }
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

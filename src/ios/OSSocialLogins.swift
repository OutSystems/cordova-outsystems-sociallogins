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
    
    @objc(doLoginGoogle:)
    func doLoginGoogle(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        
        do {
            try plugin?.doLoginGoogle(callbackID: self.callbackId)
        } catch {
            self.sendResult(result: "", error:nil , callBackID: self.callbackId)
        }
    }
    
    @objc(doLogin:)
    func doLogin(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        
        do {
            try plugin?.doLogin(callbackID: self.callbackId, provider: "GOOGLE")
        } catch {
            self.sendResult(result: "", error:nil , callBackID: self.callbackId)
        }
    }
    
    @objc(isLoggedIn:)
    func isLoggedIn(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        
        do {
            try plugin?.getCredentialState(userID: "", provider: "GOOGLE")
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

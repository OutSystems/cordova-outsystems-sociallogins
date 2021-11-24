import Foundation

@objc(OSSocialLogins)
class OSSocialLogins: CordovaImplementation {
    var plugin: SocialLoginsPlugin?
    var callbackId:String=""
    
    override func pluginInitialize() {
        plugin = SocialLoginsPlugin()
    }
    
    @objc(doLogin:)
    func doLogin(command: CDVInvokedUrlCommand) {
        callbackId = command.callbackId
        
        plugin?.doLogin()
        self.sendResult(result: "", error:nil , callBackID: self.callbackId)
    }
       
}

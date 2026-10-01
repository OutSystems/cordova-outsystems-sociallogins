#if canImport(Cordova)
import Cordova
#endif

import Foundation
import OSSocialLoginsLib
import UIKit

@objc(OSSocialLogins)
class OSSocialLogins: CDVPlugin {
    
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

    // Cordova iOS 8 apps adopt UIScene, which routes URL opens through `CDVSceneDelegate` instead of
    // `AppDelegate.application(_:open:options:)` — the notification below is the one path common to both
    // Cordova iOS 7/8, so handling it here (instead of swizzling AppDelegate) works on MABS 12 and MABS 13.
    override func handleOpenURL(_ notification: Notification) {
        guard let url = notification.object as? URL else {
            return
        }

        let urlScheme = url.absoluteString.components(separatedBy: "://").first ?? ""
        guard self.shouldDelegateToLibrary(urlScheme) else {
            return
        }

        let options = self.openURLOptions(from: notification.userInfo)
        _ = SocialLoginsApplicationDelegate.shared.application(UIApplication.shared, open: url, options: options)
    }

    private func shouldDelegateToLibrary(_ urlScheme: String) -> Bool {
        guard let urlTypes = Bundle.main.object(forInfoDictionaryKey: "CFBundleURLTypes") as? [[String: Any]] else {
            return false
        }

        let matchingURLType = urlTypes.first { ($0["CFBundleURLSchemes"] as? [String])?.contains(urlScheme) ?? false }
        let urlName = matchingURLType?["CFBundleURLName"] as? String ?? ""

        return ["Google", "Facebook", "DeepLinkScheme"].contains(urlName)
    }

    // `CDVAppDelegate` posts the raw `UIApplication.OpenURLOptionsKey`-keyed options dictionary as `userInfo`,
    // while `CDVSceneDelegate` (Cordova iOS 8) rebuilds it with plain string keys - this normalizes both shapes.
    private func openURLOptions(from userInfo: [AnyHashable: Any]?) -> [UIApplication.OpenURLOptionsKey: Any] {
        guard let userInfo = userInfo else {
            return [:]
        }

        var options: [UIApplication.OpenURLOptionsKey: Any] = [:]

        if let sourceApplication = (userInfo[UIApplication.OpenURLOptionsKey.sourceApplication] ?? userInfo["sourceApplication"]) as? String {
            options[.sourceApplication] = sourceApplication
        }

        if let annotation = userInfo[UIApplication.OpenURLOptionsKey.annotation] ?? userInfo["annotation"] {
            options[.annotation] = annotation
        }

        return options
    }

    private func sendResult(result: String?, error: NSError?, callBackID: String) {
        var pluginResult = CDVPluginResult.result(withStatus: CDVCommandStatus_ERROR)

        if let error = error, !error.localizedDescription.isEmpty {
            let errorCode = "OS-PLUG-SOCI-\(String(format: "%04d", error.code))"
            let errorMessage = error.localizedDescription
            let errorDict = ["code": errorCode, "message": errorMessage]
            pluginResult = CDVPluginResult.result(withStatus: CDVCommandStatus_ERROR, messageAsDictionary: errorDict);
        } else if let result = result {
            pluginResult = result.isEmpty ? CDVPluginResult.result(withStatus: CDVCommandStatus_OK) : CDVPluginResult.result(withStatus: CDVCommandStatus_OK, messageAsString: result)
        }

        self.commandDelegate.send(pluginResult, callbackId: callBackID);
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

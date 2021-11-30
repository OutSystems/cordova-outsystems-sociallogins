//
//  SocialLoginsGoogleProvider.swift
//  POC Social Logins iOS Test
//
//  Created by Carlos Correa on 30/11/2021.
//

import Foundation
import GoogleSignIn

class SocialLoginsGoogleProvider {
    
    var signInConfig: GIDConfiguration?
    var GIDSharedInstance = GIDSignIn.sharedInstance
    
    init() {
        if let url = externalURLScheme() {
            if let reverseURL = self.reverseURLScheme(url) {
                self.signInConfig = GIDConfiguration.init(clientID: reverseURL)
            }
        }
    }

    func reverseURLScheme(_ urlScheme:String) -> String? {
        let urlSchemeArr = urlScheme.split{$0 == "."}.map(String.init)
        let googleClientIDarr = urlSchemeArr.reversed()
        let googleClientID = googleClientIDarr.joined(separator:".")
        return googleClientID
    }
    
    func externalURLScheme() -> String? {
        if let urlTypes = Bundle.main.infoDictionary?["CFBundleURLTypes"] as? [AnyObject] {
            for urlType in urlTypes {
                if let urlSchemes = urlType["CFBundleURLSchemes"] as? [AnyObject] {
                    for urlScheme in urlSchemes {
                        if let externalURLScheme = urlScheme as? String {
                            if externalURLScheme.contains("google") {
                                return externalURLScheme
                            }
                        }
                    }
                }
            }
        }
        return ""
    }
    
    
    
}

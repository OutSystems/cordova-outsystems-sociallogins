import Foundation
import AuthenticationServices

class SocialLoginsPlugin: NSObject,
                          ASAuthorizationControllerDelegate,
                          ASAuthorizationControllerPresentationContextProviding {

    private var callbackID:String=""
    var delegate: SocialLoginsProtocol?
    var rootViewController:UIViewController?
    var appleProvider: SocialLoginsAppleProvider?
    var googleProvider: SocialLoginsGoogleProvider?
    var facebookProvider: SocialLoginsFacebookProvider?
    
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return self.rootViewController!.view as! ASPresentationAnchor
    }
    
    init (appleProvider: SocialLoginsAppleProvider, googleProvider: SocialLoginsGoogleProvider, facebookProvider: SocialLoginsFacebookProvider) {
        super.init()
        self.googleProvider = googleProvider
        self.appleProvider = appleProvider
        self.facebookProvider = facebookProvider
//        performExistingAccountSetupFlows() TODO: verify if this needs to be called.
    }
    
    func performExistingAccountSetupFlows() {
        let requests = [ASAuthorizationAppleIDProvider().createRequest(),
                        ASAuthorizationPasswordProvider().createRequest()]
        
        let authorizationController = ASAuthorizationController(authorizationRequests: requests)
        authorizationController.delegate = self
        authorizationController.presentationContextProvider = self
        authorizationController.performRequests()
    }
    
    func doLogin(callbackID:String, provider: String) throws {
        guard let provider = ProviderEnum(rawValue: provider) else { return }
        
        switch provider {
            case .apple:
                self.callbackID = callbackID
                let appleIDProvider = ASAuthorizationAppleIDProvider()
                let request = appleIDProvider.createRequest()
                request.requestedOperation = ASAuthorization.OpenIDOperation.operationLogin
                request.requestedScopes = [.fullName, .email]

                let authorizationController = ASAuthorizationController(authorizationRequests: [request])
                authorizationController.delegate = self
                authorizationController.presentationContextProvider = self
                authorizationController.performRequests()
            case .google:
                if let signInConfig = googleProvider?.signInConfig {
                    googleProvider?.GIDSharedInstance.signIn(with: signInConfig, presenting: rootViewController!) { user, error in
                        guard error == nil else { return }

                        if let user = user {
                            let name = user.profile?.name ?? ""
                            let email = user.profile?.email ?? ""
                            let userID = user.userID ?? ""
                            let accessToken = user.authentication.accessToken

                            let userResponse = UserInfoResponse(userIdentifier: userID,
                                                                email: email,
                                                                fullName: name,
                                                                identityToken: accessToken)

                            self.delegate?.callBackUserInfoResponse(result: userResponse, error: nil, callBackID: self.callbackID)
                        }
                    }
                }
            case .facebook:
                if let provider = self.facebookProvider {
                    provider.login(from: rootViewController) { result in
                        switch result {
                        case .success(let userResponse):
                            self.delegate?.callBackUserInfoResponse(result: userResponse, error: nil, callBackID: self.callbackID)
                        case .failure(_):
                            break
                        }
                        
                    }
                }
        }
    }
    
    func doLogout(callbackID:String, provider: String) throws {
        guard let provider = ProviderEnum(rawValue: provider) else { return }
        
        switch provider {
            case .google:
                self.googleProvider?.GIDSharedInstance.signOut()
                self.delegate?.callBackCredentialState(result: "loggedout", error: nil, callBackID: self.callbackID)
            case .facebook:
                if let provider = self.facebookProvider {
                    provider.logout()
                    self.delegate?.callBackCredentialState(result: "loggedout", error: nil, callBackID: self.callbackID)
                }
            default:
                break
        }
    }
    
    func getGoogleCredentialState(userID:String) {
        googleProvider?.GIDSharedInstance.restorePreviousSignIn { user, error in
            if error != nil || user == nil {
              // Show the app's signed-out state.
                self.delegate?.callBackCredentialState(result: "notFound", error: nil, callBackID: self.callbackID)
            } else {
              // Show the app's signed-in state.
                self.delegate?.callBackCredentialState(result: "authorized", error: nil, callBackID: self.callbackID)
            }
          }
    }
    
    func getAppleCredentialState(userID:String) {
        var ID: String = KeychainItem.currentUserIdentifier
        if !userID.isEmpty { ID = userID }
        
        if ID.isEmpty {
            self.delegate?.callBackCredentialState(result: "notFound", error: nil, callBackID: self.callbackID)
            return
        }
        
        let appleIDProvider = ASAuthorizationAppleIDProvider()
        appleIDProvider.getCredentialState(forUserID: ID) { (credentialState, error) in
            if let error = error { debugPrint(error) }
            switch credentialState {
            case .authorized:
                self.delegate?.callBackCredentialState(result: "authorized", error: nil, callBackID: self.callbackID)
                return
            case .revoked:
                self.delegate?.callBackCredentialState(result: "revoked", error: nil, callBackID: self.callbackID)
                return
            case .notFound:
                self.delegate?.callBackCredentialState(result: "notFound", error: nil, callBackID: self.callbackID)
                return
            default:
                break
            }
        }
    }
    
    func getCredentialState(userID:String, provider:String) throws {
        guard let provider = ProviderEnum(rawValue: provider) else { return }
        
        switch provider {
        case .apple:
            getAppleCredentialState(userID:userID)
        case .google:
            getGoogleCredentialState(userID: userID)
        case .facebook:
            if let provider = self.facebookProvider {
                provider.getCredentialState { result in
                    switch result {
                    case .success(_):
                        self.delegate?.callBackCredentialState(result: "authorized", error: nil, callBackID: self.callbackID)
                    case .failure(_):
                        self.delegate?.callBackCredentialState(result: "notFound", error: nil, callBackID: self.callbackID)
                    }
                }
            }
        }
    }
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        if let appleIDCredential = authorization.credential as?  ASAuthorizationAppleIDCredential {
            let userIdentifier = appleIDCredential.user
            let email = appleIDCredential.email ?? ""
            
            if let authorizationCode = appleIDCredential.authorizationCode {
                guard let authoCode = String(data: authorizationCode, encoding: .utf8) else {return}
                print(authoCode)
            }
                    
            if let token = appleIDCredential.identityToken {
                guard let strToken = String(data: token, encoding: .utf8) else {return}
                print(strToken)
                let userResponse = UserInfoResponse(userIdentifier: userIdentifier,
                                                    email: email,
                                                    fullName: "",
                                                    identityToken: strToken)
                
                self.saveUserInKeychain(userIdentifier)
                delegate?.callBackUserInfoResponse(result: userResponse, error: nil, callBackID: self.callbackID)
            }
        }
    }
    
    private func saveUserInKeychain(_ userIdentifier: String) {
        do {
            try KeychainItem(service: "com.outsystems.rd.sociallogins", account: "userIdentifier").saveItem(userIdentifier)
        } catch {
            print("Unable to save userIdentifier to keychain.")
        }
    }
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        // TODO: create a class for the errors
    }
    
}

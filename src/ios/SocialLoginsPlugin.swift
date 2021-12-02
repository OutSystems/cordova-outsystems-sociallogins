import Foundation
import AuthenticationServices

class SocialLoginsPlugin: NSObject,
                          ASAuthorizationControllerDelegate,
                          ASAuthorizationControllerPresentationContextProviding {

    private var callbackID:String=""
    var delegate: SocialLoginsProtocol?
    var rootViewController:UIViewController?
    
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return self.rootViewController!.view as! ASPresentationAnchor
    }
    
    override init() {
        super.init()
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
    
    func doLogin(callbackID:String) throws {
        self.callbackID = callbackID
        let appleIDProvider = ASAuthorizationAppleIDProvider()
        let request = appleIDProvider.createRequest()
        request.requestedOperation = ASAuthorization.OpenIDOperation.operationLogin
        request.requestedScopes = [.fullName, .email]

        let authorizationController = ASAuthorizationController(authorizationRequests: [request])
        authorizationController.delegate = self
        authorizationController.presentationContextProvider = self
        authorizationController.performRequests()
    }
    
    func doLogout(callbackID:String) throws {
//        Here we should clear all the user information in keychain or server side.
//        But apple does not provide a logout or revoke method
    }
    
    func getCredentialState(userID:String) throws {
        
        var ID: String = KeychainItem.currentUserIdentifier
        if !userID.isEmpty {
            ID = userID
        }
        
        if ID.isEmpty {
            self.delegate?.callBackCredentialState(result: "notFound", error: nil, callBackID: self.callbackID)
            return
        }
        
        let appleIDProvider = ASAuthorizationAppleIDProvider()
        appleIDProvider.getCredentialState(forUserID: ID) { (credentialState, error) in
            
            if let error = error {
                debugPrint(error)
            }
            
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
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithAuthorization authorization: ASAuthorization) {
        if let appleIDCredential = authorization.credential as?  ASAuthorizationAppleIDCredential {
            let userIdentifier = appleIDCredential.user
            let email = appleIDCredential.email ?? ""
                    
            if let token = appleIDCredential.identityToken {
                guard let strToken = String(data: token, encoding: .utf8) else {return}
                
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

struct UserInfoResponse: Encodable {
    let userIdentifier:String
    let email:String
    let fullName:String
    let identityToken:String
}

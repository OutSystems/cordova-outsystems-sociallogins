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
                
                delegate?.callBackUserInfoResponse(result: userResponse, error: nil, callBackID: self.callbackID)
            }
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

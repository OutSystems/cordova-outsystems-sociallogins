import Foundation
import AuthenticationServices

class SocialLoginsAppleController: NSObject, ASAuthorizationControllerDelegate, ASAuthorizationControllerPresentationContextProviding {
    
    private var callbackID:String=""
    var delegate: SocialLoginsProtocol?
    var rootViewController:UIViewController?
    
    init (delegate:SocialLoginsProtocol) {
        self.delegate = delegate
    }
 
    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return self.rootViewController!.view as! ASPresentationAnchor
    }
    
    func performExistingAccountSetupFlows() {
        let requests = [ASAuthorizationAppleIDProvider().createRequest(),
                        ASAuthorizationPasswordProvider().createRequest()]
        
        let authorizationController = ASAuthorizationController(authorizationRequests: requests)
        authorizationController.delegate = self
        authorizationController.presentationContextProvider = self
        authorizationController.performRequests()
    }
    
    func doLogin(callbackID:String) {
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
            let firstname = appleIDCredential.fullName?.givenName ?? ""
            let familyName = appleIDCredential.fullName?.familyName ?? ""
            
            if let token = appleIDCredential.identityToken {
                guard let strToken = String(data: token, encoding: .utf8) else {return}
                
                let userResponse = UserInfo(userIdentifier: userIdentifier,
                                            email: email,
                                            firstName: firstname,
                                            lastName: familyName,
                                            identityToken: strToken)
                
                delegate?.callBackUserInfoResponse(result: userResponse, error: nil, callBackID: self.callbackID)
            } else {
                delegate?.callBackUserInfoResponse(result: nil, error: nil, callBackID: self.callbackID)
            }
        }
            
    }
    
    func authorizationController(controller: ASAuthorizationController, didCompleteWithError error: Error) {
        // TODO: create a class for the errors
        delegate?.callBackUserInfoResponse(result: nil, error: error as NSError?, callBackID: self.callbackID)
    }
    
}

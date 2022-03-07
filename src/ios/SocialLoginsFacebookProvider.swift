// import FBSDKLoginKit

// protocol SocialLoginProviderProtocol {
//     func login(from viewController: UIViewController?, completion: @escaping (Result<UserInfoResponse, SocialLoginProviderError>) -> Void)
//     func logout()
//     func getCredentialState(completion: @escaping (Result<String, SocialLoginProviderError>) -> Void)
// }

// enum SocialLoginProviderError: Error {
//     case noTokenFound
//     case cancelledLoginError
//     case loginError(description: String)
// }

class SocialLoginsFacebookProvider
// : SocialLoginProviderProtocol 
{
    // private let manager = LoginManager()
    

    // func login(from viewController: UIViewController?, completion: @escaping (Result<UserInfoResponse, SocialLoginProviderError>) -> Void) {
        
    //     manager.logIn(permissions: ["public_profile"], from: viewController) { result, error in     // `public_profile` permission allows apps to read the Default Public Profile Fields on the User node
    //         if let error = error {
    //             completion(.failure(.loginError(description: error.localizedDescription)))
    //             return
    //         }

    //         guard
    //             let result = result,
    //             let accessToken = result.token
    //         else {
    //             completion(.failure(.noTokenFound))
    //             return
    //         }

    //         if !result.isCancelled {
    //             let request = GraphRequest(graphPath: "me", parameters: ["fields" : "id, email, name"]) // Graph API is used to get data in and out of Facebook's social graph.
    //             request.start { _, res, _ in
    //                 guard
    //                     let profileData = res as? [String : Any],
    //                     let profileEmail = profileData["email"] as? String,
    //                     let profileId = profileData["id"] as? String,
    //                     let profileFullName = profileData["name"] as? String
    //                 else { return }

    //                 let userInfo = UserInfoResponse(userIdentifier: profileId, email: profileEmail, fullName: profileFullName, identityToken: accessToken.tokenString)
    //                 completion(.success(userInfo))
    //             }
    //         } else {
    //             completion(.failure(.cancelledLoginError))
    //         }
    //     }
    // }

    // func logout() {
    //     manager.logOut()
    // }

    // func getCredentialState(completion: @escaping (Result<String, SocialLoginProviderError>) -> Void) {
    //     guard let accessToken = AccessToken.current else {
    //         completion(.failure(.noTokenFound))
    //         return
    //     }

    //     completion(.success(accessToken.userID))
    // }
}

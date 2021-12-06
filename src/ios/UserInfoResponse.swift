import Foundation

struct UserInfoResponse: Encodable {
    let userIdentifier:String
    let email:String
    let fullName:String
    let identityToken:String
}

enum ProviderEnum: String
{
    case apple = "APPLE",
         google = "GOOGLE"
}

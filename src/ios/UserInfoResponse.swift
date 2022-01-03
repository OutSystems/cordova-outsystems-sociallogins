import Foundation

struct UserInfoResponse: Encodable {
    let userIdentifier:String
    let email:String
    let fullName:String
    let identityToken:String
}

enum ProviderEnum: Int
{
    case apple = 57,
         google = 25
}

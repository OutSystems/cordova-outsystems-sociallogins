import Foundation

struct UserInfo: Encodable {
    let id:String
    let email:String
    let firstName:String
    let lastName:String
    let token:String
    let picture:String
}

enum ProviderEnum: String
{
    case apple = "APPLE",
         google = "GOOGLE"
}

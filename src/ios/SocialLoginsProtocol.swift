import Foundation

protocol SocialLoginsProtocol {
    func callBackUserInfoResponse(result: UserInfo?, error: NSError?, callBackID:String)
    func callBackCredentialState(result: String?, error: NSError?, callBackID:String)
}

extension String {
    public func decode<T: Decodable>(string:String) -> T {
        let data: Data? = string.data(using: .utf8)
        return try! JSONDecoder().decode(T.self, from: data!)
    }
}

extension Encodable {
    public func encode<T: Encodable>(object:T) -> String {
        let encoder = JSONEncoder()
        encoder.outputFormatting = .prettyPrinted
        let data = try! encoder.encode(object)
        return String(data: data, encoding: .utf8)!
    }
}

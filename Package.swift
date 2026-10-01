// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "com.outsystems.plugins.sociallogins",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "com.outsystems.plugins.sociallogins",
            targets: ["com.outsystems.plugins.sociallogins"])
    ],
    dependencies: [
        .package(url: "https://github.com/apache/cordova-ios.git", branch: "master"),
        .package(url: "https://github.com/google/GoogleSignIn-iOS.git", exact: "7.1.0"),
        .package(url: "https://github.com/facebook/facebook-ios-sdk.git", exact: "17.0.0")
    ],
    targets: [
        .binaryTarget(
            name: "OSSocialLoginsLib",
            path: "src/ios/frameworks/OSSocialLoginsLib.xcframework"
        ),
        .target(
            name: "com.outsystems.plugins.sociallogins",
            dependencies: [
                .product(name: "Cordova", package: "cordova-ios"),
                .product(name: "GoogleSignIn", package: "GoogleSignIn-iOS"),
                .product(name: "FacebookLogin", package: "facebook-ios-sdk"),
                .target(name: "OSSocialLoginsLib")
            ],
            path: "src/ios",
            exclude: [
                "frameworks/OSSocialLoginsLib.xcframework"
            ],
            publicHeadersPath: ".")
    ]
)
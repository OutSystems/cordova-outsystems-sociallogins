#import <XCTest/XCTest.h>
#import "OSSocialLogins.h"

// Exposing private methods for unit testing
@interface OSSocialLogins (Testing)
-(NSString*)privateHelloWorldString;
@end

@interface OSSocialLoginsLibTests : XCTestCase

@property (nonatomic,strong) OSSocialLogins *plugin;

@end

@implementation OSSocialLoginsLibTests

- (void)setUp {
    [super setUp];
    // Put setup code here. This method is called before the invocation of each test method in the class.
    
    self.plugin = [[OSSocialLogins alloc] init];
}

- (void)tearDown {
    // Put teardown code here. This method is called after the invocation of each test method in the class.
    [super tearDown];
}

-(void)testPrivateMethod{
    NSString *obtainedResult = [_plugin privateHelloWorldString];
    NSString *expectedResult = @"Hello World";
    XCTAssertEqualObjects(obtainedResult,expectedResult,@"A unexpected string was returned.");
}


- (void)testPublicHelloWorld {
    
    NSString *obtainedResult = [_plugin publicHelloWorld];
    XCTAssertNotNil(obtainedResult,@"A null string was returned.");
    
    NSString *expectedResult = @"Hello World";
    XCTAssertEqualObjects(obtainedResult,expectedResult,@"A unexpected string was returned.");
    
}

- (void)testPerformanceExample {
    // This is an example of a performance test case.
    [self measureBlock:^{
        // Put the code you want to measure the time of here.
    }];
}

@end

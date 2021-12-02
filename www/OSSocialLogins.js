var exec = require('cordova/exec');

exports.doLogin = function (success, error, provider) {
    exec(success, error, 'OSSocialLogins', 'doLogin', [provider]);
};

exports.logout = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'logout', [arg0]);
};

exports.getLoginData = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'getLoginData', [arg0]);
};

exports.checkLoginStatus = function (success, error, provider) {
    exec(success, error, 'OSSocialLogins', 'checkLoginStatus', [provider]);
};

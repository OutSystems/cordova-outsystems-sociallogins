var exec = require('cordova/exec');

exports.login = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'login', [arg0]);
};

exports.logout = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'logout', [arg0]);
};

exports.getLoginData = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'getLoginData', [arg0]);
};

exports.checkLoginStatus = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'checkLoginStatus', [arg0]);
};

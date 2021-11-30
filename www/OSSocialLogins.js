var exec = require('cordova/exec');

exports.doLogin = function (success, error) {
    exec(success, error, 'OSSocialLogins', 'doLogin');
};

exports.isLoggedIn = function (success, error, arg0) {
    exec(success, error, 'OSSocialLogins', 'isLoggedIn', [arg0]);
};
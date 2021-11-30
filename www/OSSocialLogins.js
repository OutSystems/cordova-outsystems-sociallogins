var exec = require('cordova/exec');

exports.coolMethod = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'coolMethod', [arg0]);
};

exports.login = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'login', [arg0]);
};

exports.logout = function (arg0, success, error) {
    exec(success, error, 'OSSocialLogins', 'logout', [arg0]);
};

var exec = require('cordova/exec');

exports.doLogin = function (success, error) {
    exec(success, error, 'OSSocialLogins', 'doLogin');
};

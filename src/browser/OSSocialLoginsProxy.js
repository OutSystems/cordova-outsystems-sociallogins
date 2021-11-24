
function doLogin (success, error, opts) {
    console.log("Hello social logins!");
}


module.exports = {
    coolMethod: doLogin,
    cleanup: function () {}
};

require('cordova/exec/proxy').add('OSSocialLogins', module.exports);
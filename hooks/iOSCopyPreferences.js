const et = require('elementtree');
const path = require('path');
const fs = require('fs');
const { ConfigParser } = require('cordova-common');
const { Console } = require('console');

module.exports = function (context) {

    const ProvidersEnum = Object.freeze({"Apple":"1", "Facebook":"2", "Google":"3", "LinkedIn":"4"})
    var projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;

    var google_client_id = "";
    var appNamePath = path.join(projectRoot, 'config.xml');
    var appNameParser = new ConfigParser(appNamePath);
    var appName = appNameParser.name();

    //read json config file                         platforms/ios/www/jsonConfig
    var jsonConfig = path.join(projectRoot, 'platforms/ios/www/jsonConfig/sociallogins_configurations.json');
    var jsonConfigFile = fs.readFileSync(jsonConfig).toString();
    console.log(jsonConfigFile);
    var jsonParsed = JSON.parse(jsonConfigFile);

    jsonParsed.forEach(function(configItem) {
        if (configItem.AuthenticationConfiguration.ProviderId == ProvidersEnum.Google) {
            google_client_id = configItem.ClientId 
        }
    });

    //Change info.plist
    var infoPlistPath = path.join(projectRoot, 'platforms/ios/' + appName + '/'+ appName +'-info.plist');
    var infoPlistFile = fs.readFileSync(infoPlistPath).toString();
    var etreeInfoPlist = et.parse(infoPlistFile);
    var infoPlistTags = etreeInfoPlist.findall('./dict/array/dict/array/string');

    for (var i = 0; i < infoPlistTags.length; i++) {
        if (infoPlistTags[i].text.includes("GOOGLE_CLIENT_ID")) {
            infoPlistTags[i].text = infoPlistTags[i].text.replace('GOOGLE_CLIENT_ID', google_client_id)
        }
    }

    var resultXmlInfoPlist = etreeInfoPlist.write();
    fs.writeFileSync(infoPlistPath, resultXmlInfoPlist);

};

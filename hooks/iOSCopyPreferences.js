const et = require('elementtree');
const path = require('path');
const fs = require('fs');
const { ConfigParser } = require('cordova-common');
const { Console } = require('console');

module.exports = function (context) {

    const ProvidersEnum = Object.freeze({"apple":"1", "facebook":"2", "google":"3", "linkedIn":"4"})
    const ApplcationTypeEnum = Object.freeze({"web":"1", "ios":"2", "android":"3"})
    var projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;

    var google_client_id = "";
    var appNamePath = path.join(projectRoot, 'config.xml');
    var appNameParser = new ConfigParser(appNamePath);
    var appName = appNameParser.name();

    //read json config file                         platforms/ios/www/jsonConfig
    var jsonConfig = "";
    try {
        jsonConfig = path.join(projectRoot, 'platforms/ios/www/jsonConfig/sociallogins_configurations.json');
        var jsonConfigFile = fs.readFileSync(jsonConfig).toString();
        var jsonParsed = JSON.parse(jsonConfigFile);
    
        jsonParsed.environment_configurations.forEach(function(configItem) {
            if ((configItem.provider_id == ProvidersEnum.google) && (configItem.application_type_id == ApplcationTypeEnum.ios)) {
                google_client_id = configItem.client_id 
            }
        });

    } catch {
        throw new Error("Missing configuration file or error trying to obtain the configuration.");
        //console.log();
    }

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

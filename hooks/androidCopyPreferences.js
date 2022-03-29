const et = require('elementtree');
const path = require('path');
const fs = require('fs');
const { ConfigParser } = require('cordova-common');
const { Console } = require('console');

const ProvidersEnum = Object.freeze({"apple":"1", "facebook":"2", "google":"3", "linkedIn":"4"})
const ApplicationTypeEnum = Object.freeze({"web":"1", "ios":"2", "android":"3"})

module.exports = function (context) {
    var projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;

    copyFacebookPreferences(projectRoot);

    var configXML = path.join(projectRoot, 'config.xml');
    var configParser = new ConfigParser(configXML);
    var default_hostname = configParser.getGlobalPreference("DefaultHostname");

    //go inside the AndroidManifest and change value for DEFAULT_HOSTNAME
    var manifestPath = path.join(projectRoot, 'platforms/android/app/src/main/AndroidManifest.xml');
    var manifestFile = fs.readFileSync(manifestPath).toString();
    var etreeManifest = et.parse(manifestFile);

    var dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:host="DEFAULT_HOSTNAME"]');
    for (var data of dataTags) {
        data.set("android:host", default_hostname);
    }

    var resultXmlManifest = etreeManifest.write();
    fs.writeFileSync(manifestPath, resultXmlManifest);
};

function copyFacebookPreferences(projectRoot) {

    var facebook_client_appId = "";
    var facebook_client_token = "";

    var jsonConfig = "";
    try {
        jsonConfig = path.join(projectRoot, 'www/json-config/SocialLoginsConfigurations.json');
        var jsonConfigFile = fs.readFileSync(jsonConfig).toString();
        var jsonParsed = JSON.parse(jsonConfigFile);
    
        jsonParsed.app_configurations.forEach(function(configItem) {
            if ((configItem.provider_id == ProvidersEnum.facebook) && 
                (configItem.application_type_id == ApplicationTypeEnum.android)) {
                facebook_client_appId = configItem.client_id;
                facebook_client_token = configItem.client_token;
            }
        });
    } catch {
        throw new Error("Missing configuration file or error trying to obtain the configuration.");
    }

    var stringsPath = path.join(projectRoot, 'platforms/android/app/src/main/res/values/strings.xml');
    var stringsFile = fs.readFileSync(stringsPath).toString();
    var etreeStrings = et.parse(stringsFile);

    var appIdTags = etreeStrings.findall('./string/[@name="facebook_app_id"]');
    for (var tag of appIdTags) {
        tag.text = facebook_client_appId;
    }

    var clientTokenTags = etreeStrings.findall('./string/[@name="facebook_client_token"]');
    for (var tag of clientTokenTags) {
        tag.text = facebook_client_token;
    }

    var resultXmlStrings = etreeStrings.write();
    fs.writeFileSync(stringsPath, resultXmlStrings);

};

const et = require('elementtree');
const path = require('path');
const fs = require('fs');
const { ConfigParser } = require('cordova-common');
const { Console } = require('console');

module.exports = function (context) {

    const ProvidersEnum = Object.freeze({"apple":"1", "facebook":"2", "google":"3", "linkedIn":"4"})
    const ApplicationTypeEnum = Object.freeze({"web":"1", "ios":"2", "android":"3"})


    var linkedin_deeplink_url = "";
    var linkedin_deeplink_host = "";
    var linkedin_deeplink_path = "";
    var projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;
    var configXML = path.join(projectRoot, 'config.xml');
    var configParser = new ConfigParser(configXML);
    
    //read json config file             /Users/carloscorrea/Downloads/source 9/www/jsonConfig/sociallogins_configurations.json
    var jsonConfig = "";
    try {
        jsonConfig = path.join(projectRoot, 'www/jsonConfig/sociallogins_configurations.json');
        var jsonConfigFile = fs.readFileSync(jsonConfig).toString();
        var jsonParsed = JSON.parse(jsonConfigFile);

        jsonParsed.environment_configurations.forEach(function(configItem) {
            //Todo: this config will be related with the app and not with the providers 
            if ((configItem.provider_id == ProvidersEnum.linkedIn) && (configItem.application_type_id == ApplicationTypeEnum.web)) {
                linkedin_deeplink_url = configItem.url_scheme
                var deeplink_url = configItem.deeplink_url
                linkedin_deeplink_host = deeplink_url.substring(0, deeplink_url.indexOf("/"));
                linkedin_deeplink_path = deeplink_url.substring(deeplink_url.indexOf("/"), deeplink_url.length);
            } 

        });
    } catch {
        throw new Error("Missing configuration file or error trying to obtain the configuration.");
    }
    
    //go inside the AndroidManifest and change values for schema, host and path
    var manifestPath = path.join(projectRoot, 'platforms/android/app/src/main/AndroidManifest.xml');
    var manifestFile = fs.readFileSync(manifestPath).toString();
    var etreeManifest = et.parse(manifestFile);

    var dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:scheme="LINKEDIN_DEEPLINK_SCHEME"]');
    for (var i = 0; i < dataTags.length; i++) {
        var data = dataTags[i];
        data.set("android:scheme", linkedin_deeplink_url);
    }
    dataTags = null;

    dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:host="LINKEDIN_DEEPLINK_HOST"]');
    for (var i = 0; i < dataTags.length; i++) {
        var data = dataTags[i];
        data.set("android:host", linkedin_deeplink_host);
    }

    dataTags = null;
    dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:path="LINKEDIN_DEEPLINK_PATH"]');
    for (var i = 0; i < dataTags.length; i++) {
        var data = dataTags[i];
        data.set("android:path", linkedin_deeplink_path);
    }

    var resultXmlManifest = etreeManifest.write();
    fs.writeFileSync(manifestPath, resultXmlManifest);
};

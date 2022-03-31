const et = require('elementtree');
const path = require('path');
const fs = require('fs');

module.exports = function (context) {
    const ProvidersEnum = Object.freeze({"apple":"1", "facebook":"2", "google":"3", "linkedIn":"4"})
    const ApplicationTypeEnum = Object.freeze({"web":"1", "ios":"2", "android":"3"})

    const configFileName = 'www/json-config/SocialLoginsConfigurations.json';
    var linkedin_deeplink_url = "";
    var linkedin_deeplink_host = "";
    var linkedin_deeplink_path = "";
    var projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;

     //read json config file       www/jsonConfig/sociallogins_configurations.json
     var jsonConfig = "";
     try {
         jsonConfig = path.join(projectRoot, configFileName);
         var jsonConfigFile = fs.readFileSync(jsonConfig).toString();
         var jsonParsed = JSON.parse(jsonConfigFile);
 
         linkedin_deeplink_url = jsonParsed.app_deeplink.url_scheme.replace(/\s/g, '')
         linkedin_deeplink_host = jsonParsed.app_deeplink.url_host.replace(/\s/g, '')
         linkedin_deeplink_path = jsonParsed.app_deeplink.url_path.replace(/\s/g, '')
 
     } catch {
         throw new Error("Missing configuration file or error trying to obtain the configuration.");
     }

    //go inside the AndroidManifest and change values for schema, host and path
    var manifestPath = path.join(projectRoot, 'platforms/android/app/src/main/AndroidManifest.xml');
    var manifestFile = fs.readFileSync(manifestPath).toString();
    var etreeManifest = et.parse(manifestFile);

    var dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:scheme="OAUTH_DEEPLINK_SCHEME"]');
    for (var i = 0; i < dataTags.length; i++) {
        var data = dataTags[i];
        data.set("android:scheme", linkedin_deeplink_url);
    }
    dataTags = null;

    dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:host="OAUTH_DEEPLINK_HOST"]');
    for (var i = 0; i < dataTags.length; i++) {
        var data = dataTags[i];
        data.set("android:host", linkedin_deeplink_host);
    }

    dataTags = null;
    dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:path="OAUTH_DEEPLINK_PATH"]');
    for (var i = 0; i < dataTags.length; i++) {
        var data = dataTags[i];
        data.set("android:path", linkedin_deeplink_path);
    }

    var resultXmlManifest = etreeManifest.write();
    fs.writeFileSync(manifestPath, resultXmlManifest);
    
};

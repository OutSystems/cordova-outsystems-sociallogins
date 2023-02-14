const et = require('elementtree');
const path = require('path');
const fs = require('fs');
const { ConfigParser } = require('cordova-common');
const ProvidersEnum = Object.freeze({"apple":"1", "facebook":"2", "google":"3", "linkedIn":"4"})
const ApplicationTypeEnum = Object.freeze({"web":"1", "ios":"2", "android":"3"})


module.exports = async function (context) {

    
    let linkedin_deeplink_url = "";
    let linkedin_deeplink_host = "";
    let linkedin_deeplink_path = "";
    let projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;

    let configXML = path.join(projectRoot, 'config.xml');
    let configParser = new ConfigParser(configXML);
    let configuratorBaseURL = configParser.getGlobalPreference("CONFIGURATOR_BASE_URL");
    let appName = configParser.name();
    
    let jsonConfig = await getJsonFile(configuratorBaseURL, appName);
    copyFacebookPreferences(jsonConfig);

    try {
        linkedin_deeplink_url = jsonConfig.app_deeplink.url_scheme.replace(/\s/g, '')
        linkedin_deeplink_host = jsonConfig.app_deeplink.url_host.replace(/\s/g, '')
        linkedin_deeplink_path = jsonConfig.app_deeplink.url_path.replace(/\s/g, '')
    } catch {
        throw new Error("Missing configuration file or error trying to obtain the configuration.");
    }

    //go inside the AndroidManifest and change values for schema, host and path
    let manifestPath = path.join(projectRoot, 'platforms/android/app/src/main/AndroidManifest.xml');
    let manifestFile = fs.readFileSync(manifestPath).toString();
    let etreeManifest = et.parse(manifestFile);
  
    let dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:scheme="OAUTH_DEEPLINK_SCHEME"]');
    dataTags.forEach((data) => data.set("android:scheme", linkedin_deeplink_url));
    dataTags = null;

    dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:host="OAUTH_DEEPLINK_HOST"]');
    dataTags.forEach((data) => data.set("android:host", linkedin_deeplink_host));
    dataTags = null;

    dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:path="OAUTH_DEEPLINK_PATH"]');
    dataTags.forEach((data) => data.set("android:path", linkedin_deeplink_path));
   
    let resultXmlManifest = etreeManifest.write();
    fs.writeFileSync(manifestPath, resultXmlManifest);
    
};

async function getJsonFile(baseURL, appName){
    let jsonURL = baseURL + `/SocialLoginConfigurator/rest/v1/configurations?AppName=${appName}`;
    let response = await fetch(jsonURL, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    });
    
    if(response.status != 200){
        let errorJSON = await response.json();
        throw new Error(errorJSON.Errors[0]);
    }
    let json = await response.json(); 
    return json;
    
};

function copyFacebookPreferences(jsonConfig) {

    let facebook_client_appId = "";
    let facebook_client_token = "";

    try {
        jsonConfig.app_configurations.forEach( (configItem) =>{
            if ((configItem.provider_id == ProvidersEnum.facebook) && 
                (configItem.application_type_id == ApplicationTypeEnum.android)) {
                facebook_client_appId = configItem.client_id;
                facebook_client_token = configItem.client_token;
            }
        });
    } catch {
        throw new Error("Missing configuration file or error trying to obtain the configuration.");
    }

    let stringsPath = path.join(projectRoot, 'platforms/android/app/src/main/res/values/strings.xml');
    let stringsFile = fs.readFileSync(stringsPath).toString();
    let etreeStrings = et.parse(stringsFile);

    let appIdTags = etreeStrings.findall('./string/[@name="facebook_app_id"]');
    for (let tag of appIdTags) {
        tag.text = facebook_client_appId;
    }

    let clientTokenTags = etreeStrings.findall('./string/[@name="facebook_client_token"]');
    for (let tag of clientTokenTags) {
        tag.text = facebook_client_token;
    }

    let resultXmlStrings = etreeStrings.write();
    fs.writeFileSync(stringsPath, resultXmlStrings);

};

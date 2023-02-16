const et = require('elementtree');
const path = require('path');
const fs = require('fs');
const plist = require('plist');
const { ConfigParser } = require('cordova-common');
const { Console } = require('console');

const {getJsonFile, ProvidersEnum, ApplicationTypeEnum} = require('./utils');

module.exports = async function (context) {
    var projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;

    var google_url_scheme = "";
    
    var facebook_client_appId = "";
    var facebook_client_token = "";

    var use_apple_signin = true;

    var deeplink_url_scheme = "";

    var appNamePath = path.join(projectRoot, 'config.xml');
    var configParser = new ConfigParser(appNamePath);
    var appName = configParser.name();

    let platformPath = path.join(projectRoot, 'platforms/ios');
    let configuratorURL = configParser.getGlobalPreference("SOCIAL_CONF_API_ENDPOINT");
    
    if(configuratorURL.length == 0)
        throw new Error("Missing preference: SOCIAL_CONF_API_ENDPOINT. Please make sure this preference is configured");
    
    let jsonConfig = await getJsonFile(configuratorURL, appName);

    const iOSConfigArray = jsonConfig.app_configurations.filter(configItem => configItem.application_type_id == ApplicationTypeEnum.ios);
    const errorMap = new Map();

    iOSConfigArray.forEach(function(configItem) {
        if (configItem.provider_id == ProvidersEnum.google) {
            var googleErrorArray = [];

            if (configItem.url_scheme != null && configItem.url_scheme !== "") {
                google_url_scheme = configItem.url_scheme;
            } else {
                googleErrorArray.push('URL Scheme');
            }

            if (googleErrorArray.length > 0) {
                errorMap['Google'] = googleErrorArray;
            }
        } else if (configItem.provider_id == ProvidersEnum.facebook) {
            var facebookErrorArray = [];

            if (configItem.client_id != null && configItem.client_id !== "") {
                console.log(facebook_client_appId);
                facebook_client_appId = configItem.client_id;
            } else {
                facebookErrorArray.push('Client ID');
            }

            if (configItem.client_token != null && configItem.client_token !== "") {
                console.log(facebook_client_token);
                facebook_client_token = configItem.client_token;
            } else {
                facebookErrorArray.push('Client Token');
            }

            if (facebookErrorArray.length > 0) {
                errorMap['Facebook'] = facebookErrorArray;
            }
        } else if (configItem.provider_id == ProvidersEnum.apple) {
            if (!configItem.is_enabled) {
                use_apple_signin = false;    
            }
        }
    });

    if (jsonConfig.app_deeplink.url_scheme != null && jsonConfig.app_deeplink.url_scheme !== "") {
        deeplink_url_scheme = jsonConfig.app_deeplink.url_scheme;
    } else {
        errorMap['General'] = ['URL Scheme'];
    }

    if (Object.getOwnPropertyNames(errorMap).length > 0) {
        var errorMessage = 'Configuration is missing the following fields: ';        

        for (const [key, value] of Object.entries(errorMap)) {
            errorMessage += '\n - On ' + key + ': ' + value + '.';
        }
        throw new Error(errorMessage);
    }
    
    //Change info.plist
    var infoPlistPath = path.join(platformPath, appName + '/'+ appName +'-info.plist');
    var infoPlistFile = fs.readFileSync(infoPlistPath, 'utf8');
    var infoPlist = plist.parse(infoPlistFile);

    const initialCFBundleURLTypeArray = infoPlist['CFBundleURLTypes'];
    var finalCFBundleURLTypeArray = [];
    initialCFBundleURLTypeArray.forEach(function(item) {
        if (item['CFBundleURLName'] == 'Google' && google_url_scheme != '') {
            item['CFBundleURLSchemes'] = [google_url_scheme];
            finalCFBundleURLTypeArray.push(item);
        } else if (item['CFBundleURLName'] == 'Facebook' && facebook_client_appId != '') {
            item['CFBundleURLSchemes'] = ['fb' + facebook_client_appId];
            finalCFBundleURLTypeArray.push(item);
        } else if (item['CFBundleURLName'] == 'DeepLinkScheme') {
            item['CFBundleURLSchemes'] = [deeplink_url_scheme];
            finalCFBundleURLTypeArray.push(item);
        }
    });

    infoPlist['CFBundleURLTypes'] = finalCFBundleURLTypeArray;
    if (facebook_client_appId != '') {
        infoPlist['FacebookAppID'] = facebook_client_appId
    } else {
        delete infoPlist['FacebookAppID'];
    }

    if (facebook_client_token != '') {
        infoPlist['FacebookClientToken'] = facebook_client_token;
    } else {
        delete infoPlist['FacebookClientToken'];
    }

    fs.writeFileSync(infoPlistPath, plist.build(infoPlist, { indent: '\t' }));

    if (!use_apple_signin) {
        // Change Entitlements files
        var debugEntitlementsPath = path.join(platformPath, appName + '/'+ 'Entitlements-Debug.plist');
        var debugEntitlementsFile = fs.readFileSync(debugEntitlementsPath, 'utf8');
        var debugEntitlements = plist.parse(debugEntitlementsFile);    
        delete debugEntitlements['com.apple.developer.applesignin'];
        fs.writeFileSync(debugEntitlementsPath, plist.build(debugEntitlements, { indent: '\t' }));

        var releaseEntitlementsPath = path.join(platformPath, appName + '/' + 'Entitlements-Release.plist');
        var releaseEntitlementsFile = fs.readFileSync(releaseEntitlementsPath, 'utf8');
        var releaseEntitlements = plist.parse(releaseEntitlementsFile);
        delete releaseEntitlements['com.apple.developer.applesignin'];
        fs.writeFileSync(releaseEntitlementsPath, plist.build(releaseEntitlements, { indent: '\t' }));
    }
};

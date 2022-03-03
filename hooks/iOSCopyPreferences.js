const et = require('elementtree');
const path = require('path');
const fs = require('fs');
const { ConfigParser } = require('cordova-common');
const { Console } = require('console');

module.exports = function (context) {

    var projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;
    var configXML = path.join(projectRoot, 'config.xml');
    var configParser = new ConfigParser(configXML);
    var google_client_id = configParser.getGlobalPreference("GOOGLE_CLIENT_ID");

    var facebook_client_appId = configParser.getGlobalPreference("FACEBOOK_CLIENT_APPID");
    var facebook_client_token = configParser.getGlobalPreference("FACEBOOK_CLIENT_TOKEN");
    var facebook_client_displayName = configParser.getGlobalPreference("FACEBOOK_CLIENT_DISPLAYNAME");

    var appNamePath = path.join(projectRoot, 'config.xml');
    var appNameParser = new ConfigParser(appNamePath);
    var appName = appNameParser.name();

    //Change info.plist
    var infoPlistPath = path.join(projectRoot, 'platforms/ios/' + appName + '/'+ appName +'-info.plist');
    var infoPlistFile = fs.readFileSync(infoPlistPath).toString();
    var etreeInfoPlist = et.parse(infoPlistFile);
    var infoPlistTags = etreeInfoPlist.findall('./dict/array/dict/array/string');

    for (var i = 0; i < infoPlistTags.length; i++) {
        if (infoPlistTags[i].text.includes("GOOGLE_CLIENT_ID")) {
            infoPlistTags[i].text = infoPlistTags[i].text.replace('GOOGLE_CLIENT_ID', google_client_id)
            console.log(infoPlistTags[i].text);
        } else if (infoPlistTags[i].text.includes("FACEBOOK_CLIENT_APPID")) {
            infoPlistTags[i].text = infoPlistTags[i].text.replace('FACEBOOK_CLIENT_APPID', facebook_client_appId)
            console.log(infoPlistTags[i].text);
        } else if (infoPlistTags[i].text.includes("FACEBOOK_CLIENT_TOKEN")) {
            infoPlistTags[i].text = infoPlistTags[i].text.replace('FACEBOOK_CLIENT_TOKEN', facebook_client_token)
            console.log(infoPlistTags[i].text);
        } else if (infoPlistTags[i].text.includes("FACEBOOK_CLIENT_DISPLAYNAME")) {
            infoPlistTags[i].text = infoPlistTags[i].text.replace('FACEBOOK_CLIENT_DISPLAYNAME', facebook_client_displayName)
            console.log(infoPlistTags[i].text);
        }
    }

    var resultXmlInfoPlist = etreeInfoPlist.write();
    fs.writeFileSync(infoPlistPath, resultXmlInfoPlist);

};

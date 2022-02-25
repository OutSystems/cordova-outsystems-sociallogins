const et = require('elementtree');
const path = require('path');
const fs = require('fs');
const { ConfigParser } = require('cordova-common');
const { Console } = require('console');

module.exports = function (context) {
    var projectRoot = context.opts.cordova.project ? context.opts.cordova.project.root : context.opts.projectRoot;
    var configXML = path.join(projectRoot, 'config.xml');
    var configParser = new ConfigParser(configXML);
    var default_hostname = configParser.getGlobalPreference("DefaultHostname");

    console.log("default_hostname: " + default_hostname);

    
    //go inside the AndroidManifest and change value for DEFAULT_HOSTNAME
    var manifestPath = path.join(projectRoot, 'platforms/android/app/src/main/AndroidManifest.xml');
    var manifestFile = fs.readFileSync(manifestPath).toString();
    var etreeManifest = et.parse(manifestFile);

    console.log("ElementTree fez o parse do manifest");

    var dataTags1 = etreeManifest.findall('./application/activity/intent-filter/data[@android:scheme]');
    console.log("Number of data tags: " + dataTags1.length);

    var appleActivityTags = etreeManifest.findall('./application/activity[@android:name="com.outsystems.plugins.sociallogins.AppleSignInActivity"]');
    console.log("Number of apple activity tags: " + appleActivityTags.length);

    var dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:host="DEFAULT_HOSTNAME"]');
    console.log("dataTags.length: " + dataTags.length);

    for (var i = 0; i < dataTags.length; i++) {
        var data = dataTags[i];
        data.set("android:host", default_hostname);
        console.log("entrou no for");
    }

    var resultXmlManifest = etreeManifest.write();
    fs.writeFileSync(manifestPath, resultXmlManifest);

    console.log("acabou, fez o write");
    
};

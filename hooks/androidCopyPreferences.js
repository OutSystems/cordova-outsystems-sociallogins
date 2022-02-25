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

    
    //go inside the AndroidManifest and change value for DEFAULT_HOSTNAME
    var manifestPath = path.join(projectRoot, 'platforms/android/app/src/main/AndroidManifest.xml');
    var manifestFile = fs.readFileSync(manifestPath).toString();
    var etreeManifest = et.parse(manifestFile);

    var dataTags = etreeManifest.findall('./application/activity/intent-filter/data[@android:host="DEFAULT_HOSTNAME"]');
    for (var i = 0; i < dataTags.length; i++) {
        var data = dataTags[i];
        data.set("android:host", default_hostname);
    }

    var resultXmlManifest = etreeManifest.write();
    fs.writeFileSync(manifestPath, resultXmlManifest);
    
};

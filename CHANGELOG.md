# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

The changes documented here do not include those from the original repository.

## [Unreleased]

### 2024-01-30
- Fix: [iOS] Unable for an app to route into the screen mentioned in the deep link (using the `ApplicationID`). (https://outsystemsrd.atlassian.net/browse/RMET-3139)

## Version 2.0.2 (2024-01-23)

### 2024-01-22
- Fix: [iOS] Unable to use `Application ID` as `URL Scheme` when the plugin is installed. (https://outsystemsrd.atlassian.net/browse/RMET-3063)

## Version 2.0.1 (2023-10-24)

### 2023-10-18
- Fix: Updated facebook-android-sdk version do MABS10 builds work with Facebook login.

## Version 2.0.0 (2023-03-21)

### 2023-03-20
- Feat: Update error code format
- Feat: [Android] Update error codes and descriptions (https://outsystemsrd.atlassian.net/browse/RMET-2343)

### 2023-03-16
- Feat: [iOS] Update error codes and descriptions (https://outsystemsrd.atlassian.net/browse/RMET-2342).

### 2023-02-16
- Feat: Replace JSON file dependency by endpoint (https://outsystemsrd.atlassian.net/browse/RMET-2252). 

### 2022-12-19
- Fix: [iOS] Make Apple Sign In optionally, enabling app generation without it (https://outsystemsrd.atlassian.net/browse/RMET-2060).

## Version 1.0.3 (2022-11-09)

### 2022-11-08
- Fix: [iOS] Replace the old `OSCore` framework for the new `OSCommonPluginLib` pod.

## Version 1.0.2 (2022-10-25)

### 2022-10-24
- Fix: [Android] Updated code to handle the new library version (https://outsystemsrd.atlassian.net/browse/RMET-1969).

### 2022-10-18
- Fix: [iOS] Update hook so that providers can be individually configured and not all are required for the plugin to work.
- Fix: [iOS] Improve error handling, so that more detailed messages can be thrown on build process.

### 2022-10-12
- Fix: [iOS] Rename the swizzled `appDelegate:didFinishLaunchingWithOptions:` method to something unique (https://outsystemsrd.atlassian.net/jira/software/c/projects/RMET/boards/893?selectedIssue=RPM-3153).

## Version 1.0.1 (2022-06-29)

### 2022-06-29
- Fix: Removed hook that adds swift support and added the plugin as dependecy. (https://outsystemsrd.atlassian.net/browse/RMET-1680)

## Version 1.0.0 (2022-04-11)

### 2022-04-05
- Hiding blank OAuthActivity called for Apple and LinkedIn login (https://outsystemsrd.atlassian.net/browse/RMET-1536)
- Removed suplicated files and added .arr dependencies (https://outsystemsrd.atlassian.net/browse/RMET-1476)

### 2022-04-01
- Fixing if google email is empty or null (https://outsystemsrd.atlassian.net/browse/RMET-1422)

### 2022-03-28
- Implementation of LinkedIn Sign In on Android (https://outsystemsrd.atlassian.net/browse/RMET-1450)

### 2022-03-25
- Implementation of LinkedIn Sign In on iOS (https://outsystemsrd.atlassian.net/browse/RMET-1449)

### 2022-03-16
- Implementation of Google Sign In on Android (https://outsystemsrd.atlassian.net/browse/RMET-1246)
- Implementation of Facebook Sign In on iOS (https://outsystemsrd.atlassian.net/browse/RMET-1420) & (https://outsystemsrd.atlassian.net/browse/RMET-1421)

### 2022-03-03
- Implementation of Apple Sign In on iOS (https://outsystemsrd.atlassian.net/browse/RMET-1405)
- Apple Sign In implementation for Android (https://outsystemsrd.atlassian.net/browse/RMET-1406)

### 2021-11-30
- PoC Implementation of the Google Sign In for Android (https://outsystemsrd.atlassian.net/browse/RMET-1152)

### 2021-11-29
- PoC Implementation of the Apple Sign In for Android (https://outsystemsrd.atlassian.net/browse/RMET-1213)

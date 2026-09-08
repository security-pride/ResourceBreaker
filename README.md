# ResourceBreaker

This repository contains the artifacts for the paper *"One Resource to Break Them All: Exploiting Malformed Resources for Permanent Denial-of-Service in Android"*.

## Repository Structure

```txt
├── README.md
├── ResourceBreaker/                    # Crash path discovery framework
│   ├── OOM_SOF_Finder/                 # Field-level OOM and StackOverflow element finder
│   │   ├── README.md
│   │   ├── gen/                        # Generated ANTLR resource files
│   │   ├── libs/                       # Dependencies
│   │   ├── pom.xml
│   │   ├── results.txt                 # Analysis results
│   │   └── src/                        # Source code
│   ├── appshark/                       # Static taint analysis engine
│   │   ├── AppShark-0.1.2-all-c1.jar   # Pre-built JAR for C1 source configuration
│   │   ├── AppShark-0.1.2-all-c2.jar   # Pre-built JAR for C2 source configuration
│   │   ├── c1-violate/                 # Taint analysis rules and results for C1 violations
│   │   ├── c2-violate/                 # Taint analysis rules and results for C2 violations
│   │   └── src/                        # Source code for entry-model generalisation,
│   │                                   # structural taint propagation, and
│   │                                   # cross-component resolution modules
│   └── fuzzer/                         # Coverage-guided fuzzing harnesses
│       ├── android-15.0.0_r1/          # Diff files for AFL++ harnesses
│       └── results.md                  # Fuzzing results and crash site summary
└── SafeModeAnalysis/                   # Safe Mode filtering audit
    ├── C1_ReachabilityAnalysis/        # API-level coverage analysis (C1)
    │   ├── call_hierarchy.dot          # Call graph 
    │   ├── config.json                 # Analysis configuration
    │   ├── output/                     
    │   ├── pm_field_mappings.csv      
    │   ├── pm_field_mappings_metadata.csv
    │   ├── pm_field_mappings_parsed.csv
    │   ├── pom.xml
    │   ├── result.md                   # Summary of C1 audit results
    │   ├── run.sh                      # Entry script
    │   └── src/                        # Soot-based backward reachability auditor
    └── C2_SafeModeInstrumentation/     # Boot-time temporal coverage analysis (C2)
        ├── Instrumentation.diff        # Instrumentation patch for AOSP Android 15
        ├── result.md                   # Summary of C2 audit results
        └── result_raw.txt              # Raw boot-time query logs
```
## Components

### SafeModeAnalysis

**C1_ReachabilityAnalysis** — A Soot-based static analysis tool that enumerates all 224 PMS Binder methods and performs backward breadth-first search from `ComputerEngine.safeMode()` to identify APIs that never consult the Safe Mode flag. Identifies 8 component-info lookup APIs that unconditionally return third-party component data.

###### Query / Resolve (11 methods, 11 reach safeMode, 0 C1 violations)

| #    | Method                            | Return Type         | Reaches safeMode() |
| ---- | --------------------------------- | ------------------- | ------------------ |
| 1    | `getLaunchIntentSenderForPackage` | `IntentSender`      | ✓                  |
| 2    | `canForwardTo`                    | `boolean`           | ✓                  |
| 3    | `getHomeActivities`               | `ComponentName`     | ✓                  |
| 4    | `getInstantAppResolverComponent`  | `ComponentName`     | ✓                  |
| 5    | `queryIntentActivities`           | `ParceledListSlice` | ✓                  |
| 6    | `queryIntentActivityOptions`      | `ParceledListSlice` | ✓                  |
| 7    | `queryIntentContentProviders`     | `ParceledListSlice` | ✓                  |
| 8    | `queryIntentReceivers`            | `ParceledListSlice` | ✓                  |
| 9    | `queryIntentServices`             | `ParceledListSlice` | ✓                  |
| 10   | `resolveIntent`                   | `ResolveInfo`       | ✓                  |
| 11   | `resolveService`                  | `ResolveInfo`       | ✓                  |

###### Preferred Activity (11 methods, 10 reach safeMode, 0 C1 violations)

| #    | Method                                      | Return Type   | Reaches safeMode() |
| ---- | ------------------------------------------- | ------------- | ------------------ |
| 1    | `getLastChosenActivity`                     | `ResolveInfo` | ✓                  |
| 2    | `addPersistentPreferredActivity`            | `void`        | ✓                  |
| 3    | `addPreferredActivity`                      | `void`        | ✓                  |
| 4    | `clearPackagePersistentPreferredActivities` | `void`        | ✓                  |
| 5    | `clearPersistentPreferredActivity`          | `void`        | ✓                  |
| 6    | `findPersistentPreferredActivity`           | `ResolveInfo` | ✓                  |
| 7    | `getPreferredActivityBackup`                | `byte[]`      | ✗                  |
| 8    | `replacePreferredActivity`                  | `void`        | ✓                  |
| 9    | `resetApplicationPreferences`               | `void`        | ✓                  |
| 10   | `setHomeActivity`                           | `void`        | ✓                  |
| 11   | `setLastChosenActivity`                     | `void`        | ✓                  |

###### Component Info Lookup (8 methods, 0 reach safeMode, 8 C1 violations)

| #    | Method                    | Return Type       | Reaches safeMode() |
| ---- | ------------------------- | ----------------- | ------------------ |
| 1    | `getActivityInfo`         | `ActivityInfo`    | ✗                  |
| 2    | `getApplicationInfo`      | `ApplicationInfo` | ✗                  |
| 3    | `getPackageInfo`          | `PackageInfo`     | ✗                  |
| 4    | `getPackageInfoVersioned` | `PackageInfo`     | ✗                  |
| 5    | `getProviderInfo`         | `ProviderInfo`    | ✗                  |
| 6    | `getReceiverInfo`         | `ActivityInfo`    | ✗                  |
| 7    | `getServiceInfo`          | `ServiceInfo`     | ✗                  |
| 8    | `resolveContentProvider`  | `ProviderInfo`    | ✗                  |

###### Other (193 methods, 3 reach safeMode)

| #    | Method                                      | Return Type                       | Reaches safeMode() |
| ---- | ------------------------------------------- | --------------------------------- | ------------------ |
| 1    | `checkPackageStartable`                     | `void`                            | ✗                  |
| 2    | `clearApplicationProfileData`               | `void`                            | ✗                  |
| 3    | `clearApplicationUserData`                  | `void`                            | ✗                  |
| 4    | `clearCrossProfileIntentFilters`            | `void`                            | ✗                  |
| 5    | `deleteApplicationCacheFiles`               | `void`                            | ✗                  |
| 6    | `deleteApplicationCacheFilesAsUser`         | `void`                            | ✗                  |
| 7    | `dump`                                      | `void`                            | ✗                  |
| 8    | `enterSafeMode`                             | `void`                            | ✗                  |
| 9    | `extendVerificationTimeout`                 | `void`                            | ✗                  |
| 10   | `flushPackageRestrictionsAsUser`            | `void`                            | ✗                  |
| 11   | `freeStorage`                               | `void`                            | ✗                  |
| 12   | `freeStorageAndNotify`                      | `void`                            | ✗                  |
| 13   | `getAppMetadataFd`                          | `android.os.ParcelFileDescriptor` | ✗                  |
| 14   | `getAppMetadataSource`                      | `int`                             | ✗                  |
| 15   | `getArchivedAppIcon`                        | `Bitmap`                          | ✗                  |
| 16   | `getArchivedPackage`                        | `ArchivedPackageParcel`           | ✗                  |
| 17   | `getChangedPackages`                        | `ChangedPackages`                 | ✗                  |
| 18   | `getDomainVerificationAgent`                | `ComponentName`                   | ✗                  |
| 19   | `getDomainVerificationBackup`               | `byte[]`                          | ✗                  |
| 20   | `getHoldLockToken`                          | `android.os.IBinder`              | ✗                  |
| 21   | `getInitialNonStoppedSystemPackages`        | `List`                            | ✗                  |
| 22   | `getInstantAppAndroidId`                    | `String`                          | ✗                  |
| 23   | `getInstantAppCookie`                       | `byte[]`                          | ✗                  |
| 24   | `getInstantAppIcon`                         | `Bitmap`                          | ✓                  |
| 25   | `getInstantApps`                            | `ParceledListSlice`               | ✓                  |
| 26   | `getMimeGroup`                              | `List`                            | ✗                  |
| 27   | `getMoveStatus`                             | `int`                             | ✗                  |
| 28   | `getPermissionControllerPackageName`        | `String`                          | ✗                  |
| 29   | `getRuntimePermissionsVersion`              | `int`                             | ✗                  |
| 30   | `getSplashScreenTheme`                      | `String`                          | ✗                  |
| 31   | `getSuspendedPackageAppExtras`              | `android.os.Bundle`               | ✗                  |
| 32   | `getSuspendingPackage`                      | `String`                          | ✗                  |
| 33   | `getSystemAvailableFeatures`                | `ParceledListSlice`               | ✗                  |
| 34   | `getUnsuspendablePackagesForUser`           | `String[]`                        | ✗                  |
| 35   | `getUserMinAspectRatio`                     | `int`                             | ✗                  |
| 36   | `getVerifierDeviceIdentity`                 | `VerifierDeviceIdentity`          | ✗                  |
| 37   | `holdLock`                                  | `void`                            | ✗                  |
| 38   | `installExistingPackageAsUser`              | `int`                             | ✗                  |
| 39   | `isAppArchivable`                           | `boolean`                         | ✗                  |
| 40   | `isAutoRevokeWhitelisted`                   | `boolean`                         | ✗                  |
| 41   | `isPackageStateProtected`                   | `boolean`                         | ✗                  |
| 42   | `isProtectedBroadcast`                      | `boolean`                         | ✗                  |
| 43   | `logAppProcessStartIfNeeded`                | `void`                            | ✗                  |
| 44   | `makeProviderVisible`                       | `void`                            | ✗                  |
| 45   | `makeUidVisible`                            | `void`                            | ✗                  |
| 46   | `movePackage`                               | `int`                             | ✗                  |
| 47   | `movePrimaryStorage`                        | `int`                             | ✗                  |
| 48   | `notifyDexLoad`                             | `void`                            | ✗                  |
| 49   | `notifyPackageUse`                          | `void`                            | ✗                  |
| 50   | `notifyPackagesReplacedReceived`            | `void`                            | ✗                  |
| 51   | `onShellCommand`                            | `void`                            | ✗                  |
| 52   | `onTransact`                                | `boolean`                         | ✓                  |
| 53   | `overrideLabelAndIcon`                      | `void`                            | ✗                  |
| 54   | `queryProperty`                             | `ParceledListSlice`               | ✗                  |
| 55   | `registerDexModule`                         | `void`                            | ✗                  |
| 56   | `registerMoveCallback`                      | `void`                            | ✗                  |
| 57   | `registerPackageMonitorCallback`            | `void`                            | ✗                  |
| 58   | `relinquishUpdateOwnership`                 | `void`                            | ✗                  |
| 59   | `removeCrossProfileIntentFilter`            | `boolean`                         | ✗                  |
| 60   | `requestPackageChecksums`                   | `void`                            | ✗                  |
| 61   | `restoreDomainVerification`                 | `void`                            | ✗                  |
| 62   | `restoreLabelAndIcon`                       | `void`                            | ✗                  |
| 63   | `sendDeviceCustomizationReadyBroadcast`     | `void`                            | ✗                  |
| 64   | `setApplicationCategoryHint`                | `void`                            | ✗                  |
| 65   | `setApplicationEnabledSetting`              | `void`                            | ✗                  |
| 66   | `setApplicationHiddenSettingAsUser`         | `boolean`                         | ✗                  |
| 67   | `setBlockUninstallForUser`                  | `boolean`                         | ✗                  |
| 68   | `setComponentEnabledSetting`                | `void`                            | ✗                  |
| 69   | `setComponentEnabledSettings`               | `void`                            | ✗                  |
| 70   | `setDistractingPackageRestrictionsAsUser`   | `String[]`                        | ✗                  |
| 71   | `setHarmfulAppWarning`                      | `void`                            | ✗                  |
| 72   | `setInstallLocation`                        | `boolean`                         | ✗                  |
| 73   | `setInstallerPackageName`                   | `void`                            | ✗                  |
| 74   | `setInstantAppCookie`                       | `boolean`                         | ✗                  |
| 75   | `setKeepUninstalledPackages`                | `void`                            | ✗                  |
| 76   | `setMimeGroup`                              | `void`                            | ✗                  |
| 77   | `setPackageStoppedState`                    | `void`                            | ✗                  |
| 78   | `setPackagesSuspendedAsUser`                | `String[]`                        | ✗                  |
| 79   | `setRequiredForSystemUser`                  | `boolean`                         | ✗                  |
| 80   | `setRuntimePermissionsVersion`              | `void`                            | ✗                  |
| 81   | `setSplashScreenTheme`                      | `void`                            | ✗                  |
| 82   | `setUpdateAvailable`                        | `void`                            | ✗                  |
| 83   | `setUserMinAspectRatio`                     | `void`                            | ✗                  |
| 84   | `unregisterMoveCallback`                    | `void`                            | ✗                  |
| 85   | `unregisterPackageMonitorCallback`          | `void`                            | ✗                  |
| 86   | `verifyPendingInstall`                      | `void`                            | ✗                  |
| 87   | `waitForHandler`                            | `boolean`                         | ✗                  |
| 88   | `activitySupportsIntentAsUser`              | `boolean`                         | ✗                  |
| 89   | `addCrossProfileIntentFilter`               | `void`                            | ✗                  |
| 90   | `addPermission`                             | `boolean`                         | ✗                  |
| 91   | `addPermissionAsync`                        | `boolean`                         | ✗                  |
| 92   | `canPackageQuery`                           | `boolean[]`                       | ✗                  |
| 93   | `canRequestPackageInstalls`                 | `boolean`                         | ✗                  |
| 94   | `canonicalToCurrentPackageNames`            | `String[]`                        | ✗                  |
| 95   | `checkPermission`                           | `int`                             | ✗                  |
| 96   | `checkSignatures`                           | `int`                             | ✗                  |
| 97   | `checkUidPermission`                        | `int`                             | ✗                  |
| 98   | `checkUidSignatures`                        | `int`                             | ✗                  |
| 99   | `clearPackagePreferredActivities`           | `void`                            | ✗                  |
| 100  | `currentToCanonicalPackageNames`            | `String[]`                        | ✗                  |
| 101  | `deleteExistingPackageAsUser`               | `void`                            | ✗                  |
| 102  | `deletePackageAsUser`                       | `void`                            | ✗                  |
| 103  | `deletePackageVersioned`                    | `void`                            | ✗                  |
| 104  | `deletePreloadsFileCache`                   | `void`                            | ✗                  |
| 105  | `finishPackageInstall`                      | `void`                            | ✗                  |
| 106  | `getAllIntentFilters`                       | `ParceledListSlice`               | ✗                  |
| 107  | `getAllPackages`                            | `List`                            | ✗                  |
| 108  | `getAppOpPermissionPackages`                | `String[]`                        | ✗                  |
| 109  | `getAppPredictionServicePackageName`        | `String`                          | ✗                  |
| 110  | `getApplicationEnabledSetting`              | `int`                             | ✗                  |
| 111  | `getApplicationHiddenSettingAsUser`         | `boolean`                         | ✗                  |
| 112  | `getArtManager`                             | `dex.IArtManager`                 | ✗                  |
| 113  | `getAttentionServicePackageName`            | `String`                          | ✗                  |
| 114  | `getBlockUninstallForUser`                  | `boolean`                         | ✗                  |
| 115  | `getComponentEnabledSetting`                | `int`                             | ✗                  |
| 116  | `getDeclaredSharedLibraries`                | `ParceledListSlice`               | ✗                  |
| 117  | `getDefaultAppsBackup`                      | `byte[]`                          | ✗                  |
| 118  | `getDefaultTextClassifierPackageName`       | `String`                          | ✗                  |
| 119  | `getFlagsForUid`                            | `int`                             | ✗                  |
| 120  | `getHarmfulAppWarning`                      | `CharSequence`                    | ✗                  |
| 121  | `getIncidentReportApproverPackageName`      | `String`                          | ✗                  |
| 122  | `getInstallLocation`                        | `int`                             | ✗                  |
| 123  | `getInstallReason`                          | `int`                             | ✗                  |
| 124  | `getInstallSourceInfo`                      | `InstallSourceInfo`               | ✗                  |
| 125  | `getInstalledApplications`                  | `ParceledListSlice`               | ✗                  |
| 126  | `getInstalledModules`                       | `List`                            | ✗                  |
| 127  | `getInstalledPackages`                      | `ParceledListSlice`               | ✗                  |
| 128  | `getInstallerPackageName`                   | `String`                          | ✗                  |
| 129  | `getInstantAppInstallerComponent`           | `ComponentName`                   | ✗                  |
| 130  | `getInstantAppResolverSettingsComponent`    | `ComponentName`                   | ✗                  |
| 131  | `getInstrumentationInfoAsUser`              | `InstrumentationInfo`             | ✗                  |
| 132  | `getIntentFilterVerifications`              | `ParceledListSlice`               | ✗                  |
| 133  | `getIntentVerificationStatus`               | `int`                             | ✗                  |
| 134  | `getKeySetByAlias`                          | `KeySet`                          | ✗                  |
| 135  | `getModuleInfo`                             | `ModuleInfo`                      | ✗                  |
| 136  | `getNameForUid`                             | `String`                          | ✗                  |
| 137  | `getNamesForUids`                           | `String[]`                        | ✗                  |
| 138  | `getPackageGids`                            | `int[]`                           | ✗                  |
| 139  | `getPackageInstaller`                       | `IPackageInstaller`               | ✗                  |
| 140  | `getPackageSizeInfo`                        | `void`                            | ✗                  |
| 141  | `getPackageUid`                             | `int`                             | ✗                  |
| 142  | `getPackagesForUid`                         | `String[]`                        | ✗                  |
| 143  | `getPackagesHoldingPermissions`             | `ParceledListSlice`               | ✗                  |
| 144  | `getPermissionGroupInfo`                    | `PermissionGroupInfo`             | ✗                  |
| 145  | `getPersistentApplications`                 | `ParceledListSlice`               | ✗                  |
| 146  | `getPreferredActivities`                    | `int`                             | ✗                  |
| 147  | `getPrivateFlagsForUid`                     | `int`                             | ✗                  |
| 148  | `getPropertyAsUser`                         | `PackageManager$Property`         | ✗                  |
| 149  | `getRotationResolverPackageName`            | `String`                          | ✗                  |
| 150  | `getSdkSandboxPackageName`                  | `String`                          | ✗                  |
| 151  | `getServicesSystemSharedLibraryPackageName` | `String`                          | ✗                  |
| 152  | `getSetupWizardPackageName`                 | `String`                          | ✗                  |
| 153  | `getSharedLibraries`                        | `ParceledListSlice`               | ✗                  |
| 154  | `getSharedSystemSharedLibraryPackageName`   | `String`                          | ✗                  |
| 155  | `getSigningKeySet`                          | `KeySet`                          | ✗                  |
| 156  | `getSystemCaptionsServicePackageName`       | `String`                          | ✗                  |
| 157  | `getSystemSharedLibraryNames`               | `String[]`                        | ✗                  |
| 158  | `getSystemSharedLibraryNamesAndPaths`       | `Map`                             | ✗                  |
| 159  | `getSystemTextClassifierPackageName`        | `String`                          | ✗                  |
| 160  | `getTargetSdkVersion`                       | `int`                             | ✗                  |
| 161  | `getUidForSharedUser`                       | `int`                             | ✗                  |
| 162  | `getWellbeingPackageName`                   | `String`                          | ✗                  |
| 163  | `grantRuntimePermission`                    | `void`                            | ✗                  |
| 164  | `hasSigningCertificate`                     | `boolean`                         | ✗                  |
| 165  | `hasSystemFeature`                          | `boolean`                         | ✗                  |
| 166  | `hasSystemUidErrors`                        | `boolean`                         | ✗                  |
| 167  | `hasUidSigningCertificate`                  | `boolean`                         | ✗                  |
| 168  | `isDeviceUpgrading`                         | `boolean`                         | ✗                  |
| 169  | `isFirstBoot`                               | `boolean`                         | ✗                  |
| 170  | `isInstantApp`                              | `boolean`                         | ✗                  |
| 171  | `isPackageAvailable`                        | `boolean`                         | ✗                  |
| 172  | `isPackageDeviceAdminOnAnyUser`             | `boolean`                         | ✗                  |
| 173  | `isPackageQuarantinedForUser`               | `boolean`                         | ✗                  |
| 174  | `isPackageSignedByKeySet`                   | `boolean`                         | ✗                  |
| 175  | `isPackageSignedByKeySetExactly`            | `boolean`                         | ✗                  |
| 176  | `isPackageStoppedForUser`                   | `boolean`                         | ✗                  |
| 177  | `isPackageSuspendedForUser`                 | `boolean`                         | ✗                  |
| 178  | `isSafeMode`                                | `boolean`                         | ✗                  |
| 179  | `isStorageLow`                              | `boolean`                         | ✗                  |
| 180  | `isUidPrivileged`                           | `boolean`                         | ✗                  |
| 181  | `performDexOptMode`                         | `boolean`                         | ✗                  |
| 182  | `performDexOptSecondary`                    | `boolean`                         | ✗                  |
| 183  | `queryContentProviders`                     | `ParceledListSlice`               | ✗                  |
| 184  | `queryInstrumentationAsUser`                | `ParceledListSlice`               | ✗                  |
| 185  | `querySyncProviders`                        | `void`                            | ✗                  |
| 186  | `removePermission`                          | `void`                            | ✗                  |
| 187  | `restoreDefaultApps`                        | `void`                            | ✗                  |
| 188  | `restorePreferredActivities`                | `void`                            | ✗                  |
| 189  | `setSystemAppHiddenUntilInstalled`          | `void`                            | ✗                  |
| 190  | `setSystemAppInstallState`                  | `boolean`                         | ✗                  |
| 191  | `snapshot`                                  | `com.android.server.pm.Computer`  | ✗                  |
| 192  | `updateIntentVerificationStatus`            | `boolean`                         | ✗                  |
| 193  | `verifyIntentFilter`                        | `void`                            | ✗                  |

###### Summary

| Category              | Total   | Reaches safeMode() | C1 viol. |
| --------------------- | ------- | ------------------ | -------- |
| Query / Resolve       | 11      | 11                 | 0        |
| Preferred Activity    | 11      | 10                 | 0        |
| Component Info Lookup | 8       | 0                  | **8**    |
| Other                 | 193     | 3                  | 0        |
| **Total**             | **223** | **24**             | **8**    |



**C2_SafeModeInstrumentation** — Instrumentation patches for the four `queryIntent*Internal` convergence points in AOSP Android 15. Captures the `safeMode` state, effective flags, timestamps, and stack traces during Safe Mode boot. Reveals a deterministic 701 ms window with 12 unprotected queries from 7 system services.

T0 (first query): 04-15 20:27:10.041
First safeMode=true snapshot: +797ms
First safeMode=true query: +701ms
Total queries: 412

###### Summary Statistics (Table c2-stats)

| Metric                                  | Value  |
| --------------------------------------- | ------ |
| Total queryIntent*Internal invocations  | 412    |
| Before safeMode activation (30):        |        |
| Self-protected (MSO = true)             | 18     |
| **Unprotected (MSO = false)**           | **12** |
| After safeMode activation (382):        |        |
| Protected (MSO = true)                  | 382    |
| Unprotected queries with GET_META_DATA  | 6      |
| Distinct calling services (unprotected) | 7      |
| Race window                             | 797 ms |

###### C2 Violations (Table c2-violations)

| #    | Δt (ms) | Caller  | Intent                                              | MSO  | META |
| ---- | ------- | ------- | --------------------------------------------------- | ---- | ---- |
| 1    | +63     | PMS     | `RESOLVE_INSTANT_APP_PACKAGE`                       | ✗    | ✗    |
| 2    | +279    | RoleSvc | `app.role.RoleControllerService`                    | ✗    | ✗    |
| 3    | +370    | IMMS    | `view.InputMethod`                                  | ✗    | ✓    |
| 4    | +374    | A11ySvc | `accessibilityservice.AccessibilityService`         | ✗    | ✗    |
| 5    | +472    | WifiSvc | `settings.SETTINGS`                                 | ✗    | ✗    |
| 6    | +530    | NMS     | `service.notification.NotificationAssistantService` | ✗    | ✗    |
| 7    | +533    | NMS     | `service.notification.ConditionProviderService`     | ✗    | ✓    |
| 8    | +615    | CDM     | `permission.PermissionControllerService`            | ✗    | ✗    |

Total unique C2 violations: 8
  With GET_META_DATA: 2

###### Unprotected Callers Breakdown

| Caller  | Queries | With META_DATA | Distinct Intents                                             |
| ------- | ------- | -------------- | ------------------------------------------------------------ |
| NMS     | 6       | 5              | service.notification.ConditionProviderService, service.notification.Notificat... |
| PMS     | 1       | 0              | RESOLVE_INSTANT_APP_PACKAGE                                  |
| RoleSvc | 1       | 0              | app.role.RoleControllerService                               |
| IMMS    | 1       | 1              | view.InputMethod                                             |
| A11ySvc | 1       | 0              | accessibilityservice.AccessibilityService                    |
| WifiSvc | 1       | 0              | settings.SETTINGS                                            |
| CDM     | 1       | 0              | permission.PermissionControllerService                       |

### ResourceBreaker

**appshark** — Static taint analysis engine built on AppShark, extended with entry-model generalisation for system service lifecycles, structural taint propagation across container boundaries, and cross-component resolution via unified class hierarchy. Two pre-built JARs are provided for C1 and C2 source configurations respectively.

###### C1: Collect from `ResourceBreaker/ResourceBreaker/appshark/c1-violate/out/results.json`

| EntryPoint / Service             |  Count |
| -------------------------------- | -----: |
| `VoiceInteractionManagerService` |      7 |
| `AutofillManagerService`         |      4 |
| `AppWidgetService`               |      1 |
| `CredentialManagerService`       |      1 |
| `ContentCaptureManagerService`   |      1 |
| `ShortcutService`                |      1 |
| `GameManagerService`             |      1 |
| `DevicePolicyManagerService`     |      1 |
| **Total**                        | **17** |

###### C2: Collect from `ResourceBreaker/ResourceBreaker/appshark/c2-violate/out/results.json`

| EntryPoint / Service        | Count |
| --------------------------- | ----: |
| `InputMethodManagerService` |     1 |
| **Total**                   | **1** |

**OOM_SOF_Finder** — Constraint solver and call-graph analyser for field-level crash resource construction. Performs symbolic execution over 010 Editor binary templates to maximise Java-layer allocations under native-layer structural constraints (OOM), and detects recursive call cycles in VectorDrawable parsing paths (StackOverflow).

```
==== Prediction Results ====
UTF-16 max string length: 2147483647 (0x7fffffff)
UTF-8 max string length: 32767 (0x7fff)
[!] Virtual/polymorphic pointer recursion (stack overflow risk):
  Group::draw -> child->draw
  Group::dump -> mChildren[i]->dump
  Group::syncProperties -> child->syncProperties
```



**fuzzer** — AFL++ fuzzing harnesses targeting Skia's DNG decoder and the framework's AXML parser, cross-compiled for PC-side execution with ASan and UBSan. Includes build guides, diff files against `android-15.0.0_r1`, and crash site results.

[*] Progress: 10527/10527

[*] Fuzzing completed, generating report...

###### Crash Location Aggregation Table （Skia）

| Crash Location                                               | Sample Files Triggering the Crash (up to 3 per location)     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `ASan heap-buffer-overflow in RefBaselineABCDtoRGB`          | `/home/***/report/heap_overflow_RefBaselineABCDtoRGB.png`    |
| `ASan stack-buffer-overflow in dng_interleave_task::Start`   | `/home/***/report/stack_dng_interleave_task_Start.png`, `all_crashes/id:000006,sig:06,src:019839,time:483971080,execs:35947315,op:quick,pos:31`, `all_crashes/id:000006,sig:06,src:019965,time:495362512,execs:37610082,op:quick,pos:31,val:+3` ... *(807 in total)* |
| `external/dng_sdk/source/dng_linearization_info.cpp:746:9`   | `/home/***/report/461782921.png`                             |
| `external/dng_sdk/source/dng_lossless_jpeg_shared.cpp:1935:48` | `/home/***/report/462390721-more.png`, `all_crashes/id:000003,sig:06,src:019813,time:472338971,execs:33575777,op:quick,pos:14456,val:+2`, `all_crashes/id:000004,sig:06,src:019696,time:481007691,execs:36147048,op:quick,pos:14457` ... *(380 in total)* |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:275:22`        | `/home/***/report/dng_misc_opcodes_275.png`, `all_crashes/id:000000,sig:06,src:005343,time:457680099,execs:34522245,op:quick,pos:13086,val:+6`, `all_crashes/id:000000,sig:06,src:005384,time:466630552,execs:33365837,op:quick,pos:13002` ... *(2626 in total)* |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:276:22`        | `/home/***/report/456422800.png`, `/home/***/report/dng_opcode_MapTable_ProcessArea.png`, `/home/***/report/dng_misc_opcodes_276.png` ... *(1784 in total)* |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:565:32`        | `/home/***/report/dng_misc_opcodes_565.png`                  |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:792:32`        | `/home/***/report/449448549.webp`, `all_crashes/id:000004,sig:06,src:013834,time:462414081,execs:35774773,op:quick,pos:13022`, `all_crashes/id:000020,sig:06,src:013872,time:589041270,execs:43213294,op:quick,pos:13022,val:+2` ... *(208 in total)* |
| `external/dng_sdk/source/dng_misc_opcodes.cpp:827:26`        | `all_crashes/id:000142,sig:06,src:015163,time:1799832692,execs:154010339,op:havoc,rep:4`, `all_crashes/id:000158,sig:06,src:025220,time:2194699412,execs:198120208,op:havoc,rep:1`, `all_crashes/id:000164,sig:06,src:025093,time:2434371274,execs:223015670,op:havoc,rep:2` ... *(6 in total)* |
| `external/dng_sdk/source/dng_read_image.cpp:2455:43`         | `/home/***/report/467965812.png`, `all_crashes/id:000004,sig:06,src:017771,time:469342506,execs:33699022,op:quick,pos:165,val:+2`, `all_crashes/id:000004,sig:06,src:017828,time:470877083,execs:35706681,op:quick,pos:165` ... *(124 in total)* |
| `external/dng_sdk/source/dng_read_image.cpp:3673:43`         | `/home/***/report/467888081.png`, `all_crashes/id:000000,sig:06,src:019378,time:467186175,execs:36758508,op:havoc,rep:12`, `all_crashes/id:000003,sig:06,src:019772,time:470244671,execs:35203094,op:quick,pos:156` ... *(370 in total)* |
| `external/dng_sdk/source/dng_reference.cpp:2897:18`          | `/home/***/report/456380811.png`, `/home/***/report/crash-dng_reference.png` |
| `external/dng_sdk/source/dng_stream.cpp:1050:10`             | `/home/***/report/470580610.png`, `all_crashes/id:000064,sig:06,src:023174,time:979346807,execs:68203643,op:havoc,rep:3`, `all_crashes/id:000068,sig:06,src:015077,time:1045860920,execs:74741577,op:havoc,rep:2` ... *(100 in total)* |
| `external/dng_sdk/source/dng_stream.cpp:1051:10`             | `/home/***/report/470582070.png`, `all_crashes/id:000019,sig:06,src:014236,time:561402367,execs:38732928,op:havoc,rep:4`, `all_crashes/id:000056,sig:06,src:009824,time:907626133,execs:60327938,op:havoc,rep:2` ... *(63 in total)* |
| `external/dng_sdk/source/dng_string.cpp:951:28`              | `/home/***/report/470574979.png`, `all_crashes/id:000001,sig:06,src:003640,time:458071993,execs:33478365,op:havoc,rep:15`, `all_crashes/id:000002,sig:06,src:003640,time:458072164,execs:33478387,op:havoc,rep:15` ... *(2071 in total)* |
| `external/dng_sdk/source/dng_utils.cpp:1456:59`              | `/home/***/report/470577223.png`, `all_crashes/id:000048,sig:06,src:022247,time:848690792,execs:56985078,op:quick,pos:177,val:+3`, `all_crashes/id:000052,sig:06,src:022327,time:915212331,execs:63063479,op:havoc,rep:8` ... *(1983 in total)* |

No crash samples were found in androidfw. 

## Target

- AOSP `android-15.0.0_r1` (API 35)
- Security patch: `android-security-15.0.0_r12` (Dec 2025)
- Validated on Google Pixel 6 and Pixel 6a
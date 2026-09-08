 All Method Call CFG edges collected.

## Safe Mode Coverage Audit Results

### Query / Resolve (11 methods, 11 reach safeMode, 0 C1 violations)

| # | Method | Return Type | Reaches safeMode() |
|---|--------|-------------|-------------------|
| 1 | `getLaunchIntentSenderForPackage` | `IntentSender` | ✓ |
| 2 | `canForwardTo` | `boolean` | ✓ |
| 3 | `getHomeActivities` | `ComponentName` | ✓ |
| 4 | `getInstantAppResolverComponent` | `ComponentName` | ✓ |
| 5 | `queryIntentActivities` | `ParceledListSlice` | ✓ |
| 6 | `queryIntentActivityOptions` | `ParceledListSlice` | ✓ |
| 7 | `queryIntentContentProviders` | `ParceledListSlice` | ✓ |
| 8 | `queryIntentReceivers` | `ParceledListSlice` | ✓ |
| 9 | `queryIntentServices` | `ParceledListSlice` | ✓ |
| 10 | `resolveIntent` | `ResolveInfo` | ✓ |
| 11 | `resolveService` | `ResolveInfo` | ✓ |

### Preferred Activity (11 methods, 10 reach safeMode, 0 C1 violations)

| # | Method | Return Type | Reaches safeMode() |
|---|--------|-------------|-------------------|
| 1 | `getLastChosenActivity` | `ResolveInfo` | ✓ |
| 2 | `addPersistentPreferredActivity` | `void` | ✓ |
| 3 | `addPreferredActivity` | `void` | ✓ |
| 4 | `clearPackagePersistentPreferredActivities` | `void` | ✓ |
| 5 | `clearPersistentPreferredActivity` | `void` | ✓ |
| 6 | `findPersistentPreferredActivity` | `ResolveInfo` | ✓ |
| 7 | `getPreferredActivityBackup` | `byte[]` | ✗ |
| 8 | `replacePreferredActivity` | `void` | ✓ |
| 9 | `resetApplicationPreferences` | `void` | ✓ |
| 10 | `setHomeActivity` | `void` | ✓ |
| 11 | `setLastChosenActivity` | `void` | ✓ |

### Component Info Lookup (8 methods, 0 reach safeMode, 8 C1 violations)

| # | Method | Return Type | Reaches safeMode() |
|---|--------|-------------|-------------------|
| 1 | `getActivityInfo` | `ActivityInfo` | ✗ |
| 2 | `getApplicationInfo` | `ApplicationInfo` | ✗ |
| 3 | `getPackageInfo` | `PackageInfo` | ✗ |
| 4 | `getPackageInfoVersioned` | `PackageInfo` | ✗ |
| 5 | `getProviderInfo` | `ProviderInfo` | ✗ |
| 6 | `getReceiverInfo` | `ActivityInfo` | ✗ |
| 7 | `getServiceInfo` | `ServiceInfo` | ✗ |
| 8 | `resolveContentProvider` | `ProviderInfo` | ✗ |

### Other (193 methods, 3 reach safeMode, 0 C1 violations)

| # | Method | Return Type | Reaches safeMode() |
|---|--------|-------------|-------------------|
| 1 | `checkPackageStartable` | `void` | ✗ |
| 2 | `clearApplicationProfileData` | `void` | ✗ |
| 3 | `clearApplicationUserData` | `void` | ✗ |
| 4 | `clearCrossProfileIntentFilters` | `void` | ✗ |
| 5 | `deleteApplicationCacheFiles` | `void` | ✗ |
| 6 | `deleteApplicationCacheFilesAsUser` | `void` | ✗ |
| 7 | `dump` | `void` | ✗ |
| 8 | `enterSafeMode` | `void` | ✗ |
| 9 | `extendVerificationTimeout` | `void` | ✗ |
| 10 | `flushPackageRestrictionsAsUser` | `void` | ✗ |
| 11 | `freeStorage` | `void` | ✗ |
| 12 | `freeStorageAndNotify` | `void` | ✗ |
| 13 | `getAppMetadataFd` | `android.os.ParcelFileDescriptor` | ✗ |
| 14 | `getAppMetadataSource` | `int` | ✗ |
| 15 | `getArchivedAppIcon` | `Bitmap` | ✗ |
| 16 | `getArchivedPackage` | `ArchivedPackageParcel` | ✗ |
| 17 | `getChangedPackages` | `ChangedPackages` | ✗ |
| 18 | `getDomainVerificationAgent` | `ComponentName` | ✗ |
| 19 | `getDomainVerificationBackup` | `byte[]` | ✗ |
| 20 | `getHoldLockToken` | `android.os.IBinder` | ✗ |
| 21 | `getInitialNonStoppedSystemPackages` | `List` | ✗ |
| 22 | `getInstantAppAndroidId` | `String` | ✗ |
| 23 | `getInstantAppCookie` | `byte[]` | ✗ |
| 24 | `getInstantAppIcon` | `Bitmap` | ✓ |
| 25 | `getInstantApps` | `ParceledListSlice` | ✓ |
| 26 | `getMimeGroup` | `List` | ✗ |
| 27 | `getMoveStatus` | `int` | ✗ |
| 28 | `getPermissionControllerPackageName` | `String` | ✗ |
| 29 | `getRuntimePermissionsVersion` | `int` | ✗ |
| 30 | `getSplashScreenTheme` | `String` | ✗ |
| 31 | `getSuspendedPackageAppExtras` | `android.os.Bundle` | ✗ |
| 32 | `getSuspendingPackage` | `String` | ✗ |
| 33 | `getSystemAvailableFeatures` | `ParceledListSlice` | ✗ |
| 34 | `getUnsuspendablePackagesForUser` | `String[]` | ✗ |
| 35 | `getUserMinAspectRatio` | `int` | ✗ |
| 36 | `getVerifierDeviceIdentity` | `VerifierDeviceIdentity` | ✗ |
| 37 | `holdLock` | `void` | ✗ |
| 38 | `installExistingPackageAsUser` | `int` | ✗ |
| 39 | `isAppArchivable` | `boolean` | ✗ |
| 40 | `isAutoRevokeWhitelisted` | `boolean` | ✗ |
| 41 | `isPackageStateProtected` | `boolean` | ✗ |
| 42 | `isProtectedBroadcast` | `boolean` | ✗ |
| 43 | `logAppProcessStartIfNeeded` | `void` | ✗ |
| 44 | `makeProviderVisible` | `void` | ✗ |
| 45 | `makeUidVisible` | `void` | ✗ |
| 46 | `movePackage` | `int` | ✗ |
| 47 | `movePrimaryStorage` | `int` | ✗ |
| 48 | `notifyDexLoad` | `void` | ✗ |
| 49 | `notifyPackageUse` | `void` | ✗ |
| 50 | `notifyPackagesReplacedReceived` | `void` | ✗ |
| 51 | `onShellCommand` | `void` | ✗ |
| 52 | `onTransact` | `boolean` | ✓ |
| 53 | `overrideLabelAndIcon` | `void` | ✗ |
| 54 | `queryProperty` | `ParceledListSlice` | ✗ |
| 55 | `registerDexModule` | `void` | ✗ |
| 56 | `registerMoveCallback` | `void` | ✗ |
| 57 | `registerPackageMonitorCallback` | `void` | ✗ |
| 58 | `relinquishUpdateOwnership` | `void` | ✗ |
| 59 | `removeCrossProfileIntentFilter` | `boolean` | ✗ |
| 60 | `requestPackageChecksums` | `void` | ✗ |
| 61 | `restoreDomainVerification` | `void` | ✗ |
| 62 | `restoreLabelAndIcon` | `void` | ✗ |
| 63 | `sendDeviceCustomizationReadyBroadcast` | `void` | ✗ |
| 64 | `setApplicationCategoryHint` | `void` | ✗ |
| 65 | `setApplicationEnabledSetting` | `void` | ✗ |
| 66 | `setApplicationHiddenSettingAsUser` | `boolean` | ✗ |
| 67 | `setBlockUninstallForUser` | `boolean` | ✗ |
| 68 | `setComponentEnabledSetting` | `void` | ✗ |
| 69 | `setComponentEnabledSettings` | `void` | ✗ |
| 70 | `setDistractingPackageRestrictionsAsUser` | `String[]` | ✗ |
| 71 | `setHarmfulAppWarning` | `void` | ✗ |
| 72 | `setInstallLocation` | `boolean` | ✗ |
| 73 | `setInstallerPackageName` | `void` | ✗ |
| 74 | `setInstantAppCookie` | `boolean` | ✗ |
| 75 | `setKeepUninstalledPackages` | `void` | ✗ |
| 76 | `setMimeGroup` | `void` | ✗ |
| 77 | `setPackageStoppedState` | `void` | ✗ |
| 78 | `setPackagesSuspendedAsUser` | `String[]` | ✗ |
| 79 | `setRequiredForSystemUser` | `boolean` | ✗ |
| 80 | `setRuntimePermissionsVersion` | `void` | ✗ |
| 81 | `setSplashScreenTheme` | `void` | ✗ |
| 82 | `setUpdateAvailable` | `void` | ✗ |
| 83 | `setUserMinAspectRatio` | `void` | ✗ |
| 84 | `unregisterMoveCallback` | `void` | ✗ |
| 85 | `unregisterPackageMonitorCallback` | `void` | ✗ |
| 86 | `verifyPendingInstall` | `void` | ✗ |
| 87 | `waitForHandler` | `boolean` | ✗ |
| 88 | `activitySupportsIntentAsUser` | `boolean` | ✗ |
| 89 | `addCrossProfileIntentFilter` | `void` | ✗ |
| 90 | `addPermission` | `boolean` | ✗ |
| 91 | `addPermissionAsync` | `boolean` | ✗ |
| 92 | `canPackageQuery` | `boolean[]` | ✗ |
| 93 | `canRequestPackageInstalls` | `boolean` | ✗ |
| 94 | `canonicalToCurrentPackageNames` | `String[]` | ✗ |
| 95 | `checkPermission` | `int` | ✗ |
| 96 | `checkSignatures` | `int` | ✗ |
| 97 | `checkUidPermission` | `int` | ✗ |
| 98 | `checkUidSignatures` | `int` | ✗ |
| 99 | `clearPackagePreferredActivities` | `void` | ✗ |
| 100 | `currentToCanonicalPackageNames` | `String[]` | ✗ |
| 101 | `deleteExistingPackageAsUser` | `void` | ✗ |
| 102 | `deletePackageAsUser` | `void` | ✗ |
| 103 | `deletePackageVersioned` | `void` | ✗ |
| 104 | `deletePreloadsFileCache` | `void` | ✗ |
| 105 | `finishPackageInstall` | `void` | ✗ |
| 106 | `getAllIntentFilters` | `ParceledListSlice` | ✗ |
| 107 | `getAllPackages` | `List` | ✗ |
| 108 | `getAppOpPermissionPackages` | `String[]` | ✗ |
| 109 | `getAppPredictionServicePackageName` | `String` | ✗ |
| 110 | `getApplicationEnabledSetting` | `int` | ✗ |
| 111 | `getApplicationHiddenSettingAsUser` | `boolean` | ✗ |
| 112 | `getArtManager` | `dex.IArtManager` | ✗ |
| 113 | `getAttentionServicePackageName` | `String` | ✗ |
| 114 | `getBlockUninstallForUser` | `boolean` | ✗ |
| 115 | `getComponentEnabledSetting` | `int` | ✗ |
| 116 | `getDeclaredSharedLibraries` | `ParceledListSlice` | ✗ |
| 117 | `getDefaultAppsBackup` | `byte[]` | ✗ |
| 118 | `getDefaultTextClassifierPackageName` | `String` | ✗ |
| 119 | `getFlagsForUid` | `int` | ✗ |
| 120 | `getHarmfulAppWarning` | `CharSequence` | ✗ |
| 121 | `getIncidentReportApproverPackageName` | `String` | ✗ |
| 122 | `getInstallLocation` | `int` | ✗ |
| 123 | `getInstallReason` | `int` | ✗ |
| 124 | `getInstallSourceInfo` | `InstallSourceInfo` | ✗ |
| 125 | `getInstalledApplications` | `ParceledListSlice` | ✗ |
| 126 | `getInstalledModules` | `List` | ✗ |
| 127 | `getInstalledPackages` | `ParceledListSlice` | ✗ |
| 128 | `getInstallerPackageName` | `String` | ✗ |
| 129 | `getInstantAppInstallerComponent` | `ComponentName` | ✗ |
| 130 | `getInstantAppResolverSettingsComponent` | `ComponentName` | ✗ |
| 131 | `getInstrumentationInfoAsUser` | `InstrumentationInfo` | ✗ |
| 132 | `getIntentFilterVerifications` | `ParceledListSlice` | ✗ |
| 133 | `getIntentVerificationStatus` | `int` | ✗ |
| 134 | `getKeySetByAlias` | `KeySet` | ✗ |
| 135 | `getModuleInfo` | `ModuleInfo` | ✗ |
| 136 | `getNameForUid` | `String` | ✗ |
| 137 | `getNamesForUids` | `String[]` | ✗ |
| 138 | `getPackageGids` | `int[]` | ✗ |
| 139 | `getPackageInstaller` | `IPackageInstaller` | ✗ |
| 140 | `getPackageSizeInfo` | `void` | ✗ |
| 141 | `getPackageUid` | `int` | ✗ |
| 142 | `getPackagesForUid` | `String[]` | ✗ |
| 143 | `getPackagesHoldingPermissions` | `ParceledListSlice` | ✗ |
| 144 | `getPermissionGroupInfo` | `PermissionGroupInfo` | ✗ |
| 145 | `getPersistentApplications` | `ParceledListSlice` | ✗ |
| 146 | `getPreferredActivities` | `int` | ✗ |
| 147 | `getPrivateFlagsForUid` | `int` | ✗ |
| 148 | `getPropertyAsUser` | `PackageManager$Property` | ✗ |
| 149 | `getRotationResolverPackageName` | `String` | ✗ |
| 150 | `getSdkSandboxPackageName` | `String` | ✗ |
| 151 | `getServicesSystemSharedLibraryPackageName` | `String` | ✗ |
| 152 | `getSetupWizardPackageName` | `String` | ✗ |
| 153 | `getSharedLibraries` | `ParceledListSlice` | ✗ |
| 154 | `getSharedSystemSharedLibraryPackageName` | `String` | ✗ |
| 155 | `getSigningKeySet` | `KeySet` | ✗ |
| 156 | `getSystemCaptionsServicePackageName` | `String` | ✗ |
| 157 | `getSystemSharedLibraryNames` | `String[]` | ✗ |
| 158 | `getSystemSharedLibraryNamesAndPaths` | `Map` | ✗ |
| 159 | `getSystemTextClassifierPackageName` | `String` | ✗ |
| 160 | `getTargetSdkVersion` | `int` | ✗ |
| 161 | `getUidForSharedUser` | `int` | ✗ |
| 162 | `getWellbeingPackageName` | `String` | ✗ |
| 163 | `grantRuntimePermission` | `void` | ✗ |
| 164 | `hasSigningCertificate` | `boolean` | ✗ |
| 165 | `hasSystemFeature` | `boolean` | ✗ |
| 166 | `hasSystemUidErrors` | `boolean` | ✗ |
| 167 | `hasUidSigningCertificate` | `boolean` | ✗ |
| 168 | `isDeviceUpgrading` | `boolean` | ✗ |
| 169 | `isFirstBoot` | `boolean` | ✗ |
| 170 | `isInstantApp` | `boolean` | ✗ |
| 171 | `isPackageAvailable` | `boolean` | ✗ |
| 172 | `isPackageDeviceAdminOnAnyUser` | `boolean` | ✗ |
| 173 | `isPackageQuarantinedForUser` | `boolean` | ✗ |
| 174 | `isPackageSignedByKeySet` | `boolean` | ✗ |
| 175 | `isPackageSignedByKeySetExactly` | `boolean` | ✗ |
| 176 | `isPackageStoppedForUser` | `boolean` | ✗ |
| 177 | `isPackageSuspendedForUser` | `boolean` | ✗ |
| 178 | `isSafeMode` | `boolean` | ✗ |
| 179 | `isStorageLow` | `boolean` | ✗ |
| 180 | `isUidPrivileged` | `boolean` | ✗ |
| 181 | `performDexOptMode` | `boolean` | ✗ |
| 182 | `performDexOptSecondary` | `boolean` | ✗ |
| 183 | `queryContentProviders` | `ParceledListSlice` | ✗ |
| 184 | `queryInstrumentationAsUser` | `ParceledListSlice` | ✗ |
| 185 | `querySyncProviders` | `void` | ✗ |
| 186 | `removePermission` | `void` | ✗ |
| 187 | `restoreDefaultApps` | `void` | ✗ |
| 188 | `restorePreferredActivities` | `void` | ✗ |
| 189 | `setSystemAppHiddenUntilInstalled` | `void` | ✗ |
| 190 | `setSystemAppInstallState` | `boolean` | ✗ |
| 191 | `snapshot` | `com.android.server.pm.Computer` | ✗ |
| 192 | `updateIntentVerificationStatus` | `boolean` | ✗ |
| 193 | `verifyIntentFilter` | `void` | ✗ |

### Summary

| Category | Total | Reaches safeMode() | C1 viol. |
|----------|-------|--------------------|----------|
| Query / Resolve | 11 | 11 | 0 |
| Preferred Activity | 11 | 10 | 0 |
| Component Info Lookup | 8 | 0 | **8** |
| Other | 193 | 3 | 0 |
| **Total** | **223** | **24** | **8** |


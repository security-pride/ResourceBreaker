# ResourceBreaker Experimental Data

This document collects the experimental measurements and result summaries associated with **ResourceBreaker**. It is intentionally separate from the project overview in [`README.md`](README.md): the README explains the artifact, while this file records what was measured, where the data is stored, and how results should be interpreted.

The primary source is the accompanying paper, *One Resource to Break Them All: Exploiting Malformed Resources for Permanent Denial-of-Service in Android* (ACM CCS 2026). Repository-local files are identified explicitly below.

## 1. Scope and evidence levels

| Level | Meaning |
|---|---|
| **Confirmed** | The paper reports a concrete crash path and dynamic validation on the relevant Android build or device. |
| **Supported** | Static analysis, instrumentation, or a checked-in result strongly indicates the path, but the current artifact does not independently reproduce it on this checkout. |
| **Hypothesis** | A possible impact or boundary condition that requires additional target-specific testing. |

The aggregate numbers below are paper-reported results. They should not be read as a claim that every result can be reproduced without the original Android build, device image, native harness, and disclosure context.

## 2. Safe Mode audit data

### 2.1 C1: API-level coverage

The static audit enumerated **224 Package Manager Service Binder method signatures** and searched backward from `ComputerEngine.safeMode()`.

| API category | Total | Reaches `safeMode()` | C1 violations |
|---|---:|---:|---:|
| Query / Resolve | 12 | 11 | 0 |
| Preferred Activity | 11 | 10 | 0 |
| Component Info Lookup | 8 | 0 | 8 |
| Other | 193 | 3 | 0 |
| **Total** | **224** | **24** | **8** |

The eight direct component-information lookups reported as C1 violations are:

- `getActivityInfo`
- `getApplicationInfo`
- `getPackageInfo`
- `getPackageInfoVersioned`
- `getProviderInfo`
- `getReceiverInfo`
- `getServiceInfo`
- `resolveContentProvider`

Repository evidence:

- [`SafeModeAnlysis/C1_ReachabilityAnalysis/result.md`](SafeModeAnlysis/C1_ReachabilityAnalysis/result.md)
- [`SafeModeAnlysis/C1_ReachabilityAnalysis/call_hierarchy.dot`](SafeModeAnlysis/C1_ReachabilityAnalysis/call_hierarchy.dot)
- [`ResourceBreaker/appshark/c1-violate/results.md`](ResourceBreaker/appshark/c1-violate/results.md)
- [`ResourceBreaker/appshark/c1-violate/out/results.json`](ResourceBreaker/appshark/c1-violate/out/results.json)

The AppShark C1 output is a separate source-to-sink result set. Its checked-in summary reports **17 findings** distributed across eight service entry points; this count must not be conflated with the eight API-level Safe Mode violations above.

### 2.2 C2: boot-time coverage

The instrumentation campaign recorded **412** `queryIntent*Internal` invocations during a Safe Mode boot sequence.

| Metric | Value |
|---|---:|
| Total instrumented invocations | 412 |
| Before Safe Mode activation | 30 |
| - Self-protected (`MATCH_SYSTEM_ONLY` already present) | 18 |
| - Unprotected (`MATCH_SYSTEM_ONLY` absent) | 12 |
| After Safe Mode activation | 382 |
| - Protected | 382 |
| Unprotected queries carrying `GET_META_DATA` | 6 |
| Distinct unprotected calling services | 7 |
| Reported race window | approximately 701 ms |

The eight deduplicated unprotected caller/intent pairs were:

| # | Caller | Intent/action | Metadata requested |
|---:|---|---|:---:|
| 1 | PMS | `RESOLVE_INSTANT_APP_PACKAGE` | No |
| 2 | RoleSvc | `app.role.RoleControllerService` | No |
| 3 | IMMS | `android.view.InputMethod` | Yes |
| 4 | A11ySvc | `accessibilityservice.AccessibilityService` | No |
| 5 | WifiSvc | `android.settings.SETTINGS` | No |
| 6 | NMS | `service.notification.NotificationAssistantService` | No |
| 7 | NMS | `service.notification.ConditionProviderService` | Yes |
| 8 | CDM | `permission.PermissionControllerService` | No |

Repository evidence:

- [`SafeModeAnlysis/C2_SafeModeInstrumentation/result.md`](SafeModeAnlysis/C2_SafeModeInstrumentation/result.md)
- [`SafeModeAnlysis/C2_SafeModeInstrumentation/result_raw.txt`](SafeModeAnlysis/C2_SafeModeInstrumentation/result_raw.txt)
- [`SafeModeAnlysis/C2_SafeModeInstrumentation/Instrumentation.diff`](SafeModeAnlysis/C2_SafeModeInstrumentation/Instrumentation.diff)
- [`ResourceBreaker/appshark/c2-violate/results.md`](ResourceBreaker/appshark/c2-violate/results.md)

The local C2 summary records a first `safeMode=true` snapshot at approximately `T0 + 797 ms`, while the paper's deduplicated timing table reports a Safe Mode activation boundary near `T0 + 701 ms`. This difference is retained as an evidence limitation: timing depends on the exact instrumentation point and build. It does not by itself invalidate the observation that unprotected queries occurred before filtering was active.

## 3. Resource construction strategies

ResourceBreaker uses three complementary construction strategies:

1. **Field-level manipulation:** preserve enough AXML/ARSC structure for native parsing, then maximize attacker-controlled lengths or values that are consumed by Java-side allocators. The implementation uses binary-template specifications and Z3-based symbolic constraints.
2. **Decompression bombs:** exploit the difference between compressed APK size and expanded in-memory resource size, including the APK ZIP layer and image-decoder decompression layer.
3. **Coverage-guided fuzzing:** mutate raster image inputs through native decoder paths, with the paper's reported campaign focused on Skia/DNG code.

The checked-in implementation for the first strategy is in [`ResourceBreaker/OOM_SOF_Finder/`](ResourceBreaker/OOM_SOF_Finder/). Its resources include Android resource templates and a vector-drawable source file used for recursive call-graph analysis.

## 4. Fuzzing campaign statistics

The paper reports two AFL++ campaigns, each using 64 parallel instances and running for more than 720 hours.

| Metric | Skia codec | ARSC |
|---|---:|---:|
| AFL++ instances | 64 | 64 |
| Average days per instance | 38.7 | 44.4 |
| Total fuzzer-days | 2,476 | 2,844 |
| Executions | 20.69B | 828.85B |
| Best AFL edge count | 18,406 | 464 |
| Bitmap coverage | 8.29% | 6.60% |
| Raw crashes | 10,527 | 0 |
| Raw hangs | 5,641 | 4,371 |

The paper notes that the harnesses intentionally use system-level resource-loading entry points and real-attack validation checks. Their lower bitmap coverage relative to unconstrained internal-interface fuzzers is therefore an expected trade-off, not a direct measure of decoder quality.

Repository evidence:

- [`ResourceBreaker/fuzzer/results.md`](ResourceBreaker/fuzzer/results.md)
- [`ResourceBreaker/fuzzer/`](ResourceBreaker/fuzzer/)

## 5. Crash-site aggregation

The checked-in fuzzer report aggregates **10,527 raw crash samples** into native decoder locations. The paper summarizes **15 confirmed crash sites and one false positive** in the Skia/DNG campaign. The report includes locations in `dng_sdk`, including:

- integer-overflow sites in `dng_misc_opcodes.cpp`, `dng_lossless_jpeg.cpp`, `dng_read_image.cpp`, `dng_string.cpp`, `dng_utils.cpp`, and `dng_stream.cpp`;
- an ASan heap out-of-bounds read in `RefBaselineABCDtoRGB`;
- a reported stack out-of-bounds condition in `dng_interleave_task::Start` that was not reproducible on the target Android configuration because of a thread-count mismatch.

See [`ResourceBreaker/fuzzer/results.md`](ResourceBreaker/fuzzer/results.md) for the sample-file aggregation. Sample paths in that file are machine-local and are not expected to resolve on another host.

## 6. Vulnerability inventory

The paper reports **33 vulnerabilities across six system components**. Its evaluation summary reports **29 confirmed and 4 pending review**, with the following severity distribution among the reported set:

| Severity | Count |
|---|---:|
| Critical | 12 |
| High | 15 |
| Moderate | 1 |
| Low | 1 |
| **Total classified** | **29** |

The paper reports **18 CVE identifiers assigned by Google following review by the Android Security Team**. The CVE list below is reproduced from the paper's vulnerability inventory; identifiers without a CVE assignment are not included.

### CVE list reported by the Android Security Team

| CVE | Related area | Paper-reported severity |
|---|---|---|
| `CVE-2025-48554` | Device Admin deactivation path | High |
| `CVE-2025-48645` | Device Admin patch bypass via `android:description` | High |
| `CVE-2026-0064` | Device Admin patch bypass via ZIP bomb | High |
| `CVE-2025-48603` | InputMethodManagerService permanent DoS | High |
| `CVE-2026-28633` | VoiceInteractionManagerService permanent DoS | High |
| `CVE-2026-28596` | GameManagerService permanent DoS | High |
| `CVE-2026-0044` | DNG decoder integer overflow | Critical |
| `CVE-2026-0043` | `dng_lossless_jpeg.cpp` integer overflow | Critical |
| `CVE-2026-0042` | `dng_reference.cpp` integer overflow | Critical |
| `CVE-2026-0049` | `dng_misc_opcodes.cpp` integer overflow | Critical |
| `CVE-2026-0051` | `dng_linearization_info.cpp` integer overflow | Critical |
| `CVE-2026-0080` | DNG lossless JPEG decode integer overflow | Critical |
| `CVE-2026-0052` | DNG miscellaneous opcode patch regression | Critical |
| `CVE-2026-0040` | `dng_read_image.cpp` read-path integer overflow | Critical |
| `CVE-2026-0041` | `dng_read_image.cpp` tile-read integer overflow | Critical |
| `CVE-2026-0039` | `dng_area_spec::ScaledOverlap` integer overflow | Critical |
| `CVE-2026-0079` | `dng_utils.cpp` integer overflow | High |
| `CVE-2026-0067` | `dng_stream.cpp` integer overflow | High |

These assignments and confirmations are **attributed to the Android Security Team in the accompanying paper**, not inferred from the local source tree. CVE status, severity, and patch state may change after the paper's reporting date. The 33-entry inventory is grouped as follows:

| Affected area | Entries | Main impact class |
|---|---:|---|
| Device Admin / `DevicePolicyManagerService` | 10 | PDoS and privilege-retention risk through the deactivation path |
| `InputMethodManagerService` | 2 | PDoS through C2 boot-time behavior |
| `CredentialManagerService` | 1 | PDoS through C1 |
| `VoiceInteractionManagerService` | 1 | PDoS through C1 |
| `ShortcutService` | 1 | PDoS through C1 |
| `GameManagerService` | 1 | PDoS through C1 |
| Skia/DNG decoder paths | 17 | PDoS or remote PDoS depending on reachability |
| **Total** | **33** | |

The per-report table, including Android IDs, CVEs, severity, bypass class, and impact label, is reproduced in the paper's Appendix A. This repository does not duplicate the full disclosure inventory because report status and identifiers can change.

> **Consistency note.** One paper passage states “28 confirmed,” while the abstract, evaluation summary, and appendix discussion state “29 confirmed + 4 pending.” This artifact follows the latter count and flags the discrepancy rather than silently resolving it.

## 7. Interpretation and limitations

- **Static reachability is not dynamic exploitability.** A C1/C2 path is a candidate bypass until the corresponding resource is loaded and the crash is observed on the target build.
- **Safe Mode behavior is build-dependent.** OEM changes to Package Manager, system-service startup order, resource parsers, signing policy, or recovery UI can change both reachability and impact.
- **Native sanitizer behavior matters.** Some UBSan findings may terminate instrumented builds even when an unsanitized production build behaves differently; each reported case requires target-build validation.
- **Device Admin impact is path-specific.** The privilege-retention consequence depends on the active-admin state and the availability of alternate deactivation or cleanup paths.
- **Fuzzing counts are campaign statistics, not vulnerability counts.** Multiple samples may represent one crash site, and a single site may have multiple root causes or patch states.
- **No arbitrary-install claim is implied.** The experiments assume the attacker's APK reaches the device through an authorized installation scenario described by the paper's threat model; installation permissions, user confirmation, signing, and target-device policy remain separate boundaries.

## 8. Recommended reproduction record

For each local validation run, record at least:

```text
Target build fingerprint:
Android/API level:
Kernel and native library revision:
Target package name and version code:
APK signing certificate digest:
Caller UID / permissions:
Trigger component and Binder/API path:
Resource type and resource entry:
Safe Mode state and effective query flags:
Install result:
Crash process / signal / exception:
Logcat excerpt and timestamp:
Reboot and Safe Mode result:
Cleanup or factory-reset result:
```

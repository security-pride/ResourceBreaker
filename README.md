# ResourceBreaker

> **Warning - Use only on disposable test devices.** This research includes proof-of-concept resources that may cause an irreversible, permanent denial-of-service condition. The authors may be unable to recover a device after a successful trigger. Do not run the tools or install generated APKs on personal, production, or data-bearing devices; use only authorized engineering devices or emulators whose data can be permanently lost.

ResourceBreaker demonstrates that malformed, attacker-controlled Android resources can bypass Safe Mode recovery and cause permanent denial-of-service, and reports 33 vulnerabilities across six system components, including 29 findings confirmed by the Android Security Team, 12 Critical-severity cases, and 18 CVEs assigned by Google. It accompanies the paper:

> **One Resource to Break Them All: Exploiting Malformed Resources for Permanent Denial-of-Service in Android**
> Sheng Cao, Hao Zhou, Yanjie Zhao, Tianming Liu, Songzhou Shi, and Haoyu Wang.
> ACM CCS 2026. DOI: [10.1145/3830454.3846534](https://doi.org/10.1145/3830454.3846534)

The project investigates how malformed icons, metadata, XML, resource tables, and other APK-contained files can reach privileged Android components through file-system-based resource loading. Its central goal is to connect three stages that are usually studied separately:

1. **Reachability:** identify system-service paths that can resolve or load third-party resources.
2. **Payload construction:** synthesize malformed resources that remain structurally acceptable where necessary but trigger excessive allocation, recursion, decompression, or native decoding failures.
3. **Recovery analysis:** determine whether the resulting crash is filtered by Android Safe Mode or can persist across reboots.

This repository is a research artifact and validation framework, not a general-purpose Android fuzzing distribution. Run experiments only on disposable AOSP/OEM engineering images and test applications that you are authorized to use.

## What the framework studies

### Safe Mode coverage

`SafeModeAnlysis/` contains two complementary audits of Android 15 Safe Mode:

- **C1 - API-level coverage:** a Soot-based backward-reachability analysis over Package Manager Service Binder methods. It checks whether component lookup and resolution paths consult the Safe Mode state.
- **C2 - temporal coverage:** instrumentation of boot-time `queryIntent*Internal` convergence points. It records when queries execute relative to Safe Mode activation and whether `MATCH_SYSTEM_ONLY` is present.

### Crash-path discovery

`ResourceBreaker/` contains the crash-path discovery pipeline:

- **AppShark:** static taint analysis over Android framework artifacts, including lifecycle-aware system-service entry models, structural propagation through containers and fields, and cross-artifact resolution between `services.jar` and `framework.jar`.
- **OOM/SO finder:** grammar-directed analysis for compiled Android resources and recursive vector-drawable parsing paths. It uses binary templates and symbolic constraints to search for field values that induce large allocations or deep recursion.
- **Fuzzer artifacts:** AFL++-based native image-decoding campaigns and crash aggregation results for Skia/DNG paths.

## Repository layout

```text
.
├── README.md
├── EXPERIMENTAL_DATA.md              # Experimental data and paper-aligned measurements
├── ResourceBreaker/
│   ├── OOM_SOF_Finder/               # AXML/ARSC allocation and vector-drawable analysis
│   ├── appshark/                     # Static taint-analysis engine and C1/C2 outputs
│   └── fuzzer/                       # Fuzzing notes and crash-location aggregation
└── SafeModeAnlysis/
    ├── C1_ReachabilityAnalysis/      # Soot-based Safe Mode API audit
    └── C2_SafeModeInstrumentation/   # Boot-time Safe Mode instrumentation results
```

> The directory name `SafeModeAnlysis` is preserved as it exists in the current artifact.

## Experimental data

The detailed measurements, tables, campaign statistics, vulnerability inventory, and evidence limitations are maintained separately from this overview:

**[Read the experimental data](EXPERIMENTAL_DATA.md)**

The data file distinguishes paper-reported aggregate results from repository-local output files. It should be read together with the per-component result files, rather than treated as a replacement for the paper's methodology or disclosure records.

## Quick start

### 1. Safe Mode C1 reachability analysis

Requirements: JDK 17 and Maven.

```bash
cd SafeModeAnlysis/C1_ReachabilityAnalysis
mvn package
# Update config.json for the local Android framework artifacts.
./run.sh
```

The configuration expects the framework and services artifacts used by the target Android build. The generated analysis output and `result.md` summarize the Package Manager Service coverage audit.

### 2. OOM and stack-overflow analysis

Requirements: JDK 22, Maven, the checked-in ANTLR-generated sources, and the local Z3 JAR under `libs/`.

```bash
cd ResourceBreaker/OOM_SOF_Finder
mvn package
```

The Java entry point runs the resource-template symbolic executor and the vector-drawable call-graph analysis. Use the source code and checked-in resources under `src/main/resources/` to reproduce or adapt the analyses for a target framework version.

### 3. AppShark analysis

`ResourceBreaker/appshark/` includes the source tree, pre-built analysis JARs, rule configurations, and representative C1/C2 outputs. The JSON5 configurations contain machine-specific framework paths and must be edited before running locally. Do not assume that the checked-in output was generated from the same Android build as your current target.

### 4. Fuzzing

`ResourceBreaker/fuzzer/` records the native decoder campaign and crash-location aggregation. Native fuzzing requires a separately prepared Android/AOSP build, instrumented harnesses, AFL++, and a disposable device or emulator. The repository does not claim that the checked-in report alone is a complete replayable fuzzing environment.

## Reproducibility notes

- The paper evaluates AOSP Android 15 and reports additional validation on selected devices; exact device, build, and patch state matter.
- Framework paths in analysis configurations are local placeholders and must be replaced.
- Resource parsing behavior can vary with Android release, OEM modifications, compiler sanitizers, native library revisions, heap limits, and boot ordering.
- A static candidate is not automatically a confirmed vulnerability. Confirmation requires a matching resource, the intended trigger path, and dynamic evidence from the target build.
- See [EXPERIMENTAL_DATA.md](EXPERIMENTAL_DATA.md) for the distinction between confirmed, supported, and unvalidated results.

## Responsible use

This project is intended for defensive research, security validation, and responsible disclosure. Do not deploy crash-inducing APKs against devices or services without authorization. When validating a finding, record the target build fingerprint, package identity, signing information, permissions, trigger path, logs, crash signature, recovery behavior, and cleanup result.

## Citation

If you use this artifact, please cite:

```bibtex
@inproceedings{cao2026resourcebreaker,
  author    = {Cao, Sheng and Zhou, Hao and Zhao, Yanjie and Liu, Tianming and Shi, Songzhou and Wang, Haoyu},
  title     = {One Resource to Break Them All: Exploiting Malformed Resources for Permanent Denial-of-Service in Android},
  booktitle = {Proceedings of the 2026 ACM SIGSAC Conference on Computer and Communications Security},
  year      = {2026},
  publisher = {ACM},
  doi       = {10.1145/3830454.3846534}
}
```

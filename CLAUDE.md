# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Single-module Android app named **Zeus Lightning Gates**. Despite that name, the application namespace and `applicationId` are `com.xd.xdtglobal` — keep this distinction in mind when grepping, naming new files, or reading stack traces.

The codebase is currently a near-empty skeleton: both `LoadingActivity` and `MainActivity` have empty `setContent { }` blocks. The presence of game audio assets in `app/src/main/res/raw/` (`game_music.mp3`, `lightning.mp3`, `level_win.mp3`, `level_lose.mp3`) and a custom `font/font.ttf` indicates the intended product is a Compose-based game; the gameplay/UI has not been implemented yet, so most feature work means building from scratch rather than modifying existing screens.

## Build / run / test

Use the Gradle wrapper from the repo root.

```sh
./gradlew assembleDebug          # build debug APK
./gradlew installDebug           # install on connected device/emulator
./gradlew test                   # JVM unit tests (app/src/test)
./gradlew connectedAndroidTest   # instrumented tests on a device/emulator (app/src/androidTest)
./gradlew lint                   # Android lint
./gradlew clean
```

Run a single unit test class / method:

```sh
./gradlew :app:testDebugUnitTest --tests "com.xd.xdtglobal.ExampleUnitTest"
./gradlew :app:testDebugUnitTest --tests "com.xd.xdtglobal.ExampleUnitTest.someTest"
```

There is no separate launcher target — the **launcher activity is `LoadingActivity`** (it owns the `MAIN`/`LAUNCHER` intent filter in `AndroidManifest.xml`), not `MainActivity`. `MainActivity` is `exported="false"` and is intended to be started from `LoadingActivity`.

## Toolchain

- AGP **9.1.1**, Kotlin **2.2.10**, Compose BOM **2026.02.01** — these are very recent versions; if a build fails on a fresh machine, check that Android Studio / command-line tools are new enough rather than downgrading.
- `compileSdk = 36` is declared with the new DSL form (`compileSdk { version = release(36) { minorApiLevel = 1 } }`). Don't rewrite this to the older `compileSdk = 36` shorthand — it's intentional.
- `minSdk = 24`, `targetSdk = 36`, Java 11 source/target.
- Dependencies are managed through the version catalog at `gradle/libs.versions.toml`. Add new libraries there (under `[versions]` and `[libraries]`) and reference them as `libs.xxx` from `app/build.gradle.kts`; do not hard-code coordinates in the module build file.
- Only Compose + Material3 + Activity-Compose + Lifecycle are wired up. There is no DI framework, no navigation library, no networking client, no persistence — picking one is a real decision, not a default; ask before introducing one.

## Local config

`local.properties` and `gradle.properties` are checked in. `local.properties` typically contains the SDK path and is machine-specific — avoid committing changes to it.

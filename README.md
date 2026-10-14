# ShoppingListTesting

An educational Android shopping-list app with Room storage, Pixabay image search, and testing examples.

## Run locally

1. Open the repository in Android Studio. Use JDK 11 as the Gradle JDK and the checked-in Gradle 7.5 wrapper (Android Gradle Plugin 7.4.2).
2. Install Android SDK Platform 33 and Android SDK Build-Tools 30.0.3 (the plugin default). Use an emulator or device running Android 5.0 / API 21 or newer.
3. Get your own API key from [Pixabay's API documentation](https://pixabay.com/api/docs/).
4. Add the setting from [gradle.properties.example](gradle.properties.example) to your **user** Gradle properties file: `~/.gradle/gradle.properties` on macOS/Linux or `%USERPROFILE%\.gradle\gradle.properties` on Windows. Create the file if needed; preserve existing settings. Replace `YOUR_PIXABAY_API_KEY` with your own value.
5. Sync Gradle, select the `app` configuration, and run. Image search needs internet access and a valid key.

Build from the repository root:

```sh
sh ./gradlew :app:assembleDebug
```

On Windows use `gradlew.bat :app:assembleDebug`. The APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Configuration placeholders

| Setting | Purpose | Without a real value |
| --- | --- | --- |
| `PIXABAY_API_KEY` | Key passed to Pixabay image searches | The default is empty; remote image search cannot succeed. |
| `sdk.dir` in local `local.properties` | Installed Android SDK directory | Android Studio can create this file; CLI builds need an SDK path. |

`PIXABAY_API_KEY` can also be supplied as an environment variable before starting Gradle/Android Studio. A Gradle property takes precedence. Rebuild after changing it: the existing Retrofit interface reads `BuildConfig.API_KEY`, generated from `PIXABAY_API_KEY`.

The example value is a placeholder, not a shared working credential. Keep your own key outside the repository. The existing project `gradle.properties` contains build settings and should not be replaced with the example file.

## Build and test notes

Use the wrapper and documented JDK for this older dependency set. See the [AGP 7.4 compatibility table](https://developer.android.com/build/releases/agp-7-4-0-release-notes#compatibility). If dependency resolution fails, check repository availability in the root `build.gradle`; it still contains historical `jcenter()` entries. The Hilt plugin, Hilt runtime/compiler, and Robolectric versions also come from the original teaching project and may need coordinated updates for a newer toolchain.

Local tests: `sh ./gradlew :app:testDebugUnitTest`. Instrumented tests require a running emulator/device: `sh ./gradlew :app:connectedDebugAndroidTest`.

Build verification on 2026-10-14: `:app:assembleDebug` completed with JDK 17, SDK Platform 33, and the checked-in wrapper. The APK was built with an empty API key; generation of the configuration was also checked with a dummy value. Real Pixabay responses and device UI behavior have not been verified.

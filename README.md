<p align="center">
  <img src="https://reseam.app/logo.svg" alt="Reseam logo" width="96">
</p>

<h1 align="center">Reseam patch bundle template</h1>

Start here to write your own patches for [Reseam](https://reseam.app). Copy this repository, rename a few things, and you have a bundle that builds, signs itself, and publishes releases Reseam Manager can install.

It ships two example patches that work on any app:

- `Enable debugging` edits the manifest.
- `Allow screenshots` redirects calls into Java code shipped in the bundle (an extension).

The [docs](https://reseam.app/docs/authoring/start/) walk through this template step by step.

## Setup

- JDK 17.
- Android SDK, with `ANDROID_HOME` pointing at it.
- The `reseam` CLI, on `PATH` or at the path in `RESEAM_BIN`. Use the version that matches the plugin version in `settings.gradle.kts`. Releases ship Linux x64 and Windows x64 binaries; on other platforms [build it from source](https://reseam.app/docs/cli/install/).
- A signing key: `reseam bundle keygen --out ~/.reseam/bundle-signing.key`. It prints the public key users will trust. Keep the key file private and out of the repository.

## Make it yours

1. Set `name`, `author`, and `description` in `manifest.toml`, and `rootProject.name` in `settings.gradle.kts`.
2. Rename `apps/example` to the app you patch and change the `app.example` packages to your own.
3. Replace the example patches with your own.

Layout:

```text
manifest.toml                     bundle name, author, description
settings.gradle.kts               Reseam plugin version
apps/<app>/patch/                 Kotlin patches, one module per app
apps/<app>/extensions/<name>/     Java code injected into the app
shared/<name>/                    extensions used by more than one app
```

Modules need no build script. The `app.reseam.workspace` plugin configures them from the layout. How to write patches is in [Your first patch](https://reseam.app/docs/authoring/first_patch/).

## Build and test

```shell
./gradlew bundle
reseam bundle list build/reseam/example-patches.reseam --trust <public key>
reseam patch app.apk \
  --bundle build/reseam/example-patches.reseam \
  --trust <public key> \
  --enable "Enable debugging" \
  --output patched.apk
```

## Release

The workflow in `.github/workflows/release.yml` runs on `v*` tags. It builds and signs the bundle, writes `patches.json`, and attaches both to a GitHub release.

1. Add the signing key as the repository secret `BUNDLE_SIGNING_KEY_B64`: `base64 -w0 ~/.reseam/bundle-signing.key`.
2. Push a tag: `git tag v0.1.0 && git push origin v0.1.0`.

Users paste this URL into Add bundle in Reseam Manager to get updates. Reseam Manager shows the signer's public key before trusting it, so publish your key where users can check it:

```text
https://github.com/<owner>/<repo>/releases/latest/download/patches.json
```

When you move to a new Reseam version, bump the plugin version in `settings.gradle.kts`. The release workflow downloads the CLI of that same version.

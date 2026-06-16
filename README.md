# koin-proxy

This project is a small Android/Kotlin sample for comparing direct Koin usage with a thin proxy layer over Koin.

## Modules

### `:app`

Android Compose demo app.

It depends on:

- `:koin-proxy`
- `:example-with-koin-proxy`
- `:example-with-koin-direct`

The app starts Koin through the proxy API, loads the sample modules, and displays strings returned by each sample API.

### `:koin-proxy`

Thin wrapper module around Koin.

This module owns the real Koin dependency and exposes a small project-defined API:

- `koinProxyModule { ... }`
- `single(SomeType::class) { ... }`
- `startKoinProxy(...)`
- `getSingle(SomeType::class)`
- `injectSingle(SomeType::class)`

The goal is to let app or feature modules avoid importing `org.koin.*` directly when they choose to use the proxy layer.

### `:example-with-koin-proxy`

Example feature module using `:koin-proxy`.

It registers `ProxyExampleService` through `koinProxyModule`, then exposes `proxyExampleValue()` as a simple public API. The API resolves the service through the proxy and returns a string from it.

### `:example-with-koin-direct`

Example feature module using Koin directly.

It depends on `koin-core`, defines a native Koin `module { single { ... } }`, loads that module into the global Koin context, and exposes `directKoinExampleValue()` as a simple public API.

This module is intentionally kept as a contrast case for direct Koin usage.

## Dependency Direction

```text
:app
  ├── :koin-proxy
  ├── :example-with-koin-proxy ──> :koin-proxy ──> Koin
  └── :example-with-koin-direct ────────────────> Koin
```

## Build

```bash
./gradlew :app:assembleDebug
```

# koin-proxy

本项目是一个小型的 Android/Kotlin 示例，用于对比直接使用 Koin 与通过一层薄代理使用 Koin 两种方式。

## 模块

### `:app`

基于 Android Compose 的示例应用。

它依赖：

- `:koin-proxy`
- `:example-with-koin-proxy`
- `:example-with-koin-direct`

应用通过代理 API 启动 Koin，加载示例模块，并展示各个示例 API 返回的字符串。

### `:koin-proxy`

对 Koin 进行封装的薄代理模块。

该模块持有真正的 Koin 依赖，对外暴露一套项目自定义的 API：

- `koinProxyModule { ... }`
- `single(SomeType::class) { ... }`
- `startKoinProxy(...)`
- `getSingle(SomeType::class)`
- `injectSingle(SomeType::class)`

其目标是让 app 或 feature 模块在选择使用代理层时，无需直接引入 `org.koin.*`。

### `:example-with-koin-proxy`

使用 `:koin-proxy` 的示例 feature 模块。

它通过 `koinProxyModule` 注册 `ProxyExampleService`，并以 `proxyExampleValue()` 作为简单的对外 API。该 API 通过代理解析对应服务，并返回其中的字符串。

### `:example-with-koin-direct`

直接使用 Koin 的示例 feature 模块。

它依赖 `koin-core`，定义原生 Koin 的 `module { single { ... } }`，将该模块加载到全局 Koin 上下文中，并以 `directKoinExampleValue()` 作为简单的对外 API。

此模块作为直接使用 Koin 的对照示例而保留。

## 依赖方向

```text
:app
  ├── :koin-proxy
  ├── :example-with-koin-proxy ──> :koin-proxy ──> Koin
  └── :example-with-koin-direct ────────────────> Koin
```

## 构建

```bash
./gradlew :app:assembleDebug
```

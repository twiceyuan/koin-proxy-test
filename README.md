# koin-proxy

本项目是一个小型的 Android/Kotlin 示例，用于对比直接使用 Koin 与通过桥接层使用 Koin 两种方式。

桥接层的目标是严格隔离 Koin：业务模块只引用 `:koin-proxy-api`，编译出来的业务模块字节码里不包含 `org.koin.*` / `org/koin` 引用。真正的 Koin 依赖只存在于 `:koin-3-proxy` 或 `:koin-4-proxy` 实现模块中。

项目同时支持 Koin `3.1.5` 和 Koin `4.2.2`，通过 Gradle 属性 `koinProxyMajor` 切换。

## 模块

### `:app`

基于 Android Compose 的示例应用。

它依赖：

- `:koin-proxy-api`
- `:koin-3-proxy` 或 `:koin-4-proxy`（由 `koinProxyMajor` 属性决定）
- `:example-with-koin-proxy`
- `:example-with-direct-koin-3` 或 `:example-with-direct-koin-4`（由 `koinProxyMajor` 属性决定）

应用通过适配器启动真实 Koin 运行时，通过 `:koin-proxy-api` 定义和获取对象，并展示各个示例 API 返回的字符串。

### `:koin-proxy-api`

桥接层的纯 API 模块，不依赖 Koin。

该模块只暴露项目自定义的 API 和定义模型：

- `koinProxyModule { ... }`
- `single { ... }` / `factory { ... }` / `scope { ... }` / `scoped { ... }`
- `named("qualifier")` 限定符
- `getSingle<T>()` / `injectSingle<T>()`
- `getFactory<T>()` / `injectFactory<T>()`
- `createScope()` / `getScope()`
- `loadKoinProxyModules()` / `unloadKoinProxyModules()`

其中 reified inline API 只会把 `T` 转成 `KClass`，然后调用桥接层自己的非 inline API，不会 inline 任何 Koin 调用到业务模块字节码中。

### `:koin-3-proxy`

桥接层的 Koin 3.x 实现模块（Koin `3.1.5`）。

### `:koin-4-proxy`

桥接层的 Koin 4.x 实现模块（Koin `4.2.2`）。

适配器模块持有真正的 Koin 依赖，负责：

- `startKoinProxy(...)`
- 将 `:koin-proxy-api` 中的定义模型转换为 Koin Module
- 安装基于 Koin 的运行时解析器
- Scope 管理

业务模块不应该直接依赖或导入该模块中的 Koin 相关实现细节；通常只由 app 启动阶段使用它来接入真实运行时。

### `:example-with-koin-proxy`

使用 `:koin-proxy-api` 的示例 feature 模块。

该模块不依赖 Koin，编译产物中不应出现 `org.koin.*` / `org/koin`。

### `:example-with-direct-koin-3`

直接使用 Koin 3 的示例 feature 模块。

### `:example-with-direct-koin-4`

直接使用 Koin 4 的示例 feature 模块。

以上两个模块作为直接使用 Koin 的对照示例而保留。

## 依赖方向

```text
:app
  ├── :koin-proxy-api
  ├── :koin-3-proxy / :koin-4-proxy ──> :koin-proxy-api + Koin
  ├── :example-with-koin-proxy ────────> :koin-proxy-api
  └── :example-with-direct-koin-3/4 ──> Koin
```

## 构建

```bash
# 使用 Koin 3（默认）
./gradlew :app:assembleDebug

# 使用 Koin 4
./gradlew :app:assembleDebug -PkoinProxyMajor=4
```

## 隔离验证

可以用下面的方式检查使用桥接层的模块字节码中是否存在 Koin 引用：

```bash
rg -a "org[./]koin" koin-proxy-api/build/libs koin-proxy-api/build/classes
rg -a "org[./]koin" example-with-koin-proxy/build/libs example-with-koin-proxy/build/classes
```

预期结果是没有输出。

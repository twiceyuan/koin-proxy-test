package io.github.twiceyuan.koin.proxy.di

import kotlin.reflect.KClass

class KoinProxyModule(
    val definitions: List<KoinProxyDefinition<*>>,
)

class KoinProxyDefinition<T : Any>(
    val type: KClass<T>,
    val factory: () -> T,
)

class KoinProxyModuleBuilder {
    private val definitions = mutableListOf<KoinProxyDefinition<*>>()

    fun <T : Any> single(type: KClass<T>, factory: () -> T) {
        definitions += KoinProxyDefinition(type, factory)
    }

    inline fun <reified T : Any> single(noinline factory: () -> T) {
        single(T::class, factory)
    }

    fun build(): KoinProxyModule = KoinProxyModule(definitions.toList())
}

interface KoinProxyResolver {
    fun <T : Any> get(type: KClass<T>): T
}

object KoinProxyRuntime {
    @Volatile
    private var resolver: KoinProxyResolver? = null

    fun installResolver(resolver: KoinProxyResolver) {
        this.resolver = resolver
    }

    fun <T : Any> get(type: KClass<T>): T {
        return checkNotNull(resolver) { "KoinProxyRuntime has not been started." }.get(type)
    }
}

fun koinProxyModule(declaration: KoinProxyModuleBuilder.() -> Unit): KoinProxyModule {
    return KoinProxyModuleBuilder().apply(declaration).build()
}

fun <T : Any> getSingle(type: KClass<T>): T {
    return KoinProxyRuntime.get(type)
}

inline fun <reified T : Any> getSingle(): T {
    return getSingle(T::class)
}

fun <T : Any> injectSingle(type: KClass<T>): Lazy<T> {
    return lazy { getSingle(type) }
}

inline fun <reified T : Any> injectSingle(): Lazy<T> {
    return injectSingle(T::class)
}

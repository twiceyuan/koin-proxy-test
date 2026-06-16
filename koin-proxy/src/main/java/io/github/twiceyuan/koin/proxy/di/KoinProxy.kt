package io.github.twiceyuan.koin.proxy.di

import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.instance.SingleInstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import kotlin.reflect.KClass

private val rootScopeQualifier = named("_root_")

class KoinProxyModule internal constructor(
    internal val source: Module,
)

class KoinProxyModuleBuilder internal constructor() {
    private val source = module { }

    @OptIn(KoinInternalApi::class)
    fun <T : Any> single(type: KClass<T>, factory: () -> T) {
        val definition = BeanDefinition(
            scopeQualifier = rootScopeQualifier,
            primaryType = type,
            definition = { factory() },
            kind = Kind.Singleton,
        )
        val instanceFactory = SingleInstanceFactory(definition)
        source.indexPrimaryType(instanceFactory)
    }

    internal fun build(): KoinProxyModule = KoinProxyModule(source)
}

fun koinProxyModule(declaration: KoinProxyModuleBuilder.() -> Unit): KoinProxyModule {
    return KoinProxyModuleBuilder().apply(declaration).build()
}

fun startKoinProxy(modules: List<KoinProxyModule>) {
    startKoin {
        modules(modules.map { it.source })
    }
}

fun <T : Any> injectSingle(type: KClass<T>): Lazy<T> {
    return lazy { getSingle(type) }
}

fun <T : Any> getSingle(type: KClass<T>): T {
    return GlobalContext.get().get(type)
}

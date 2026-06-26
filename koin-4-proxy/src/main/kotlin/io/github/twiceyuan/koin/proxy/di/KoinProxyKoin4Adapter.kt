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

private val rootScopeQualifier = named("_root_")

fun startKoinProxy(modules: List<KoinProxyModule>) {
    startKoin {
        modules(modules.map { it.toKoinModule() })
    }
    KoinProxyRuntime.installResolver(KoinBackedProxyResolver)
}

private object KoinBackedProxyResolver : KoinProxyResolver {
    override fun <T : Any> get(type: kotlin.reflect.KClass<T>): T {
        return GlobalContext.get().get(type)
    }
}

@OptIn(KoinInternalApi::class)
private fun KoinProxyModule.toKoinModule(): Module {
    val koinModule = module { }
    definitions.forEach { definition ->
        val beanDefinition = BeanDefinition(
            scopeQualifier = rootScopeQualifier,
            primaryType = definition.type,
            definition = { definition.factory() },
            kind = Kind.Singleton,
        )
        val instanceFactory = SingleInstanceFactory(beanDefinition)
        koinModule.indexPrimaryType(instanceFactory)
    }
    return koinModule
}

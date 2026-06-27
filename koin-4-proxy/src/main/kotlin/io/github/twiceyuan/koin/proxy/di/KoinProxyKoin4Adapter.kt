package io.github.twiceyuan.koin.proxy.di

import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.definition.BeanDefinition
import org.koin.core.definition.Kind
import org.koin.core.instance.FactoryInstanceFactory
import org.koin.core.instance.SingleInstanceFactory
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
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
    override fun <T : Any> get(type: kotlin.reflect.KClass<T>, qualifier: KoinProxyQualifier?): T {
        val koinQualifier = qualifier?.toKoinQualifier()
        return GlobalContext.get().get(type, koinQualifier)
    }

    override fun loadModules(modules: List<KoinProxyModule>) {
        GlobalContext.get().loadModules(modules.map { it.toKoinModule() })
    }

    override fun unloadModules(modules: List<KoinProxyModule>) {
        GlobalContext.get().unloadModules(modules.map { it.toKoinModule() })
    }

    override fun getScopeRegistry(): KoinProxyScopeRegistry {
        return Koin4ScopeRegistry
    }
}

private object Koin4ScopeRegistry : KoinProxyScopeRegistry {
    override fun createScope(scopeId: String, qualifier: KoinProxyQualifier): KoinProxyScope {
        val koinQualifier = qualifier.toKoinQualifier()
        val koinScope = GlobalContext.get().createScope(scopeId, koinQualifier)
        return Koin4Scope(koinScope)
    }

    override fun getScope(scopeId: String): KoinProxyScope? {
        val koinScope = GlobalContext.get().getScopeOrNull(scopeId)
        return koinScope?.let { Koin4Scope(it) }
    }
}

private class Koin4Scope(private val scope: org.koin.core.scope.Scope) : KoinProxyScope {
    override fun <T : Any> get(type: kotlin.reflect.KClass<T>, qualifier: KoinProxyQualifier?): T {
        val koinQualifier = qualifier?.toKoinQualifier()
        return scope.get(type, koinQualifier)
    }

    override fun close() {
        scope.close()
    }
}

private fun KoinProxyQualifier.toKoinQualifier(): Qualifier = when (this) {
    is KoinProxyQualifier.Named -> named(this.name)
}

@OptIn(KoinInternalApi::class)
private fun KoinProxyModule.toKoinModule(): Module {
    val koinModule = module { }

    // 处理普通定义
    definitions.forEach { definition ->
        val beanDefinition = BeanDefinition(
            scopeQualifier = rootScopeQualifier,
            primaryType = definition.type,
            definition = { definition.factory() },
            kind = when (definition.kind) {
                DefinitionKind.SINGLE -> Kind.Singleton
                DefinitionKind.FACTORY -> Kind.Factory
                DefinitionKind.SCOPED -> Kind.Scoped
            },
            qualifier = definition.qualifier?.toKoinQualifier(),
        )
        val instanceFactory = when (definition.kind) {
            DefinitionKind.SINGLE -> SingleInstanceFactory(beanDefinition)
            DefinitionKind.FACTORY -> FactoryInstanceFactory(beanDefinition)
            DefinitionKind.SCOPED -> SingleInstanceFactory(beanDefinition)
        }
        koinModule.indexPrimaryType(instanceFactory)
    }

    // 处理作用域定义
    scopeDefinitions.forEach { scopeDef ->
        val scopeQualifier = scopeDef.qualifier.toKoinQualifier()
        scopeDef.definitions.forEach { definition ->
            val beanDefinition = BeanDefinition(
                scopeQualifier = scopeQualifier,
                primaryType = definition.type,
                definition = { definition.factory() },
                kind = Kind.Scoped,
                qualifier = definition.qualifier?.toKoinQualifier(),
            )
            val instanceFactory = SingleInstanceFactory(beanDefinition)
            koinModule.indexPrimaryType(instanceFactory)
        }
    }

    return koinModule
}

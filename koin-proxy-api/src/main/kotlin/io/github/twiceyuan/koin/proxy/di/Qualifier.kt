package io.github.twiceyuan.koin.proxy.di

/** 限定符抽象，用于区分同类型的不同实例 */
sealed class KoinProxyQualifier {
    data class Named(val name: String) : KoinProxyQualifier()
}

/** 创建字符串限定符 */
fun named(name: String): KoinProxyQualifier = KoinProxyQualifier.Named(name)

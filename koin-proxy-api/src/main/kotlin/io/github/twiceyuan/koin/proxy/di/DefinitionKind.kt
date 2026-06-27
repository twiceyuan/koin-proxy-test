package io.github.twiceyuan.koin.proxy.di

/** 定义类型 */
enum class DefinitionKind {
    /** 全局单例 */
    SINGLE,

    /** 每次获取创建新实例 */
    FACTORY,

    /** 作用域内单例 */
    SCOPED,
}

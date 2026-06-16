package io.github.twiceyuan.koin.direct.example

import org.koin.core.context.GlobalContext
import org.koin.dsl.module

class DirectKoinExampleService {
    fun value(): String = "value from direct Koin module"
}

private val directKoinExampleModule = module {
    single { DirectKoinExampleService() }
}

fun loadDirectKoinExampleModule() {
    GlobalContext.get().loadModules(listOf(directKoinExampleModule))
}

fun directKoinExampleValue(): String {
    return GlobalContext.get().get<DirectKoinExampleService>().value()
}

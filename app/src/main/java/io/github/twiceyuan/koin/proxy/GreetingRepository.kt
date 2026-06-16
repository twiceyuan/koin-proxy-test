package io.github.twiceyuan.koin.proxy

class GreetingRepository {
    fun greeting(name: String): String = "Hello $name from Koin single!"
}

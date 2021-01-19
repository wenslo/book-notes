package com.github.wenslo.redisinaction

import redis.clients.jedis.Jedis

/**
 * Redis In Action 第一章 代码
 */
class Chapter1 {
    fun run() {
        val jedis = Jedis("127.0.0.1", 6379)
    }
}

fun main() {
    val chapter1 = Chapter1()
    chapter1.run()
}
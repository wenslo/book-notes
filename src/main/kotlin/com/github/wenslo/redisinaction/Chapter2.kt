package com.github.wenslo.redisinaction

import redis.clients.jedis.Jedis

class Chapter2 {

    companion object {
        var QUIT = false
        const val LIMIT = 10000000
    }

    fun run() {
        val conn = Jedis("127.0.0.1", 6379)
        conn.select(15)
    }

    fun checkToken(conn: Jedis, token: String): String? {
        return conn.hget("login:", token)
    }

    fun updateToken(conn: Jedis, token: String, user: String, item: String?) {
        val currentTimeMillis = System.currentTimeMillis()
        conn.hset("login:", token, user)
        conn.zadd("recent:", currentTimeMillis.toDouble(), token)
        if (item != null) {
            conn.zadd("viewed:$token", currentTimeMillis.toDouble(), item)
            conn.zremrangeByRank("viewed:${token}", 0, -26)
        }
    }

    fun cleanSessions(conn: Jedis) {
        while (!QUIT) {
            val size = conn.zcard("recent:")
            if (size <= LIMIT) {
                Thread.sleep(1000)
                continue
            }
            val endIndex = (size - LIMIT).coerceAtMost(100)
            val tokens = conn.zrange("recent:", 0, endIndex - 1)
            var sessionKeys = mutableListOf<String>()
            for (token in tokens) {
                sessionKeys.add("viewed:$token")
            }
            conn.del(*sessionKeys.toTypedArray())
            conn.hdel("login:", *tokens.toTypedArray())
            conn.zrem("recent:", *tokens.toTypedArray())
        }
    }
}

fun main() {

}
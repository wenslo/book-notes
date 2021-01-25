package com.github.wenslo.redisinaction

import redis.clients.jedis.Jedis
import kotlin.math.min

class Chapter2 {

    companion object {
        var QUIT = false
        const val LIMIT = 10000000
    }

    fun run() {
        val conn = Jedis("127.0.0.1", 6379)
        conn.select(15)
    }

    /****************************************** 登录和cookie缓存 start**********************************/
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
    /****************************************** 登录和cookie缓存 end**********************************/

    /****************************************** 购物车 start ****************************************/
    fun addToCart(conn: Jedis, session: String, item: String, count: Long) {
        if (count <= 0)
            conn.hdel("cart:$session", item)
        else
            conn.hset("cart:$session", item, count.toString())
    }

    fun cleanFullSessions(conn: Jedis) {
        while (!QUIT) {
            val size = conn.zcard("recent:")
            if (size <= LIMIT) {
                Thread.sleep(1000)
                continue
            }
            val endIndex = min(size - LIMIT, 100)
            val sessions = conn.zrange("recent:", 0, endIndex - 1)
            val sessionKeys = mutableListOf<String>()
            for (sess in sessions) {
                sessionKeys.add("viewed:$sess")
                sessionKeys.add("cart:$sess")
            }
            conn.del(*sessionKeys.toTypedArray())
            conn.hdel("login:", *sessions.toTypedArray())
            conn.zrem("recent:", *sessions.toTypedArray())
        }
    }

    fun cacheRequest(
        conn: Jedis,
        request: String,
        callback: (request: String) -> String,
        canCache: (conn: Jedis, request: String) -> Boolean
    ): String {
        // if not can cache
        if (!canCache(conn, request)) {
            return callback(request)
        }
        val pageKey = "cache:${request.hashCode()}"
        var content = conn.get(pageKey)
        if (content.isBlank()) {
            content = callback(request)
            conn.setex(pageKey, 300, content)
        }
        // set with expired
        return content
    }

    /****************************************** 购物车 end **************************************/

    /****************************************** 数据行缓存 start *********************************/
}

fun main() {

}
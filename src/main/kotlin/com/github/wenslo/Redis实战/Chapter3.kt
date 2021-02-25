package com.github.wenslo.Redis实战

import redis.clients.jedis.Jedis

class Chapter3 {
    /**
     * 字符串
     */
    fun string() {
        val conn = Jedis("127.0.0.1", 6379)
        conn.select(15)
        conn.get("key")
        conn.incr("key")
        conn.decr("key")
        conn.get("key")
        conn.set("key", "15")
        conn.decrBy("key", 5)
        conn.get("key")
        conn.set("key", "13")
        conn.incr("key")

        conn.append("new-string-key", "hello")
        conn.append("new-string-key", "world")
        conn.substr("new-string-key", 3, 7)
        conn.setrange("new-string-key", 0, "H")
        conn.setrange("new-string-key", 6, "W")
        conn.get("new-string-key")
        conn.setrange("new-string-key", 11, ", how are you?")
        conn.get("new-string-key")
        conn.setbit("another-key", 2, "1")
        conn.get("another-key")
    }

    /**
     * 列表
     */
    fun list(conn: Jedis) {
        conn.select(15)
        conn.rpush("list-key", "last")
        conn.rpush("list-key", "new last")
        conn.rpush("list-key", "new last")
        conn.lrange("list-key", 0, -1)
        conn.lpop("list-key")
        conn.lpop("list-key")
        conn.lrange("list-key", 0, -1)
        conn.rpush("list-key", "a", "b", "c")
        conn.lrange("list-key", 0, -1)
        conn.ltrim("list-key", 2, -1)
        conn.lrange("list-key", 0, -1)

        conn.rpush("list", "item1")
        conn.rpush("list", "item2")
        conn.rpush("list2", "item3")
        conn.brpoplpush("list2", "list", 1)

        conn.brpoplpush("list2", "list", 1)
        conn.lrange("list", 0, -1)
        conn.brpoplpush("list", "list2", 1)
        conn.blpop(1, "list", "list2")
        conn.blpop(1, "list", "list2")
        conn.blpop(1, "list", "list2")
    }

    /**
     * 集合
     */
    fun set(conn: Jedis) {
        conn.sadd("set-key", "a", "b", "c")
        conn.srem("set-key", "c", "d")
        conn.srem("set-key", "c", "d")
        conn.scard("set-key")
        conn.smembers("set-key")
        conn.smove("set-key", "set-key2", "a")
        conn.smove("set-key", "set-key", "c")
        conn.smembers("set-key2")

        conn.sadd("skey1", "a", "b", "c", "d")
        conn.sadd("skey2", "c", "d", "e", "f")
        conn.sdiff("skey1", "skey2")
        conn.sinter("skey1", "skey2")
        conn.sunion("skey1", "skey2")
    }

    /**
     * 散列
     */
    fun hash(conn: Jedis) {
        conn.hmset("hash-key", mutableMapOf("k1" to "k2", "k2" to "k2", "ke" to "k3"))
        conn.hmget("hash-key", "k2", "k3")
        conn.hlen("hash-key")
        conn.hdel("hash-key", "k1", "k3")


        conn.hmset("hash-key2", mutableMapOf("short" to "hello", "long" to "1000000000000000000000000"))
        conn.hkeys("hash-key2")
        conn.hexists("hash-key2", "num")
        conn.hincrBy("hahs-key2", "num", 1)
        conn.hexists("hash-key2", "num")
    }
}

fun main() {

}
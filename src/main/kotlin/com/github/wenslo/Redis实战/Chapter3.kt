package com.github.wenslo.Redis实战

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub
import redis.clients.jedis.SortingParams
import redis.clients.jedis.ZParams
import java.util.concurrent.CountDownLatch

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

    /**
     * 有序集合
     */
    fun sortedSet(conn: Jedis) {
        conn.zadd("zset-key", mutableMapOf("a" to 3.0, "b" to 2.0, "c" to 1.0))
        conn.zcard("zset-key")
        conn.zincrby("zset-key", 3.0, "c")
        conn.zscore("zset-key", "b")
        conn.zrank("zset-key", "c")
        conn.zcount("zset-key", 0.0, 3.0)
        conn.zrem("zset-key", "b")
        conn.zrangeWithScores("zset-key", 0, -1)

        conn.zadd("zset-1", mutableMapOf("a" to 1.0, "b" to 2.0, "c" to 3.0))
        conn.zadd("zset-2", mutableMapOf("b" to 4.0, "c" to 1.0, "d" to 1.0))
        conn.zinterstore("zset-i", "z-set1", "z-set2")
        conn.zrangeWithScores("zset-i", 0, -1)

        val params = ZParams().aggregate(ZParams.Aggregate.MAX)
        conn.zunionstore("zset-u", params, "zset-1", "zset-2")
        conn.zrangeWithScores("zset-u", 0, -1)
        conn.zadd("set-1", mutableMapOf("a" to 0.0, "b" to 0.0))
        conn.zunionstore("zset-u2", "zset-1", "zset-2", "set-1")
        conn.zrangeWithScores("zset-u2", 0, -1)
    }

    /**
     * 发布和订阅
     * publish and subscribe
     */
    fun publisher(conn: Jedis, n: Int) {
        Thread.sleep(1000)
        for (i in 0..n) {
            conn.publish("channel", "$n")
            Thread.sleep(1000)
        }
    }

    fun run_pubsub(conn: Jedis) {
        val cdl = CountDownLatch(3);
        for (i in 0..3) {
            Thread().run { publisher(conn, 3) }
            cdl.countDown()
        }
        val listener = RedisSubPubListener()
        conn.subscribe(listener, "channel")
    }

    /**
     * 排序
     */
    fun sort(conn: Jedis) {
        conn.rpush("sort-input", "23", "15", "110", "7")
        conn.sort("sort-input")
        // 按字母表顺序对元素进行排序
        conn.sort("sort-input", SortingParams().alpha())
        conn.hset("d-7", mutableMapOf("field" to "5"))
        conn.hset("d-15", mutableMapOf("field" to "1"))
        conn.hset("d-23", mutableMapOf("field" to "9"))
        conn.hset("d-110", mutableMapOf("field" to "3"))
        conn.sort("sort-input", SortingParams().by("d-*->field"))
        conn.sort("sort-input", SortingParams().by("d-*->field"), "d-*->field")
    }

    /**
     * 事务
     */
    fun notrans(conn: Jedis) {
        println(conn.incr("notrans:"))
        Thread.sleep(100)
        conn.incrBy("notrans:", -1)
    }

    fun notransTest(conn: Jedis) {
        for (i in 0..3) {
            Thread().run { notrans(conn) }
        }
        Thread.sleep(500)
    }

    fun trans(conn: Jedis) {
        val pipelined = conn.pipelined()
        pipelined.incr("trans:")
        Thread.sleep(100)
        pipelined.incrBy("trans:", -1)
        println(pipelined.exec().get())
    }

    fun transTest(conn: Jedis) {
        for (i in 0..3) {
            Thread().run { trans(conn) }
        }
        Thread.sleep(400)
    }

    /**
     * time to alive
     * 过期时间
     */
    fun ttl(conn: Jedis) {
        conn.set("key", "value")
        conn.get("key")
        conn.expire("key", 2)
        Thread.sleep(2000)
        conn.get("key")
        conn.set("key", "value2")
        conn.expire("key", 100)
        conn.ttl("key")
    }
}

class RedisSubPubListener : JedisPubSub() {
    companion object {
        var count = 0;
    }

    // 取得订阅的消息后的处理
    override fun onMessage(channel: String, message: String) {
        //TODO 接收订阅频道消息后，业务处理逻辑

        println("$channel=$message")
        count++
        if (count == 4) unsubscribe()
    }

    // 初始化订阅时候的处理 
    override fun onSubscribe(channel: String?, subscribedChannels: Int) {
        println("$channel=$subscribedChannels");
    }

    // 取消订阅时候的处理 
    override fun onUnsubscribe(channel: String?, subscribedChannels: Int) {
        println("$channel=$subscribedChannels");
    }

    // 初始化按表达式的方式订阅时候的处理 
    override fun onPSubscribe(pattern: String?, subscribedChannels: Int) {
        println("$pattern=$subscribedChannels");
    }

    // 取消按表达式的方式订阅时候的处理 
    override fun onPUnsubscribe(pattern: String?, subscribedChannels: Int) {
        println("$pattern=$subscribedChannels");
    }

    // 取得按表达式的方式订阅的消息后的处理 
    override fun onPMessage(pattern: String, channel: String, message: String) {
        println("$pattern=$channel=$message")
    }
}

fun main() {

}
package com.github.wenslo.redisinaction

import redis.clients.jedis.Jedis
import redis.clients.jedis.ZParams


/**
 * Redis In Action 第一章 代码
 */
class Chapter1 {
    companion object {
        private const val ONE_WEEK_IN_SECONDS = 7 * 86400
        private const val VOTE_SCORE: Double = 433.0
        private const val ARTICLES_PER_PAGE = 25
    }

    fun run() {
        val conn = Jedis("127.0.0.1", 6379)
        conn.select(15)
        val articleId = postArticle(conn, "username", "A title", "http://www.google.com")
        println("We posted a new article with id: $articleId")
        println("Its HASH looks like:")
        val articleData: Map<String, String> = conn.hgetAll("article:$articleId")
        for ((key, value) in articleData) {
            println("  $key: $value")
        }

        println()

        articleVote(conn, "other_user", "article:$articleId")
        val votes: String = conn.hget("article:$articleId", "votes")
        println("We voted for the article, it now has votes: $votes")
        assert(votes.toInt() > 1)

        println("The currently highest-scoring articles are:")
        var articles: List<Map<String, String>> = getArticles(conn, 1)
        printArticles(articles)
        assert(articles.isNotEmpty())

        addRemoveGroups(conn, articleId, listOf("new-group"))
        println("We added the article to a new group, other articles include:")
        articles = getGroupArticles(conn, "new-group", 1)
        printArticles(articles)
        assert(articles.isNotEmpty())
    }

    fun postArticle(conn: Jedis, user: String, title: String, link: String): String {
        val articleId = conn.incr("article:").toString()
        val voted = "voted:${articleId}"
        conn.sadd(voted, user)
        conn.expire(voted, ONE_WEEK_IN_SECONDS)
        val now = (System.currentTimeMillis() / 1000).toString()
        val article = "article:$articleId";
        conn.hmset(
            article, mutableMapOf(
                Pair("title", title),
                Pair("link", link),
                Pair("poster", user),
                Pair("time", now),
                Pair("votes", "1")
            )
        )
        return articleId;
    }

    fun articleVote(conn: Jedis, user: String, article: String) {
        val cutoff = (System.currentTimeMillis() / 1000) - ONE_WEEK_IN_SECONDS
        if (conn.zscore("time:", article) < cutoff) {
            return
        }
        var articleId = article.substring(article.indexOf(":") + 1)
        if (conn.sadd("voted:$articleId", user) > 0) {
            conn.zincrby("score:", VOTE_SCORE, article)
            conn.hincrBy(article, "votes", 1)
        }
    }

    fun getArticles(conn: Jedis, page: Long, order: String = "score:"): List<Map<String, String>> {
        val start = (page - 1) * ARTICLES_PER_PAGE
        var end = start + ARTICLES_PER_PAGE - 1
        var ids = conn.zrevrange(order, start, end)
        var articles = mutableListOf<Map<String, String>>()
        ids.forEach {
            val articleData = conn.hgetAll(it)
            articleData[it] = it
            articles.add(articleData)
        }
        return articles
    }

    fun addRemoveGroups(conn: Jedis, articleId: String, toAdd: List<String> = emptyList(), toRemove: List<String> = emptyList()) {
        val article = "article:$articleId"
        for (group in toAdd) {
            conn.sadd("group:$group", article)
        }
        for (group in toRemove) {
            conn.srem("group:$group", article)
        }
    }

    fun getGroupArticles(conn: Jedis, group: String, page: Long, order: String = "score:"): List<Map<String, String>> {
        val key = order + group
        if (!conn.exists(key)) {
            val params = ZParams().aggregate(ZParams.Aggregate.MAX)
            conn.zinterstore(key, params, "group:$group", order)
            conn.expire(key, 60)
        }
        return getArticles(conn, page, key)
    }

    private fun printArticles(articles: List<Map<String, String>>) {
        for (article in articles) {
            println("  id: " + article["id"])
            for ((key, value) in article) {
                if (key == "id") {
                    continue
                }
                println("    $key: $value")
            }
        }
    }
}


fun main() {
    val chapter1 = Chapter1()
    chapter1.run()
}
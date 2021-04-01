package com.github.wenslo.Lucene到Elasticsearch.chapter2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 高亮查询测试
 */
public class HighlighterTest {
    private static final String PATH = "/Users/wenhailin/test";

    public static void main(String[] args) throws Exception {
        String field = "title";
        Path indexPath = Paths.get(PATH);
        Directory dir = FSDirectory.open(indexPath);
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new IKAnalyzer8x();
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse("中方");
        System.out.println("Query:" + query);
        QueryScorer score = new QueryScorer(query, field);
        // 高亮标签
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style=\"color:red;\">", "</span>");
        Highlighter highlighter = new Highlighter(formatter, score);
        // 高亮分词器
        TopDocs tds = searcher.search(query, 10);
        for (ScoreDoc sd : tds.scoreDocs) {
            Document doc = searcher.doc(sd.doc);
            System.out.println("id:" + doc.get("id"));
            System.out.println("title:" + doc.get("title"));
            TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), sd.doc, field, analyzer);
            // 获取token stream
            Fragmenter fragment = new SimpleSpanFragmenter(score);
            highlighter.setTextFragmenter(fragment);
            // 获取高亮的片段
            String str = highlighter.getBestFragment(tokenStream, doc.get(field));
            System.out.println("高亮的片段：" + str);
        }
        dir.close();
        reader.close();
    }
}

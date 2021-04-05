package com.github.wenslo.Lucene到Elasticsearch.chapter3;

import com.github.wenslo.Lucene到Elasticsearch.chapter2.IKAnalyzer8x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SearchFileServlet {
    public static List<FileModel> getTopDoc(String key, String indexPathStr, int n) {
        List<FileModel> hitsList = new ArrayList<>();
        // 检索域
        String[] fields = {"title", "content"};
        Path indexPath = Paths.get(indexPathStr);
        Directory dir;
        try {
            dir = FSDirectory.open(indexPath);
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new IKAnalyzer8x();
            MultiFieldQueryParser parser2 = new MultiFieldQueryParser(fields, analyzer);
            // 查询字符串
            Query query = parser2.parse(key);
            TopDocs topDocs = searcher.search(query, n);
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
            QueryScorer scoreTitle = new QueryScorer(query, fields[0]);
            Highlighter hlqTitle = new Highlighter(formatter, scoreTitle);
            QueryScorer scoreContent = new QueryScorer(query, fields[0]);
            Highlighter hlqContent = new Highlighter(formatter, scoreContent);
            TopDocs hits = searcher.search(query, 100);
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                String title = doc.get("title");
                String content = doc.get("content");
                TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), sd.doc, fields[0], new IKAnalyzer8x());
                Fragmenter fragmenter = new SimpleSpanFragmenter(scoreTitle);
                hlqTitle.setTextFragmenter(fragmenter);
                String hlTitle = hlqTitle.getBestFragment(tokenStream, title);
                // 获取高亮的片段，可以对数量进行限制
                tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), sd.doc, fields[1], new IKAnalyzer8x());
                fragmenter = new SimpleSpanFragmenter(scoreContent);
                hlqContent.setTextFragmenter(fragmenter);
                String hlContent = hlqContent.getBestFragment(tokenStream, content);
                FileModel fm = new FileModel(hlTitle != null ? hlTitle : title, hlContent != null ? hlContent : content);
                hitsList.add(fm);
            }
            dir.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hitsList;
    }

    public static void main(String[] args) {
        List<FileModel> wit = getTopDoc("霸权社", "/Users/wenhailin/test", 10);
        System.out.println(wit);
    }
}

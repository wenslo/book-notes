package com.github.wenslo.Lucene到Elasticsearch.chapter2;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author wenhailin
 * @create 2021/4/1-2:01 下午
 */
public class UpdateIndex {
    private static final String PATH = "/Users/wen/test";

    public static void main(String[] args) {
        Analyzer analyzer = new IKAnalyzer8x();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        Path indexPath = Paths.get(PATH);
        Directory directory;
        try {
            directory = FSDirectory.open(indexPath);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            Document doc = new Document();
            doc.add(new TextField("id", "0", Field.Store.YES));
            doc.add(new TextField("title", "陆永泉：把“轨道上的江苏”打造成引领性品牌", Field.Store.YES));
            doc.add(new TextField("content", "江苏省委十三届九次全会及今年省两会明确提出要建设交通运输现代化示范区。牢记“争当表率、争做示范、走在前列”的重大使命，把“轨道上的江苏”打造成交通运输现代化示范区建设的引领性品牌，正当其时、意义深远。", Field.Store.YES));
            indexWriter.updateDocument(new Term("title", "江苏"), doc);
            indexWriter.commit();
            indexWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

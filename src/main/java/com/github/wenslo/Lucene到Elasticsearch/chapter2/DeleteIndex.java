package com.github.wenslo.Lucene到Elasticsearch.chapter2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author wenhailin
 * @create 2021/4/1-1:43 下午
 */
public class DeleteIndex {
    private static final String PATH = "/Users/wen/test";

    public static void deleteDoc(String field, String key) {
        Analyzer analyzer = new IKAnalyzer8x();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        Path indexPath = Paths.get(PATH);
        Directory directory;
        try {
            directory = FSDirectory.open(indexPath);
            IndexWriter indexWriter = new IndexWriter(directory, config);
            indexWriter.deleteDocuments(new Term(field, key));
            indexWriter.commit();
            indexWriter.close();
            System.out.println("删除完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 删除title中带有中美二字关键词的数据
        deleteDoc("title", "中美");
    }
}

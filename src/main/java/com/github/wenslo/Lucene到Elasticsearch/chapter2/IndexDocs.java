package com.github.wenslo.Lucene到Elasticsearch.chapter2;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;

public class IndexDocs {
    private static final String PATH = "/Users/wenhailin/test";

    public static void main(String[] args) throws Exception {
        File newsFile = new File("/Users/wenhailin/test/lucene.txt");
        String text1 = textToString(newsFile);
        Analyzer smcAnalyzer = new IKAnalyzer8x(true);
        IndexWriterConfig config = new IndexWriterConfig(smcAnalyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // 索引的存储路径
        Directory directory = null;
        // 索引的增删改由indexWriter创建
        IndexWriter indexWriter = null;
        directory = FSDirectory.open(Paths.get(PATH));
        indexWriter = new IndexWriter(directory, config);
        // 新建FieldType，用于指定字段索引时的信息
        FieldType type = new FieldType();
        // 索引时保存文档、词项频率、位置信息、偏移信息
        type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        // 原始字符串全部被保存在索引中
        type.setStored(true);
        // 存储词向量
        type.setStoreTermVectors(true);
        // 词条化
        type.setTokenized(true);
        Document doc1 = new Document();
        Field field1 = new Field("content", text1, type);
        doc1.add(field1);
        indexWriter.addDocument(doc1);
        indexWriter.close();
        directory.close();
    }

    public static String textToString(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str = null;
            while ((str = br.readLine()) != null) {
                result.append(System.lineSeparator() + str);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}

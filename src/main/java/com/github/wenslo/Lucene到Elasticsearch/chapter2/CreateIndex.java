package com.github.wenslo.Lucene到Elasticsearch.chapter2;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

/**
 * @author wenhailin
 * @create 2021/4/1-9:50 上午
 */
public class CreateIndex {
    public static void main(String[] args) {
        News news1 = new News();
        news1.setId(1);
        news1.setTitle("中方谈中美高层战略对话");
        news1.setContent("杨洁篪表示，过去两天，我和王毅国务委员兼外长同布林肯国务卿、沙利文助理进行了长时间战略沟通，就各自内外政策和双边关系进行了坦诚、建设性交流。这次对话是有益的，有利于增进相互了解。双方在一些问题上仍存在重要分歧。");
        news1.setReply(672);

        News news2 = new News();
        news2.setTitle("陆永泉：把“轨道上的江苏”打造成引领性品牌");
        news2.setContent("江苏省委十三届九次全会及今年省两会明确提出要建设交通运输现代化示范区。牢记“争当表率、争做示范、走在前列”的重大使命，把“轨道上的江苏”打造成交通运输现代化示范区建设的引领性品牌，正当其时、意义深远。");
        news2.setReply(995);


        News news3 = new News();
        news3.setTitle("沈正平：加快推进“一带一路”交汇点建设");
        news3.setContent("江苏“十四五”规划作出“基本建成具有全球影响力的产业科技创新中心、具有国际竞争力的先进制造业基地、具有世界聚合力的双向开放枢纽”的重大战略部署，与“十三五”规划相比，“一枢纽”是新增的重要目标，这是立足新发展阶段、贯彻新发展理念、构建新发展格局的新任务新要求，也是贯彻落实“争当表率、争做示范、走在前列”的使命担当，具有十分重要的意义和作用。");
        news3.setReply(1872);

        // 创建IK分词器
        Analyzer analyzer = new IKAnalyzer8x();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory dir = null;
        IndexWriter indexWriter = null;
        // 索引目录
        Path indexPath = Paths.get("/Users/wen/test");
        // 开始时间
        Date start = new Date();
        try {
            if (!Files.isReadable(indexPath)) {
                System.out.println("Document directory '" + indexPath.toAbsolutePath() + "' does not exist or is not readable, please check the path.");
                System.exit(1);
            }
            dir = FSDirectory.open(indexPath);
            indexWriter = new IndexWriter(dir, config);
            // 设置新闻ID索引并存储
            FieldType idType = new FieldType();
            idType.setIndexOptions(IndexOptions.DOCS);
            idType.setStored(true);
            // 设置新闻标题索引文档、词项频率、位移信息和偏移量，存储并词条化
            FieldType titleType = new FieldType();
            titleType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            titleType.setStored(true);
            titleType.setTokenized(true);

            FieldType contentType = new FieldType();
            contentType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
            contentType.setStored(true);
            contentType.setTokenized(true);
            contentType.setStoreTermVectors(true);
            contentType.setStoreTermVectorPositions(true);
            contentType.setStoreTermVectorOffsets(true);
            contentType.setStoreTermVectorPayloads(true);

            Document doc1 = new Document();
            doc1.add(new Field("id", String.valueOf(news1.getId()), idType));
            doc1.add(new Field("title", news1.getTitle(), titleType));
            doc1.add(new Field("content", news1.getContent(), contentType));
            doc1.add(new IntPoint("reply", news1.getReply()));
            doc1.add(new StoredField("reply_display", news1.getReply()));

            Document doc2 = new Document();
            doc2.add(new Field("id", String.valueOf(news2.getId()), idType));
            doc2.add(new Field("title", news2.getTitle(), titleType));
            doc2.add(new Field("content", news2.getContent(), contentType));
            doc2.add(new IntPoint("reply", news2.getReply()));
            doc2.add(new StoredField("reply_display", news2.getReply()));

            Document doc3 = new Document();
            doc2.add(new Field("id", String.valueOf(news3.getId()), idType));
            doc2.add(new Field("title", news3.getTitle(), titleType));
            doc2.add(new Field("content", news3.getContent(), contentType));
            doc2.add(new IntPoint("reply", news3.getReply()));
            doc2.add(new StoredField("reply_display", news3.getReply()));

            indexWriter.addDocument(doc1);
            indexWriter.addDocument(doc2);
            indexWriter.addDocument(doc3);
            indexWriter.commit();
            indexWriter.close();
            dir.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date end = new Date();
        System.out.println("索引文档用时：" + (end.getTime() - start.getTime()) + " milliseconds");
    }
}

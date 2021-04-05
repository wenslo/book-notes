package com.github.wenslo.Lucene到Elasticsearch.chapter3;

import com.github.wenslo.Lucene到Elasticsearch.chapter2.IKAnalyzer8x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateIndex {
    public static List<FileModel> extractFile() throws Exception {
        List<FileModel> list = new ArrayList<>();
        File fileDir = new File("/Users/wenhailin/test/files");
        File[] allFiles = fileDir.listFiles();
        for (File f : allFiles) {
            FileModel sf = new FileModel(f.getName(), parserExtraction(f));
            list.add(sf);
        }
        return list;
    }

    private static String parserExtraction(File file) {
        // 接收文档内容
        String fileContent = "";
        BodyContentHandler handler = new BodyContentHandler();
        Parser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            ParseContext context = new ParseContext();
            parser.parse(inputStream, handler, metadata, context);
            fileContent = handler.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public static void main(String[] args) throws Exception {
        Analyzer analyzer = new IKAnalyzer8x();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory dir = null;
        IndexWriter indexWriter = null;
        Path indexPath = Paths.get("/Users/wenhailin/test");
        FieldType fieldType = new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fieldType.setStored(true);
        fieldType.setTokenized(true);
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.setStoreTermVectorOffsets(true);
        Date start = new Date();
        if (!Files.isReadable(indexPath)) {
            System.out.println(indexPath.toAbsolutePath() + "不存在或者不可读，请检查！");
            System.exit(1);
        }
        dir = FSDirectory.open(indexPath);
        indexWriter = new IndexWriter(dir, config);
        List<FileModel> fileList = extractFile();
        // 遍历，建立索引
        for (FileModel f : fileList) {
            Document doc = new Document();
            doc.add(new Field("title", f.getTitle(), fieldType));
            doc.add(new Field("content", f.getContent(), fieldType));
            indexWriter.addDocument(doc);
        }
        indexWriter.commit();
        indexWriter.close();
        dir.close();
        Date end = new Date();
        System.out.println("索引文档完成，共耗时：" + (end.getTime() - start.getTime()) + "毫秒");
    }
}

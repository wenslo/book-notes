package com.github.wenslo.Lucene到Elasticsearch.chapter3;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateIndex {
    public static List<FileModel> extractFile() throws Exception {
        List<FileModel> list = new ArrayList<>();
        File fileDir = new File("/Users/wen/test/files");
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
}

package com.github.wenslo.Lucene到Elasticsearch.chapter3;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import java.io.File;
import java.io.FileInputStream;

/**
 * 自动解析
 */
public class ParserExtraction {
    public static void main(String[] args) throws Exception {
        File fileDir = new File("/Users/wen/test/files");
        if (!fileDir.exists()) {
            System.out.println("文件夹不存在，请检查！");
            System.exit(0);
        }
        // 获取文件夹下的所有文件，存放在File数组中
        File[] fileArr = fileDir.listFiles();
        // 创建内容处理器对象
        BodyContentHandler handler = new BodyContentHandler();
        // 创建元数据对象
        Metadata metadata = new Metadata();
        FileInputStream inputStream = null;
        Parser parser = new AutoDetectParser();
        // 自动监测分词器
        ParseContext context = new ParseContext();
        for (File file : fileArr) {
            inputStream = new FileInputStream(file);
            parser.parse(inputStream, handler, metadata, context);
            System.out.println(file.getName() + "：\n" + handler.toString());
        }
    }
}
